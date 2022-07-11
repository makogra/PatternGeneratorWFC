package com.mako.patterngeneratorwfc.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.os.AsyncTask;
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
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mako.patterngeneratorwfc.R;
import com.mako.patterngeneratorwfc.TileSet;
import com.mako.patterngeneratorwfc.activities.AddTileSetActivity;
import com.mako.patterngeneratorwfc.database.TileSetRepository;
import com.mako.patterngeneratorwfc.datamodels.TileSetViewModel;
import com.mako.patterngeneratorwfc.adapters.TileSetAdapter;

public class TileSetFragment extends Fragment {

    private static final String TAG = "TileSetFragment";
    private static final int SPAN_COUNT = 2;
    private TileSetViewModel mTileSetViewModel;
    private TileSetAdapter adapter;


    public static TileSetFragment newInstance() {
        return new TileSetFragment();
    }

    ActivityResultLauncher<Intent> mGetContent = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK){
            Intent intent = result.getData();
            if (intent == null){
                Log.d(TAG, "insert is null, something isn't working in write section maybe");
                return;
            }
            TileSet tileSetFromResult = intent.getParcelableExtra("TileSet");
            mTileSetViewModel.insert(tileSetFromResult);
            mTileSetViewModel.setCurrentId(tileSetFromResult.getTileId());
            Log.d(TAG, tileSetFromResult.toString());
            Log.d(TAG, "result is working correctly");
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
        /*adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                // TODO add adapter.notifyItemInserted();
            }
        });*/
        mTileSetViewModel.getTileSetList().observe(getViewLifecycleOwner(), adapter::submitList);
        //TODO comment this
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                Log.d(TAG, "TileSetFragment.onChanged");
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), SPAN_COUNT));
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // TODO Implement responsive grid layout. Or sth like that :).
        //ArrayList<View> cardViewList = createTileSetCardViewList(viewGroup);
        //getLayoutInflater()
        //view.findViewById(R.id.fragment_tile_set_sliding_panel_layout).addChildrenForAccessibility(cardViewList);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "TileSetFragment.onCreate");
        mTileSetViewModel = new ViewModelProvider(requireActivity()).get(TileSetViewModel.class);
        AsyncTask.execute(() -> {
            mTileSetViewModel.initCurrentId();
            Log.d(TAG, "Ascync init currentId compleate");
        });
        //mTileSetViewModel.initCurrentId();
        Log.d(TAG, "TileSetFragment.onCreate - koniec");

    }

    // TODO Implement responsive grid layout. 1st try.
    /*
    private ArrayList<View> createTileSetCardViewList(ViewGroup viewGroup){
        ArrayList<View> viewList = new ArrayList<>();
        List<TileSet> tileSetList = mViewModel.getTileSetList();
        View cardView;
        TextView textView;
        LayoutInflater inflater = getLayoutInflater();// (LayoutInflater) requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for (TileSet tileSet : tileSetList){
            cardView = inflater.inflate(R.layout.cardview_tile_set, viewGroup);
            textView = cardView.findViewById(R.id.cardView_tile_set_idAsTempPreview_textView);
            textView.setText("" + tileSet.getIdAsTempPreview());
            viewList.add(cardView);
        }

        return viewList;
    }

     */

    @Override
    public void onResume() {
        super.onResume();
        //mTileSetViewModel.getTileSetList().observe(getViewLifecycleOwner(), adapter::submitList);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        String tileId = adapter.getCurrentList().get(item.getGroupId()).getTileId();
        Log.d(TAG, "onContextItemSelected() called with: item = [" + item + " " + item.getGroupId() + " " + tileId + "]");
        TileSetRepository repo = new TileSetRepository(requireActivity().getApplication());
        repo.deleteId(tileId);
        return super.onContextItemSelected(item);
    }
}