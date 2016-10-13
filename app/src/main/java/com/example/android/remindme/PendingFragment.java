package com.example.android.remindme;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import database.RemindMeDataSource;
import model.ToDoEntry;

import java.util.ArrayList;

/**
 * Created by timohtey on 13/10/2016.
 */
public class PendingFragment extends Fragment {
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerAdapter recyclerAdapter;
    private RemindMeDataSource remindMeDataSource;

    public PendingFragment() {
        // Required empty public constructor
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_pending, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);

        ArrayList<ToDoEntry> toDoEntries = remindMeDataSource.retrievePendingToDoEntries();

        recyclerAdapter = new RecyclerAdapter(toDoEntries);
        recyclerView.setAdapter(recyclerAdapter);

        linearLayoutManager = new LinearLayoutManager(rootView.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        return rootView;
    }

    public void setRemindMeDataSource(RemindMeDataSource remindMeDataSource){
        this.remindMeDataSource = remindMeDataSource;
    }
}
