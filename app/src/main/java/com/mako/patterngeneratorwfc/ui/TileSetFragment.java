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

import java.util.concurrent.atomic.AtomicBoolean;

public class TileSetFragment extends Fragment {

    private static final String TAG = "TileSetFragment";
    private static final int SPAN_COUNT = 2;
    private static ViewModelProvider sViewModelProvider;
    private ViewModelProvider mViewModelProvider;
    private TileSetViewModel mTileSetViewModel;
    private TileSetAdapter adapter;


    public static TileSetFragment newInstance(ViewModelProvider viewModelProvider) {
        TileSetFragment.sViewModelProvider = viewModelProvider;
        return new TileSetFragment();
    }

    ActivityResultLauncher<Intent> mGetContent = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK){
            Intent intent = result.getData();
            if (intent == null){
                Log.w(TAG, "insert is null, something isn't working in write section maybe");
                return;
            }
            TileSet tileSetFromResult = intent.getParcelableExtra("TileSet");
            mTileSetViewModel.insert(tileSetFromResult);
            mTileSetViewModel.setCurrentId(tileSetFromResult.getTileId());
            Log.d(TAG, "result is working correctly " + tileSetFromResult);
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
        adapter = new TileSetAdapter(new TileSetAdapter.TileSetDiff(), mTileSetViewModel);
        mTileSetViewModel.getTileSetList().observe(getViewLifecycleOwner(), adapter::submitList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), SPAN_COUNT));
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mViewModelProvider = new ViewModelProvider(requireActivity());
        mTileSetViewModel = mViewModelProvider.get(TileSetViewModel.class);
        new Thread(() -> {
            mTileSetViewModel.initCurrentId();
            Log.d(TAG, "Ascync init currentId compleate");
        }).start();

    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() != 121){
            Log.d(TAG, "onContextItemSelected: nwm co si?? sta??o ??e ma inny item id: " + item);
            return super.onContextItemSelected(item);
        }
        String tileId = adapter.getCurrentList().get(item.getGroupId()).getTileId();
        Log.d(TAG, "onContextItemSelected() called with: item = [" + item + " " + item.getGroupId() + " " + tileId + "]");
        TileSetRepository repo = TileSetRepository.getInstance(requireActivity().getApplication());
        repo.deleteId(tileId);
        //TODO choose current id
        if (mTileSetViewModel.getCurrentId().equals(tileId)){
            Log.d(TAG, "onContextItemSelected: ");
            chooseCurrentId(repo);
        }
        adapter.notifyItemRemoved(item.getGroupId());
        return super.onContextItemSelected(item);
    }

    private void chooseCurrentId(TileSetRepository tileSetRepository){
        AtomicBoolean flag = new AtomicBoolean(false);

        tileSetRepository.getTileSetList().observe(this, tileSets -> {
            if (flag.get())
                return;
            if (tileSets.isEmpty()){
                //TODO add sample TileSet
                Log.d(TAG, "chooseCurrentId: tileSetList is empty (in DB)");
            }
            flag.set(true);
            mTileSetViewModel.setCurrentId(tileSets.get(0).getTileId());
            Log.d(TAG, "chooseCurrentId: choosed id: " + mTileSetViewModel.getCurrentId());
            adapter.notifyItemChanged(0);
        });
    }
}