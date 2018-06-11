package com.cgstudios.ramateshkolnshei.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;

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

public class Rentals extends Fragment {

    private RecyclerView recyclerView;
    private List<Entry> entryList;
    private RecyclerView.Adapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private int spinnerPosition;
    private RelativeLayout relativeLayout;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference entryReference = database.getReference().child("posts");

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.rentals_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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

        Spinner spinner = getActivity().findViewById(R.id.rental_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> rentalAdapter = ArrayAdapter.createFromResource(getContext(),R.array.rentals_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        rentalAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(rentalAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerPosition = position;
                entryList.clear();

                entryReference.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Entry entry = dataSnapshot.getValue(Entry.class);

                        switch (spinnerPosition) {
                            case 0:
                                if (entry.getCategory().equals("longavailable")) {
                                    entryList.add(entry);
                                    adapter.notifyDataSetChanged();
                                }
                                break;
                            case 1:
                                if (entry.getCategory().equals("vacation")) {
                                    entryList.add(entry);
                                    adapter.notifyDataSetChanged();
                                }
                                break;
                            case 2:
                                if (entry.getCategory().equals("shortavailable")) {
                                    entryList.add(entry);
                                    adapter.notifyDataSetChanged();
                                }
                                break;
                            case 3:
                                if (entry.getCategory().equals("realestate")) {
                                    entryList.clear();
                                    entryList.add(entry);
                                    adapter.notifyDataSetChanged();
                                }
                                break;
                            case 4:
                                if (entry.getCategory().equals("seekinglong")) {
                                    entryList.add(entry);
                                    adapter.notifyDataSetChanged();
                                }
                                break;
                            default:
                                if (entry.getCategory().equals("seekingshort")) {
                                    entryList.add(entry);
                                    adapter.notifyDataSetChanged();
                                }
                                break;


                        }
                        if (entryList.isEmpty()) {
                            relativeLayout = getActivity().findViewById(R.id.empty_list_spinner);
                            relativeLayout.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        }
                        else {
                            relativeLayout = getActivity().findViewById(R.id.empty_list_spinner);
                            relativeLayout.setVisibility(View.GONE);
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
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        getActivity().setTitle("Rentals/Real Estate");
    }
}