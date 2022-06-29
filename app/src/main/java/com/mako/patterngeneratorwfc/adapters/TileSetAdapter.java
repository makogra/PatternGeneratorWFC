package com.mako.patterngeneratorwfc.adapters;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mako.patterngeneratorwfc.R;
import com.mako.patterngeneratorwfc.datamodels.TileSetViewModel;

public class TileSetAdapter extends RecyclerView.Adapter<TileSetAdapter.ViewHolder> {

    private final TileSetViewModel tileSetViewModel;
    private ImageView currentFocusedImageView;
    private Drawable defaultImageViewBackground;

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
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_tile_set, parent, false);
        defaultImageViewBackground = v.findViewById(R.id.cardView_tile_set_preview_imageView).getBackground();
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textView.setText("" + tileSetViewModel.getTileSet(position).getIdAsTempPreview());
        if (tileSetViewModel.getCurrentIndex() == position){
            holder.imageView.setBackgroundResource(R.drawable.card_view_tile_set_image_view);
            currentFocusedImageView = holder.imageView;
        }
        holder.imageView.setOnClickListener(v -> {
            currentFocusedImageView.setBackground(defaultImageViewBackground);
            holder.imageView.setBackgroundResource(R.drawable.card_view_tile_set_image_view);
            currentFocusedImageView = holder.imageView;
            tileSetViewModel.setCurrentIndex(position);
            System.out.println("id: " + tileSetViewModel.getTileSet(position).getTileId());
        });
    }

    @Override
    public int getItemCount() {
        return tileSetViewModel.getTileSetListSize();
    }

}
