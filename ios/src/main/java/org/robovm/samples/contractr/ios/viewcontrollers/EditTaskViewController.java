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
import org.robovm.apple.uikit.UIPickerView;
import org.robovm.apple.uikit.UIPickerViewDataSourceAdapter;
import org.robovm.apple.uikit.UIPickerViewDelegateAdapter;
import org.robovm.apple.uikit.UISwitch;
import org.robovm.apple.uikit.UITableView;
import org.robovm.apple.uikit.UITextField;
import org.robovm.objc.annotation.CustomClass;
import org.robovm.objc.annotation.IBAction;
import org.robovm.objc.annotation.IBOutlet;
import org.robovm.samples.contractr.core.Client;
import org.robovm.samples.contractr.core.ClientModel;
import org.robovm.samples.contractr.core.Task;
import org.robovm.samples.contractr.core.TaskModel;

import javax.inject.Inject;

/**
 * 
 */
@CustomClass("EditTaskViewController")
public class EditTaskViewController extends InjectedTableViewController {
    private static final int CLIENT_PICKER_ROW = 1;
    private static final int CLIENT_PICKER_CELL_HEIGHT = 219;

    private boolean clientPickerShowing = false;

    @Inject ClientModel clientModel;
    @Inject TaskModel taskModel;

    @IBOutlet UIPickerView clientPicker;
    @IBOutlet UITextField clientTextField;
    @IBOutlet UITextField titleTextField;
    @IBOutlet UITextField notesTextField;
    @IBOutlet UISwitch finishedSwitch;

    @Override
    public void viewDidLoad() {
        super.viewDidLoad();

        clientPicker.setDataSource(new UIPickerViewDataSourceAdapter() {
            @Override
            public long getNumberOfComponents(UIPickerView pickerView) {
                return 1;
            }
            @Override
            public long getNumberOfRows(UIPickerView pickerView, long component) {
                return clientModel.count() + 1;
            }
        });
        clientPicker.setDelegate(new UIPickerViewDelegateAdapter() {
            @Override
            public String getRowTitle(UIPickerView pickerView, long row,
                                      long component) {
                return row == 0 ? "" : clientModel.get((int) row - 1).getName();
            }

            @Override
            public void didSelectRow(UIPickerView pickerView, long row, long component) {
                Client client = getSelectedClient();
                if (client != null) {
                    clientTextField.setText(client.getName());
                } else {
                    clientTextField.setText("");
                }
                showHideClientPicker(false);
                updateSaveButtonEnabled();
            }
        });
    }

    @Override
    public void viewWillAppear(boolean animated) {
        super.viewWillAppear(animated);

        showHideClientPicker(false);

        Task task = taskModel.getSelectedTask();
        if (task == null) {
            getNavigationItem().setTitle("Add task");
        } else {
            getNavigationItem().setTitle("Edit task");
        }
        updateViewValuesWithTask(task);
    }

    @IBAction
    private void save() {
        Task task = taskModel.getSelectedTask();
        if (task == null) {
            Client client = getSelectedClient();
            taskModel.save(saveViewValuesToTask(taskModel.create(client)));
        } else {
            taskModel.save(saveViewValuesToTask(task));
        }
        getNavigationController().popViewController(true);
    }

    private Client getSelectedClient() {
        if (clientPicker.getSelectedRow(0) == 0) {
            return null;
        }
        return clientModel.get((int) clientPicker.getSelectedRow(0) - 1);
    }

    @IBAction
    private void updateSaveButtonEnabled() {
        Client client = getSelectedClient();
        String title = titleTextField.getText();
        title = title == null ? "" : title.trim();
        boolean canSave = !title.isEmpty() && client != null;
        getNavigationItem().getRightBarButtonItem().setEnabled(canSave);
    }

    @IBAction void hideClientPicker() {
        showHideClientPicker(false);
    }

    private void showHideClientPicker(boolean show) {
        clientPickerShowing = show;
        clientPicker.setHidden(!clientPickerShowing);
        // Calling beginUpdate() / endUpdates() animates the table view nicely.
        getTableView().beginUpdates();
        getTableView().endUpdates();
        if (show) {
            getView().endEditing(false);
        }
    }

    protected void updateViewValuesWithTask(Task task) {
        Client client = task == null ? null : task.getClient();
        int selectedRow = 0;
        if (client != null) {
            for (int i = 0; i < clientModel.count(); i++) {
                if (clientModel.get(i).equals(client)) {
                    selectedRow = i + 1;
                    break;
                }
            }
        }
        clientPicker.selectRow(selectedRow, 0, false);
        clientTextField.setText(task == null ? "" : task.getClient().getName());
        titleTextField.setText(task == null ? "" : task.getTitle());
        notesTextField.setText(task == null ? "" : task.getNotes());
        finishedSwitch.setOn(task == null ? false : task.isFinished());
        updateSaveButtonEnabled();
    }

    protected Task saveViewValuesToTask(Task task) {
        String title = titleTextField.getText();
        title = title == null ? "" : title.trim();
        String notes = notesTextField.getText();
        notes = notes == null ? "" : notes.trim();

        Client client = getSelectedClient();
        task.setClient(client);
        task.setTitle(title);
        task.setNotes(notes);
        task.setFinished(finishedSwitch.isOn());

        return task;
    }

    @Override
    public void didSelectRow(UITableView tableView, NSIndexPath indexPath) {
        if (indexPath.getRow() == CLIENT_PICKER_ROW) {
            // Client picker row selected. Ignore.
        } else if (indexPath.getRow() == CLIENT_PICKER_ROW - 1) {
            // Client text field row selected. Show client picker.
            showHideClientPicker(true);
            getTableView().deselectRow(indexPath, false);
        } else {
            // Some other row selected. Hide the client picker.
            showHideClientPicker(false);
        }
    }

    @Override
    public double getHeightForRow(UITableView tableView, NSIndexPath indexPath) {
        if (indexPath.getRow() == CLIENT_PICKER_ROW) {
            return clientPickerShowing ? CLIENT_PICKER_CELL_HEIGHT : 0;
        }
        return super.getHeightForRow(tableView, indexPath);
    }
}
