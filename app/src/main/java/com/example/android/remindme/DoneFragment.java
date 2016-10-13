package com.example.android.remindme;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import database.RemindMeDataSource;
import model.ToDoEntry;

import static com.example.android.remindme.MainActivity.viewPager;
import static com.example.android.remindme.MainActivity.viewPagerAdapter;

/**
 * Created by timohtey on 13/10/2016.
 *
 * This class is for the Fragment for the Done To Do Entries.
 */
public class DoneFragment extends Fragment {
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerAdapter recyclerAdapter;
    private RemindMeDataSource remindMeDataSource;
    private ArrayList<ToDoEntry> toDoEntries;

    public DoneFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_pending, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);

        // Retrieve all of the Done To Do Entries
        toDoEntries = remindMeDataSource.retrieveDoneToDoEntries();

        recyclerAdapter = new RecyclerAdapter(toDoEntries);
        recyclerView.setAdapter(recyclerAdapter);

        linearLayoutManager = new LinearLayoutManager(rootView.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        // If item is clicked, the state will be changed to pending or done
        recyclerView.addOnItemTouchListener(
                new RecyclerItemOnClickListener(rootView.getContext(), new RecyclerItemOnClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        remindMeDataSource.switchToDoEntryState(toDoEntries.get(position));

                        // Force Refresh
                        viewPager.setAdapter(viewPagerAdapter);
                        MainActivity.tabLayout.setupWithViewPager(viewPager);
                    }
                })
        );

        // Hide the Floating Action bar
        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setVisibility(View.GONE);

        return rootView;
    }

    public void setRemindMeDataSource(RemindMeDataSource remindMeDataSource){
        this.remindMeDataSource = remindMeDataSource;
    }
}
