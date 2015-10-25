package org.robovm.samples.contractr.android.fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.inject.Inject;
import org.robovm.samples.contractr.android.R;
import org.robovm.samples.contractr.android.adapter.TaskListAdapter;

import org.robovm.samples.contractr.core.ClientModel;
import org.robovm.samples.contractr.core.Task;
import org.robovm.samples.contractr.core.TaskModel;

public class TasksFragment extends ListFragment {
    @Inject
    TaskModel taskModel;
    @Inject
    ClientModel clientModel;

    private TaskListAdapter adapter;

    public static TasksFragment newInstance() {
        return new TasksFragment();
    }

    public TasksFragment() {}

    @Override
    protected void onAdd() {
        AddTaskFragment f = AddTaskFragment.newInstance();
        openDialog(f);
    }

    @Override
    protected void onEdit(int row) {
        EditTaskFragment f = EditTaskFragment.newInstance();
        Task task = (Task) adapter.getItem(row);
        f.setTask(task);
        openDialog(f);
    }

    protected void onDelete(final int row) {
        final Task task = (Task) adapter.getItem(row);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setMessage("Are you sure you want to delete " + task.getTitle() + "?")
                .setPositiveButton(android.R.string.ok, (dialog, id) -> {
                    taskModel.delete(task);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(getActivity(), "Task deleted", Toast.LENGTH_SHORT);
                })
                .setNegativeButton(android.R.string.cancel, (dialog, id) -> {
                    // User cancelled the dialog
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new TaskListAdapter(taskModel, clientModel, inflater, false);
        listView.setAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_task, container, false);
    }

    public void taskSaved() {
        adapter.notifyDataSetChanged();
    }

}
