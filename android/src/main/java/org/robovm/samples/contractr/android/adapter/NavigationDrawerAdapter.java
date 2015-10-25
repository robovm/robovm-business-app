package org.robovm.samples.contractr.android.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import org.robovm.samples.contractr.android.fragment.NavigationDrawerItem;
import org.robovm.samples.contractr.android.R;

import java.util.Collections;
import java.util.List;

public class NavigationDrawerAdapter extends RecyclerView.Adapter<NavigationDrawerAdapter.DrawerViewHolder> {
    List<NavigationDrawerItem> data = Collections.emptyList();
    private LayoutInflater inflater;
    private Context context;

    public NavigationDrawerAdapter(Context context, List<NavigationDrawerItem> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public DrawerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.nav_drawer_row, parent, false);
        DrawerViewHolder holder = new DrawerViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(DrawerViewHolder holder, int position) {
        NavigationDrawerItem current = data.get(position);
        holder.title.setText(current.getTitle());
        if (position == getItemCount() - 1) {
            holder.separator.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class DrawerViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        View separator;

        public DrawerViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            separator = itemView.findViewById(R.id.separator);
        }
    }
}
