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

import org.robovm.apple.foundation.NSIndexPath;
import org.robovm.apple.uikit.UITableView;
import org.robovm.apple.uikit.UITableViewCell;
import org.robovm.apple.uikit.UITableViewCellAccessoryType;
import org.robovm.apple.uikit.UITableViewCellStyle;
import org.robovm.objc.annotation.CustomClass;
import org.robovm.objc.annotation.IBAction;
import org.robovm.samples.contractr.core.Client;
import org.robovm.samples.contractr.core.ClientModel;
import org.robovm.samples.contractr.core.Task;
import org.robovm.samples.contractr.core.TaskModel;

import javax.inject.Inject;

/**
 * 
 */
@CustomClass("SelectTaskViewController")
public class SelectTaskViewController extends InjectedTableViewController {
    @Inject ClientModel clientModel;
    @Inject TaskModel taskModel;

    @IBAction
    private void cancel() {
        dismissViewController(true, null);
    }

    @Override
    public void viewWillAppear(boolean animated) {
        super.viewWillAppear(animated);
        getTableView().reloadData();
    }

    private Task getTaskForRow(NSIndexPath indexPath) {
        Client client = clientModel.get(indexPath.getSection());
        return taskModel.getForClient(client, true).get(indexPath.getRow());
    }

    @Override
    public void didSelectRow(UITableView tableView, NSIndexPath indexPath) {
        taskModel.startWork(getTaskForRow(indexPath));
        dismissViewController(true, null);
    }

    @Override
    public UITableViewCell getCellForRow(UITableView tableView, NSIndexPath indexPath) {
        UITableViewCell cell = tableView.dequeueReusableCell("cell");
        if (cell == null) {
            cell = new UITableViewCell(UITableViewCellStyle.Value1, "cell");
            cell.setAccessoryType(UITableViewCellAccessoryType.DisclosureIndicator);
        }
        Task task = getTaskForRow(indexPath);
        cell.getTextLabel().setText(task.getTitle());
        cell.getDetailTextLabel().setText(task.getTimeElapsed());
        return cell;
    }

    @Override
    public String getTitleForHeader(UITableView tableView, long section) {
        Client client = clientModel.get((int) section);
        return client.getName();
    }

    @Override
    public long getNumberOfSections(UITableView tableView) {
        return clientModel.count();
    }

    @Override
    public long getNumberOfRowsInSection(UITableView tableView, long section) {
        Client client = clientModel.get((int) section);
        return taskModel.getForClient(client, true).size();
    }
}
