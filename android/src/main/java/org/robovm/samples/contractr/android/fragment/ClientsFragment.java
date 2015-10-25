package org.robovm.samples.contractr.android.fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.google.inject.Inject;
import org.robovm.samples.contractr.android.R;
import org.robovm.samples.contractr.android.adapter.ClientListAdapter;
import org.robovm.samples.contractr.core.Client;
import org.robovm.samples.contractr.core.ClientModel;

public class ClientsFragment extends ListFragment {
    @Inject
    private ClientModel clientModel;

    private ClientListAdapter mAdapter;

    public static ClientsFragment newInstance() {
        return new ClientsFragment();
    }

    public ClientsFragment() {}

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAdapter = new ClientListAdapter(clientModel, inflater);
        listView.setAdapter(mAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_client, container, false);
    }

    public void clientSaved() {
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onAdd() {
        AddClientFragment f = AddClientFragment.newInstance();
        openDialog(f);
    }

    @Override
    protected void onEdit(int row) {
        EditClientFragment f = EditClientFragment.newInstance();
        Client client = clientModel.get(row);
        f.setClient(client);
        openDialog(f);
    }

    protected void onDelete(final int row) {
        final Client client = clientModel.get(row);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setMessage("Are you sure you want to delete " + client.getName())
                .setPositiveButton(android.R.string.ok, (dialog, id) -> {
                    clientModel.delete(client);
                    mAdapter.notifyDataSetChanged();
                    Toast.makeText(getActivity(), "Client deleted", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton(android.R.string.cancel, (dialog, id) -> {
                    // User cancelled the dialog
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
