package org.robovm.samples.contractr.android.fragment;

import android.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditClientFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditClientFragment extends AbstractClientFragment {

    public static EditClientFragment newInstance() {
        return new EditClientFragment();
    }

    public EditClientFragment() {
        // Required empty public constructor
    }

    @Override
    protected String getTitle() {
        return "Edit client";
    }

    @Override
    protected void onSave() {
        clientModel.save(saveViewValuesToClient(client));
        super.onSave();
    }
}
