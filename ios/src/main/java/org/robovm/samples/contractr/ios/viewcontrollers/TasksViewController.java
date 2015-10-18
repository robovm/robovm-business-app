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

import org.robovm.apple.foundation.NSArray;
import org.robovm.apple.foundation.NSAttributedString;
import org.robovm.apple.foundation.NSIndexPath;
import org.robovm.apple.uikit.NSAttributedStringAttributes;
import org.robovm.apple.uikit.NSIndexPathExtensions;
import org.robovm.apple.uikit.NSUnderlineStyle;
import org.robovm.apple.uikit.UITableView;
import org.robovm.apple.uikit.UITableViewCell;
import org.robovm.apple.uikit.UITableViewCellAccessoryType;
import org.robovm.apple.uikit.UITableViewCellStyle;
import org.robovm.apple.uikit.UITableViewRowAnimation;
import org.robovm.objc.annotation.CustomClass;
import org.robovm.samples.contractr.core.Client;
import org.robovm.samples.contractr.core.ClientModel;
import org.robovm.samples.contractr.core.Task;
import org.robovm.samples.contractr.core.TaskModel;

import javax.inject.Inject;

/**
 * 
 */
@CustomClass("TasksViewController")
public class TasksViewController extends ListViewController {

    private static final NSAttributedStringAttributes strikeThroughAttrs;

    static {
        strikeThroughAttrs = new NSAttributedStringAttributes();
        strikeThroughAttrs.setStrikethroughStyle(NSUnderlineStyle.StyleSingle);
    }

    @Inject ClientModel clientModel;
    @Inject TaskModel taskModel;

    @Override
    public void viewDidLoad() {
        super.viewDidLoad();

        getNavigationItem().setTitle("Tasks");
    }

    @Override
    protected void onAdd() {
        clientModel.selectClient(null);
        taskModel.selectTask(null);
        performSegue("editTaskSegue", this);
    }

    @Override
    protected void onEdit(int section, int row) {
        Client client = clientModel.get(section);
        Task task = taskModel.getForClient(client, false).get(row);
        clientModel.selectClient(client);
        taskModel.selectTask(task);
        performSegue("editTaskSegue", this);
    }

    @Override
    protected void onDelete(int section, int row) {
        Client client = clientModel.get(section);
        Task task = taskModel.getForClient(client, false).get(row);
        taskModel.delete(task);
        getTableView().deleteRows(
                new NSArray<>(NSIndexPathExtensions.createIndexPathForRowInSection(row, section)),
                UITableViewRowAnimation.Automatic);
    }

    @Override
    public UITableViewCell getCellForRow(UITableView tableView, NSIndexPath indexPath) {
        UITableViewCell cell = tableView.dequeueReusableCell("cell");
        if (cell == null) {
            cell = new UITableViewCell(UITableViewCellStyle.Value1, "cell");
            cell.setAccessoryType(UITableViewCellAccessoryType.DisclosureIndicator);
        }
        Client client = clientModel.get(indexPath.getSection());
        Task task = taskModel.getForClient(client, false).get(indexPath.getRow());
        String title = task.getTitle();
        if (task.isFinished()) {
            NSAttributedString attributedTitle = new NSAttributedString(title, strikeThroughAttrs);
            cell.getTextLabel().setAttributedText(attributedTitle);
        } else {
            cell.getTextLabel().setText(title);
        }
        cell.getDetailTextLabel().setText(task.getNotes());
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
        return taskModel.getForClient(client, false).size();
    }
}
