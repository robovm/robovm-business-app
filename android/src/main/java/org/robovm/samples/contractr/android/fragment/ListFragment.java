package org.robovm.samples.contractr.android.fragment;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import com.google.inject.Inject;
import org.robovm.samples.contractr.android.R;
import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;

public abstract class ListFragment extends RoboFragment
        implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    @Inject
    protected LayoutInflater inflater;
    @InjectView(android.R.id.list)
    protected AbsListView listView;
    @InjectView(R.id.add)
    protected FloatingActionButton addButton;

    public ListFragment() {}

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);
        addButton.setOnClickListener((v) -> onAdd());
    }

    protected abstract void onEdit(int row);

    protected abstract void onAdd();

    protected abstract void onDelete(int row);

    protected void openDialog(DialogFragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();

        FragmentTransaction ft = fragmentManager.beginTransaction();
        Fragment prev = fragmentManager.findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        fragment.show(ft, "dialog");
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        onEdit(position);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        onDelete(position);
        return true;
    }
}
