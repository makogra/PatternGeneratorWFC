package com.mako.patterngeneratorwfc.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mako.patterngeneratorwfc.R;
import com.mako.patterngeneratorwfc.TileSet;
import com.mako.patterngeneratorwfc.datamodels.TileSetViewModel;

import java.util.List;

public class TileSetAdapter extends RecyclerView.Adapter<TileSetAdapter.ViewHolder> {

    private final TileSetViewModel tileSetViewModel;
    private List<TileSet> tileSetList;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        final TextView textView;
        final ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.cardView_tile_set_idAsTempPreview_textView);
            imageView = itemView.findViewById(R.id.cardView_tile_set_preview_imageView);
        }
    }

    public TileSetAdapter(TileSetViewModel tileSetViewModel) {
        this.tileSetViewModel = tileSetViewModel;
        this.tileSetList = tileSetViewModel.getTileSetList();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_tile_set, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textView.setText("" + tileSetList.get(position).getIdAsTempPreview());
        holder.imageView.setFocusable(true);
        holder.imageView.setFocusableInTouchMode(true);
        holder.imageView.setBackgroundResource(R.drawable.card_view_tile_set_image_view);
        holder.imageView.setOnClickListener(v -> {
            //v.setActivated(true);
            v.requestFocus();
            tileSetViewModel.setCurrentIndex(position);
            System.out.println("id: " + tileSetList.get(position).getId());
        });
    }

    @Override
    public int getItemCount() {
        return tileSetList.size();
    }


}
