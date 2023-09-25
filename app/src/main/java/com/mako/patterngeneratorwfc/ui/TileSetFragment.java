package com.mako.patterngeneratorwfc.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mako.patterngeneratorwfc.R;
import com.mako.patterngeneratorwfc.TileSet;
import com.mako.patterngeneratorwfc.activities.AddTileSetActivity;
import com.mako.patterngeneratorwfc.adapters.TileSetAdapter;
import com.mako.patterngeneratorwfc.database.TileSetRepository;
import com.mako.patterngeneratorwfc.datamodels.TileSetViewModel;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class TileSetFragment extends Fragment {

    private static final String TAG = "TileSetFragment";
    private static final int SPAN_COUNT = 2;
    private static ViewModelProvider sViewModelProvider;
    private TileSetViewModel tileSetViewModel;
    private TileSetAdapter adapter;


    public static TileSetFragment newInstance(ViewModelProvider viewModelProvider) {
        TileSetFragment.sViewModelProvider = viewModelProvider;
        return new TileSetFragment();
    }

    ActivityResultLauncher<Intent> mGetContent = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK){
            Intent intent = result.getData();
            if (intent == null){
                Log.wtf(TAG, "insert is null, something isn't working in write section maybe");
                return;
            }
            TileSet tileSetFromResult = intent.getParcelableExtra("TileSet");
            String oldID = intent.getStringExtra(AddTileSetActivity.PUT_EXTRA_OLD_TILE_ID);
            if (null != oldID && !oldID.equals(tileSetFromResult.getTileId())) {
                tileSetViewModel.delete(oldID);
            }
            tileSetViewModel.insert(tileSetFromResult);
            tileSetViewModel.setCurrentId(tileSetFromResult.getTileId());
        }
    });

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tile_set, container, false);
        FloatingActionButton fabAddTileSet = view.findViewById(R.id.fragment_tile_set_add_new_tile_set_fab);
        fabAddTileSet.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), AddTileSetActivity.class);
            mGetContent.launch(intent);

        });
        RecyclerView recyclerView = view.findViewById(R.id.fragment_tile_set_recycler_view);
        adapter = new TileSetAdapter(new TileSetAdapter.TileSetDiff(), tileSetViewModel);
        tileSetViewModel.getTileSetList().observe(getViewLifecycleOwner(), adapter::submitList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), SPAN_COUNT));
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tileSetViewModel = new ViewModelProvider(requireActivity()).get(TileSetViewModel.class);
        new Thread(() -> {
            tileSetViewModel.initCurrentId();
        }).start();
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        TileSet tileSet = adapter.getCurrentList().get(item.getGroupId());
        String tileId = tileSet.getTileId();

        switch (item.getItemId()){
            case 120:
                edit(tileSet);
                break;
            case 121:
                delete(item, tileId);
                break;
            default:
                Log.d(TAG, "onContextItemSelected: nwm co się stało że ma inny item id (" + item.getItemId() + "): " + item);
                return super.onContextItemSelected(item);
        }


        return super.onContextItemSelected(item);
    }

    private void delete(MenuItem item, String tileId) {
        Log.d(TAG, "onContextItemSelected() called with: item = [" + item + " " + item.getGroupId() + " " + tileId + "]");
        TileSetRepository repo = TileSetRepository.getInstance(requireActivity().getApplication());
        repo.deleteId(tileId);
        //TODO choose current id
        if (tileSetViewModel.getCurrentId().equals(tileId)){
            Log.d(TAG, "onContextItemSelected: ");
            chooseCurrentId(repo);
        }
        adapter.notifyItemRemoved(item.getGroupId());
    }

    private void edit(TileSet tileSet) {
        Intent intent = new Intent(getContext(), AddTileSetActivity.class);
        intent.putExtra(AddTileSetActivity.PUT_EXTRA_TILE_SET, tileSet);
        mGetContent.launch(intent);
    }

    private void chooseCurrentId(TileSetRepository tileSetRepository){
        AtomicBoolean flag = new AtomicBoolean(false);
        //TODO check if flag can be deleted

        tileSetRepository.getTileSetList().observe(this, tileSets -> {
            /*if (flag.get())
                return;*/
            if (tileSets.isEmpty()){
                Log.d(TAG, "chooseCurrentId: tileSetList is empty (in DB)");
                TileSet tileSet = new TileSet("new Tile set", new int[][]{{1,2,3},{2,3,4},{3,4,4}}, new ArrayList<String>(){{
                    add("_");
                    add("G");
                    add("C");
                    add("S");
                    add("M");
                }});
                tileSetViewModel.insert(tileSet);
                tileSetViewModel.setCurrentId(tileSet.getTileId());
            } else {

                tileSetViewModel.setCurrentId(tileSets.get(0).getTileId());
            }
            flag.set(true);
            Log.d(TAG, "chooseCurrentId: chosen id: " + tileSetViewModel.getCurrentId());
            adapter.notifyItemChanged(0);
        });
    }
}