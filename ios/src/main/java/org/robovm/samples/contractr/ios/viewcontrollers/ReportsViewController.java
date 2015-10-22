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

import org.robovm.apple.dispatch.DispatchQueue;
import org.robovm.apple.foundation.NSIndexPath;
import org.robovm.apple.uikit.*;
import org.robovm.objc.annotation.CustomClass;
import org.robovm.objc.annotation.IBAction;
import org.robovm.objc.annotation.IBOutlet;
import org.robovm.samples.contractr.core.Client;
import org.robovm.samples.contractr.core.ClientModel;
import org.robovm.samples.contractr.ios.IOSColors;
import org.robovm.samples.contractr.ios.views.PieChartView;
import org.robovm.samples.contractr.ios.views.PieChartView.Component;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

@CustomClass("ReportsViewController")
public class ReportsViewController extends InjectedViewController {
    @Inject ClientModel clientModel;
    @IBOutlet PieChartView pieChart;
    @IBOutlet UITableView clientsTableView;
    @IBOutlet UISegmentedControl typeSegmentedControl;
    private boolean showing = true;

    @CustomClass("ReportsClientCell")
    public static class ReportsClientCell extends UITableViewCell {
        @IBOutlet UIView colorView;
        @IBOutlet UILabel nameLabel;
        @IBOutlet UILabel valueLabel;
    }

    @Override
    public void viewDidLoad() {
        super.viewDidLoad();

        clientsTableView.setDataSource(new UITableViewDataSourceAdapter() {
            @Override
            public UITableViewCell getCellForRow(UITableView tableView, NSIndexPath indexPath) {
                boolean showAmount = typeSegmentedControl.getSelectedSegment() == 0;
                ReportsClientCell cell = (ReportsClientCell) tableView.dequeueReusableCell("cell");
                Client client = clientModel.get(indexPath.getRow());
                cell.colorView.setBackgroundColor(IOSColors.getClientColor(clientModel.indexOf(client)));
                cell.nameLabel.setText(client.getName());
                if (showAmount) {
                    cell.valueLabel.setText(clientModel.getTotalAmountEarned(client, Locale.US));
                } else {
                    cell.valueLabel.setText(clientModel.getTotalTimeElapsed(client));
                }
                return cell;
            }

            @Override
            public long getNumberOfSections(UITableView tableView) {
                return 1;
            }

            @Override
            public long getNumberOfRowsInSection(UITableView tableView, long section) {
                return clientModel.count();
            }
        });
    }

    @Override
    public void viewWillAppear(boolean b) {
        super.viewWillAppear(b);

        showing = true;
        loop();
    }

    @Override
    public void viewWillDisappear(boolean b) {
        showing = false;
        super.viewWillDisappear(b);
    }

    @IBAction public void chartTypeChanged() {
        updatePieChart();
        clientsTableView.reloadData();
    }

    private void loop() {
        if (showing) {
            updatePieChart();
            clientsTableView.reloadData();
            DispatchQueue.getMainQueue().after(1, TimeUnit.SECONDS, this::loop);
        }
    }

    private void updatePieChart() {
        boolean showAmount = typeSegmentedControl.getSelectedSegment() == 0;

        List<Component> components = new ArrayList<>();
        for (int i = 0; i < clientModel.count(); i++) {
            Client client = clientModel.get(i);
            UIColor color = IOSColors.getClientColor(i);
            if (showAmount) {
                components.add(new Component(color,
                        clientModel.getTotalAmountEarned(client).doubleValue()));
            } else {
                components.add(new Component(color,
                        clientModel.getTotalSecondsElapsed(client)));
            }
        }
        pieChart.setComponents(components);
    }

}
