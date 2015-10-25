package org.robovm.samples.contractr.android.fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.*;
import android.widget.TextView;
import android.widget.ToggleButton;
import com.google.inject.Inject;
import org.robovm.samples.contractr.android.R;
import org.robovm.samples.contractr.android.adapter.TaskListAdapter;
import org.robovm.samples.contractr.core.ClientModel;
import org.robovm.samples.contractr.core.Task;
import org.robovm.samples.contractr.core.TaskModel;
import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;

import java.text.NumberFormat;
import java.util.Locale;

public class WorkFragment extends RoboFragment implements View.OnClickListener {
    @Inject
    private LayoutInflater inflater;
    private Task task;

    @InjectView(R.id.startStopButton)
    ToggleButton startStopButton;
    @InjectView(R.id.currentTaskLabel)
    TextView currentTaskLabel;
    @InjectView(R.id.timerLabel)
    TextView timerLabel;
    @InjectView(R.id.amountEarned)
    TextView amountEarned;

    private boolean showing = false;
    @Inject
    private TaskModel taskModel;
    @Inject
    private ClientModel clientModel;

    public static WorkFragment newInstance() {
        WorkFragment fragment = new WorkFragment();
        return fragment;
    }

    public WorkFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        showing = true;
        updateUIComponents();
        tick();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        startStopButton.setOnClickListener(this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        showing = !hidden;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_work, container, false);
    }

    @Override
    public void onClick(View v) {
        Task workingTask = taskModel.getWorkingTask();
        if (workingTask == null) {
            final TaskListAdapter adapter = new TaskListAdapter(taskModel, clientModel, inflater, true);
            new AlertDialog.Builder(getActivity())
                    .setTitle("Select task:")
                    .setOnCancelListener(dialog -> updateUIComponents())
                    .setAdapter(adapter, (dialog, which) -> {
                        task = (Task) adapter.getItem(which);
                        start(task);
                    }).create().show();
        } else {
            stop();
        }
    }

    private void updateUIComponents() {
        Task task = taskModel.getWorkingTask();
        String currentTaskText;
        if (task == null) {
            currentTaskText = "None";
            startStopButton.setChecked(true);
        } else {
            currentTaskText = task.getClient().getName() + " - " + task.getTitle();
            startStopButton.setChecked(false);
        }
        currentTaskLabel.setText(currentTaskText);

    }

    private void start(Task task) {
        taskModel.startWork(task);
        updateUIComponents();
        tick();
    }

    private void stop() {
        taskModel.stopWork();
        updateUIComponents();
        tick(); // Resets timer to 00:00:00
    }

    private void tick() {
        if (!showing) {
            return;
        }
        Task task = taskModel.getWorkingTask();
        if (task != null) {
            timerLabel.setText(task.getTimeElapsed());
            amountEarned.setText(task.getAmountEarned(Locale.US));
            final Handler handler = new Handler();
            handler.postDelayed(this::tick, 1000);
        } else {
            timerLabel.setText("00:00:00");
            amountEarned.setText(NumberFormat.getCurrencyInstance(Locale.US).format(0));
        }
    }

}
