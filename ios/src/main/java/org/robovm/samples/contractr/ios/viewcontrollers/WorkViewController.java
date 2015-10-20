/*
 * Copyright (C) 2014 RoboVM AB
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.robovm.samples.contractr.ios.viewcontrollers;

import net.engio.mbassy.listener.Handler;
import org.robovm.apple.coreanimation.CALayer;
import org.robovm.apple.dispatch.DispatchQueue;
import org.robovm.apple.uikit.*;
import org.robovm.objc.annotation.CustomClass;
import org.robovm.objc.annotation.IBAction;
import org.robovm.objc.annotation.IBOutlet;
import org.robovm.samples.contractr.core.ClientModel;
import org.robovm.samples.contractr.core.Task;
import org.robovm.samples.contractr.core.TaskModel;
import org.robovm.samples.contractr.core.TaskModel.WorkStartedEvent;
import org.robovm.samples.contractr.core.TaskModel.WorkStoppedEvent;
import org.robovm.samples.contractr.ios.ContractRApp;
import org.robovm.samples.contractr.ios.IOSColors;

import javax.inject.Inject;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

@CustomClass("WorkViewController")
public class WorkViewController extends InjectedViewController {

    @Inject ClientModel clientModel;
    @Inject TaskModel taskModel;

    @IBOutlet UIButton startStopButton;
    @IBOutlet UILabel currentClientLabel;
    @IBOutlet UIView currentClientColorView;
    @IBOutlet UILabel currentTaskLabel;
    @IBOutlet UILabel earnedLabel;
    @IBOutlet UILabel timerLabel;
    private boolean showing = true;

    @Handler
    public void workStarted(WorkStartedEvent event) {
        DispatchQueue.getMainQueue().async(this::updateUIComponents);
    }

    @Handler
    public void workStopped(WorkStoppedEvent event) {
        DispatchQueue.getMainQueue().async(() -> {
            updateUIComponents();
            tick(); // Resets timer to 00:00:00
        });
    }

    @Override
    public void viewWillAppear(boolean animated) {
        super.viewWillAppear(animated);
        taskModel.subscribe(this);
        showing = true;
        updateUIComponents();
        tick();
    }

    @Override
    public void viewWillDisappear(boolean animated) {
        taskModel.unsubscribe(this);
        showing = false;
        super.viewWillDisappear(animated);
    }

    @IBAction void startStopClicked() {
        Task workingTask = taskModel.getWorkingTask();
        if (workingTask == null) {
            performSegue("selectTaskSegue", this);
        } else {
            taskModel.stopWork();
        }
    }

    private void updateUIComponents() {
        Task task = taskModel.getWorkingTask();
        UIColor startStopColor = null;
        String startStopTitle = null;
        String currentTaskText = null;
        if (task == null) {
            startStopTitle = "Start work";
            startStopColor = IOSColors.START_WORK;
            currentTaskText = "None";
            currentClientLabel.setHidden(true);
            currentClientColorView.setHidden(true);
        } else {
            startStopTitle = "Stop work";
            startStopColor = IOSColors.STOP_WORK;
            currentTaskText = task.getTitle();
            currentClientLabel.setText(task.getClient().getName());
            currentClientLabel.setHidden(false);
            currentClientColorView.setBackgroundColor(
                    IOSColors.getClientColor(clientModel.indexOf(task.getClient())));
            currentClientColorView.setHidden(false);
        }
        startStopButton.setTitle(startStopTitle, UIControlState.Normal);
        startStopButton.setBackgroundColor(startStopColor);
        currentTaskLabel.setText(currentTaskText);
    }

    private void tick() {
        if (!showing) {
            return;
        }
        Task task = taskModel.getWorkingTask();
        if (task != null) {
            timerLabel.setText(task.getTimeElapsed());
            earnedLabel.setText(task.getAmountEarned(Locale.US));
            DispatchQueue.getMainQueue().after(1, TimeUnit.SECONDS, this::tick);
        } else {
            timerLabel.setText("00:00:00");
            earnedLabel.setText(NumberFormat.getCurrencyInstance(Locale.US).format(0));
        }
    }
}
