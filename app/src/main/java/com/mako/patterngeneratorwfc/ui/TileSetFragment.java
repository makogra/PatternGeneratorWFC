package com.mako.patterngeneratorwfc.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mako.patterngeneratorwfc.R;
import com.mako.patterngeneratorwfc.TileSet;
import com.mako.patterngeneratorwfc.TileSetViewModel;
import com.mako.patterngeneratorwfc.adapters.TileSetAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TileSetFragment extends Fragment {

    private static final int SPAN_COUNT = 2;
    private TileSetViewModel mViewModel;



    public static TileSetFragment newInstance() {
        return new TileSetFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tile_set, container, false);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = view.findViewById(R.id.fragment_tile_set_recycler_view);
        TileSetAdapter adapter = new TileSetAdapter(mViewModel.getTileSetList());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), SPAN_COUNT));
        // TODO Implement responsive grid layout. Or sth like that :).
        //ArrayList<View> cardViewList = createTileSetCardViewList(viewGroup);
        //getLayoutInflater()
        //view.findViewById(R.id.fragment_tile_set_sliding_panel_layout).addChildrenForAccessibility(cardViewList);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(TileSetViewModel.class);
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
}