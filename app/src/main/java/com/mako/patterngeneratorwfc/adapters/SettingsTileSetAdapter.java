package com.mako.patterngeneratorwfc.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mako.patterngeneratorwfc.R;
import com.mako.patterngeneratorwfc.datamodels.SettingsTileSetViewModel;

public class SettingsTileSetAdapter extends RecyclerView.Adapter<SettingsTileSetAdapter.ViewHolder> {

    protected final SettingsTileSetViewModel settingsTileSetViewModel;
    private final int ITEM_COUNT = SettingsTileSetViewModel.getSettingsLength();

    public static class ViewHolder extends RecyclerView.ViewHolder {
        Button decrementButton;
        Button incrementButton;
        TextView name;
        TextView value;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.decrementButton = itemView.findViewById(R.id.card_view_settings_tile_set_decrement_btn);
            this.incrementButton = itemView.findViewById(R.id.card_view_settings_tile_set_increment_btn);
            this.name = itemView.findViewById(R.id.card_view_settings_tile_set_name);
            this.value = itemView.findViewById(R.id.card_view_settings_tile_set_value);
        }


    }

    public SettingsTileSetAdapter(SettingsTileSetViewModel settingsTileSetViewModel){
        this.settingsTileSetViewModel = settingsTileSetViewModel;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_settings_tile_set, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(settingsTileSetViewModel.getHumanName(position));
        holder.value.setText("" + settingsTileSetViewModel.getValue(position));
        holder.incrementButton.setOnClickListener(v -> {
            try {
                settingsTileSetViewModel.increment(position);
                holder.value.setText("" + settingsTileSetViewModel.getValue(position));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });
        holder.decrementButton.setOnClickListener(v -> {
            try {
                settingsTileSetViewModel.decrement(position);
                holder.value.setText("" + settingsTileSetViewModel.getValue(position));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });

    }



    @Override
    public int getItemCount() {
        return ITEM_COUNT;
    }
}
