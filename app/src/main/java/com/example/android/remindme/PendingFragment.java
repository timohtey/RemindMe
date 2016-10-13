package com.example.android.remindme;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.ArrayList;

import database.RemindMeDataSource;
import model.ToDoEntry;

import static com.example.android.remindme.MainActivity.viewPager;
import static com.example.android.remindme.MainActivity.viewPagerAdapter;

/**
 * Created by timohtey on 13/10/2016.
 *
 * This class is the Fragment for the Pending To Do Entries.
 */
public class PendingFragment extends Fragment {
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerAdapter recyclerAdapter;
    private RemindMeDataSource remindMeDataSource;
    private ArrayList<ToDoEntry> toDoEntries;

    private EditText userInputDialogEditText;

    public PendingFragment() {
        // Required empty public constructor
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_pending, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);

        // Retrieve all of the pending to do entries
        toDoEntries = remindMeDataSource.retrievePendingToDoEntries();
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

        // Handles addition and deletion of to do entries
        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater layoutInflaterAndroid = LayoutInflater.from(rootView.getContext());
                View mView = layoutInflaterAndroid.inflate(R.layout.to_do_entry_input_dialog, null);
                AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(rootView.getContext());
                alertDialogBuilderUserInput.setView(mView);
                userInputDialogEditText = (EditText) mView.findViewById(R.id.userInputDialogEditText);

                alertDialogBuilderUserInput
                        .setCancelable(false)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                // Add a To Do Entry given the name input on the Dialog Box
                                ToDoEntry toDoEntry = new ToDoEntry(userInputDialogEditText.getText().toString(), 0);
                                toDoEntry.setToDoEntryId(remindMeDataSource.getMaxId()+1);
                                remindMeDataSource.addToDoEntry(toDoEntry);

                                // Force Refresh
                                viewPager.setAdapter(viewPagerAdapter);
                                MainActivity.tabLayout.setupWithViewPager(viewPager);
                            }
                        })

                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialogBox, int id) {
                                        dialogBox.cancel();
                                    }
                                });

                AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
                alertDialogAndroid.show();
            }
        });

        return rootView;
    }

    public void setRemindMeDataSource(RemindMeDataSource remindMeDataSource){
        this.remindMeDataSource = remindMeDataSource;
    }
}
