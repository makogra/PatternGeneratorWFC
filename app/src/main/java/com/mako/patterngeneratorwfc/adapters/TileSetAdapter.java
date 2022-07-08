package com.mako.patterngeneratorwfc.adapters;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.mako.patterngeneratorwfc.R;
import com.mako.patterngeneratorwfc.TileSet;
import com.mako.patterngeneratorwfc.datamodels.TileSetViewModel;

public class TileSetAdapter extends ListAdapter<TileSet, TileSetAdapter.ViewHolder> {

    //RecyclerView.Adapter<TileSetAdapter.ViewHolder>
    private static final String TAG = "TileSetAdapter";

    private final TileSetViewModel tileSetViewModel;
    private ImageView currentFocusedImageView;
    private Drawable defaultImageViewBackground;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        final TextView textView;
        final ImageView imageView;
        final CardView cardView;
        final FrameLayout frameLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.cardView_tile_set_idAsTempPreview_textView);
            imageView = itemView.findViewById(R.id.cardView_tile_set_preview_imageView);
            cardView = itemView.findViewById(R.id.card_view_tile_set_card_view);
            frameLayout = itemView.findViewById(R.id.card_view_tile_set_frame_layout);
        }
    }

    public TileSetAdapter(@NonNull DiffUtil.ItemCallback<TileSet> diffCallback, TileSetViewModel tileSetViewModel) {
        super(diffCallback);
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
        TileSet current = getItem(position);
        holder.textView.setText(current.getTileId());
        if (current.getTileId().equals(tileSetViewModel.getCurrentId())){
            holder.imageView.setBackgroundResource(R.drawable.card_view_tile_set_image_view);
            currentFocusedImageView = holder.imageView;
        }
        holder.cardView.setOnClickListener(v -> {
            holder.frameLayout.setBackgroundResource(R.color.black);
        });
        holder.imageView.setOnClickListener(v -> {
            if (currentFocusedImageView != null)
                currentFocusedImageView.setBackground(defaultImageViewBackground);
            holder.imageView.setBackgroundResource(R.drawable.card_view_tile_set_image_view);
            currentFocusedImageView = holder.imageView;
            tileSetViewModel.setCurrentId(current.getTileId());
        });
    }

    public static class TileSetDiff extends DiffUtil.ItemCallback<TileSet> {

        @Override
        public boolean areItemsTheSame(@NonNull TileSet oldItem, @NonNull TileSet newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull TileSet oldItem, @NonNull TileSet newItem) {
            return oldItem.getTileId().equals(newItem.getTileId());
        }
    }

}
