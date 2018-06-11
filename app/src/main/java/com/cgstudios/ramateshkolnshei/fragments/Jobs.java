package com.cgstudios.ramateshkolnshei.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cgstudios.ramateshkolnshei.Entry;
import com.cgstudios.ramateshkolnshei.EntryAdapter;
import com.cgstudios.ramateshkolnshei.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class Jobs extends Fragment {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference entryReference = database.getReference().child("posts");

    private RecyclerView recyclerView;
    private List<Entry> entryList;
    private RecyclerView.Adapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private ProgressBar progressBar;
    private TextView emptyTextView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.entry_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressBar = getActivity().findViewById(R.id.loading_spinner);
        progressBar.setVisibility(View.VISIBLE);

        entryList = new ArrayList<>();

        linearLayoutManager = new LinearLayoutManager(getContext());

        recyclerView = getActivity().findViewById(R.id.recycler_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        adapter = new EntryAdapter(entryList, getContext());
        recyclerView.setAdapter(adapter);

        entryReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Entry entry = dataSnapshot.getValue(Entry.class);
                if (entry.getCategory().equals("job")) {
                    // Once data was fetched, hide progress bar
                    progressBar.setVisibility(View.GONE);
                    entryList.add(entry);
                    adapter.notifyDataSetChanged();
                }

                emptyTextView = getActivity().findViewById(R.id.empty_view);
                if (entryList.isEmpty()) {
                    emptyTextView.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
                }
                else {
                    emptyTextView.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) { }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) { }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) { }
            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });

        getActivity().setTitle("Job Opportunities");

    }
}