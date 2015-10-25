package org.robovm.samples.contractr.android.fragment;

import org.robovm.samples.contractr.core.Task;

public class AddTaskFragment extends AbstractTaskFragment {
    public static AddTaskFragment newInstance() {
        return new AddTaskFragment();
    }

    public AddTaskFragment() {}

    @Override
    protected String getTitle() {
        return "Add task";
    }

    @Override
    protected void onSave() {
        Task task = saveViewValuesToTask(taskModel.create(client));
        taskModel.save(task);
        super.onSave();
    }
}
