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
import org.robovm.apple.foundation.NSIndexPath;
import org.robovm.apple.uikit.UIBarButtonItem;
import org.robovm.apple.uikit.UITableView;
import org.robovm.apple.uikit.UITableViewCellEditingStyle;
import org.robovm.apple.uikit.UITableViewController;
import org.robovm.objc.annotation.CustomClass;
import org.robovm.objc.annotation.IBAction;
import org.robovm.objc.annotation.IBOutlet;

/**
 * Abstract {@link UITableViewController} which displays a list of objects and
 * supports adding and removing objects.
 */
@CustomClass("ListViewController")
public abstract class ListViewController extends InjectedTableViewController {

    @IBOutlet UIBarButtonItem editBarButtonItem;
    @IBOutlet UIBarButtonItem doneBarButtonItem;

    @IBAction
    private void add() {
        onAdd();
    }

    @IBAction
    private void edit() {
        setEditing(true, true);
    }

    @IBAction
    private void done() {
        setEditing(false, true);
    }

    @Override
    public void setEditing(boolean editing, boolean animated) {
        getNavigationItem().setLeftBarButtonItems(new NSArray<>());
        if (editing) {
            getNavigationItem().setLeftBarButtonItem(doneBarButtonItem);
        } else {
            getNavigationItem().setLeftBarButtonItem(editBarButtonItem);
        }
        super.setEditing(editing, animated);
    }

    @Override
    public void viewWillAppear(boolean animated) {
        super.viewWillAppear(animated);

        getTableView().reloadData();
        setEditing(false);
    }

    protected abstract void onAdd();

    protected abstract void onEdit(int section, int row);

    protected abstract void onDelete(int section, int row);

    @Override
    public void didSelectRow(UITableView tableView, NSIndexPath indexPath) {
        onEdit(indexPath.getSection(), indexPath.getRow());
    }

    @Override
    public long getNumberOfSections(UITableView tableView) {
        return 1;
    }

    @Override
    public void commitEditingStyleForRow(UITableView tableView, UITableViewCellEditingStyle editingStyle,
            NSIndexPath indexPath) {

        if (editingStyle == UITableViewCellEditingStyle.Delete) {
            onDelete(indexPath.getSection(), indexPath.getRow());
        }
    }
}
