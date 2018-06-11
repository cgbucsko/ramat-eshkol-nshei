package com.cgstudios.ramateshkolnshei.fragments;

import android.content.Intent;
import android.net.Uri;
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

public class Gemach extends Fragment {

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
        return inflater.inflate(R.layout.gemach_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView gemachTextView = getActivity().findViewById(R.id.gemach_text);
        gemachTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://docs.google.com/document/d/1JTHB7_4sjME15MZgtxd7qjVTRYXu_N8BpYR2fYs9cB0/edit";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

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
                if (entry.getCategory().equals("gemach")) {
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

        getActivity().setTitle("Gemach List");

    }
}