package org.robovm.samples.contractr.android.fragment;

public class EditTaskFragment extends AbstractTaskFragment {

    public static EditTaskFragment newInstance() {
        return new EditTaskFragment();
    }

    public EditTaskFragment() {}

    @Override
    protected String getTitle() {
        return "Edit task";
    }

    @Override
    public void onResume() {
        updateViewValuesWithTask(task);
        super.onResume();
    }

    @Override
    protected void onSave() {
        taskModel.save(saveViewValuesToTask(task));
        super.onSave();
    }
}
