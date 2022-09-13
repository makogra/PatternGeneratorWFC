package com.mako.patterngeneratorwfc.adapters;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.ContextMenu;
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
import com.mako.patterngeneratorwfc.wfc.Colors;

public class TileSetAdapter extends ListAdapter<TileSet, TileSetAdapter.ViewHolder> {

    //RecyclerView.Adapter<TileSetAdapter.ViewHolder>
    private static final String TAG = "TileSetAdapter";

    private final TileSetViewModel tileSetViewModel;
    private FrameLayout currentFocusedFrameLayout;
    private int height;
    private int width;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

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

            cardView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(this.getAdapterPosition(), 121, 0, "Delete");
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
        ImageView imageView = v.findViewById(R.id.cardView_tile_set_preview_imageView);
        height = imageView.getLayoutParams().height;
        width = imageView.getLayoutParams().width;
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TileSet current = getItem(position);
        createThumbnail(holder, current);
        holder.textView.setText(current.getTileId());
        if (current.getTileId().equals(tileSetViewModel.getCurrentId())){
            holder.frameLayout.setBackgroundResource(R.color.active);
            defocusPastTileSet();
            currentFocusedFrameLayout = holder.frameLayout;
        }
        holder.cardView.setOnClickListener(v -> {
            defocusPastTileSet();
            holder.frameLayout.setBackgroundResource(R.color.active);
            currentFocusedFrameLayout = holder.frameLayout;
            tileSetViewModel.setCurrentId(current.getTileId());
        });
    }

    private void createThumbnail(@NonNull ViewHolder holder, TileSet current) {
        Log.d(TAG, "createThumbnail: " + current.getTileId());
        new Thread(new Runnable() {

            @Override
            public void run() {
                Bitmap thumbnail = getThumbnail(current);
                new Handler(Looper.getMainLooper()).post(() -> holder.imageView.setImageBitmap(thumbnail));
                //holder.imageView.post(() -> holder.imageView.setImageBitmap(thumbnail));
            }

            private Bitmap getThumbnail(TileSet tileSet){
                Bitmap bitmap = Bitmap.createBitmap(tileSet.getTileSetWidth(), tileSet.getTileSetHeight(), Bitmap.Config.ARGB_8888);
                int[][] valueGrid = tileSet.getValueGrid();

                for (int i = 0; i < valueGrid.length; i++) {
                    for (int j = 0; j < valueGrid[0].length; j++) {
                        bitmap.setPixel(j, i, getColor(valueGrid[i][j], tileSet));
                    }
                }
                return Bitmap.createScaledBitmap(bitmap, width, height, false);
            }

            private int getColor(int value, TileSet tileSet){
                return Colors.getValue(tileSet.getValueToStringPath().get(value));
            }
        }).start();
    }

    private void defocusPastTileSet() {
        if (currentFocusedFrameLayout == null)
            return;
        currentFocusedFrameLayout.setBackgroundResource(R.color.white);
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
