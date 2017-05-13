package com.muusa.sweetaroma.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.franmontiel.fullscreendialog.FullScreenDialogContent;
import com.franmontiel.fullscreendialog.FullScreenDialogController;
import com.muusa.sweetaroma.NearByPlace;
import com.muusa.sweetaroma.R;
import com.muusa.sweetaroma.adapter.NearByPlacesAdapter;
import com.muusa.sweetaroma.utils.AppConfig;

import java.util.ArrayList;

/**
 * Created by Muusa on 5/13/2017.
 */

public class PlacesListFragment extends Fragment implements FullScreenDialogContent, NearByPlacesAdapter.ClickListener {

    private FullScreenDialogController dialogController;
    private ArrayList<NearByPlace> nearByPlaceArrayList = new ArrayList<>();
    RecyclerView nearbyPlacesRecyclerView;

    public PlacesListFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view=inflater.inflate(R.layout.fragment_places_list, container, false);

        nearbyPlacesRecyclerView= (RecyclerView) view.findViewById(R.id.nearbyPlacesRecyclerView);
        nearbyPlacesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        nearbyPlacesRecyclerView.setItemAnimator(new DefaultItemAnimator());
        nearbyPlacesRecyclerView.addOnItemTouchListener(new NearByPlacesAdapter.RecyclerTouchListener(getActivity(), nearbyPlacesRecyclerView, this));

        return view;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        nearByPlaceArrayList=getArguments().getParcelableArrayList(AppConfig.MAP_LIST_ARRAYLIST);
        nearbyPlacesRecyclerView.setAdapter(new NearByPlacesAdapter(nearByPlaceArrayList, getActivity()));
    }

    @Override
    public void onDialogCreated(FullScreenDialogController dialogController) {
        this.dialogController = dialogController;
    }

    @Override
    public boolean onConfirmClick(FullScreenDialogController dialogController) {
        return false;
    }

    @Override
    public boolean onDiscardClick(FullScreenDialogController dialogController) {
        return false;
    }

    @Override
    public void onClick(View view, int position) {
        Toast.makeText(getActivity(), "You've Clicked "+nearByPlaceArrayList.get(position).getPlaceName(),Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLongClick(View view, int position) {
        Toast.makeText(getActivity(),"Ready to roll",Toast.LENGTH_SHORT).show();
    }
}
