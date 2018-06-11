package com.cgstudios.ramateshkolnshei;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;
import java.util.regex.Pattern;

public class EntryAdapter extends RecyclerView.Adapter<EntryAdapter.EntryViewHolder> {

    private List<Entry> entryList;
    private Context context;

    public EntryAdapter(List<Entry> entryList, Context context) {
        this.entryList = entryList;
        this.context = context;
    }

    @NonNull
    @Override
    public EntryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_list_item, parent, false);

        return new EntryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EntryViewHolder holder, int position) {
        Entry entry = entryList.get(position);

        showTextView(holder.titleTextView, entry.getTitle(), null);
        showTextView(holder.messageTextView, entry.getMessage(), null);
        showTextView(holder.phoneTextView, entry.getPhone(), holder.hasPhone);
        showTextView(holder.emailTextView, entry.getEmail(), holder.hasEmail);
        showTextView(holder.websiteTextView, entry.getWebsite(), holder.hasWebsite);
    }

    @Override
    public int getItemCount() {
        return entryList.size();
    }

    class EntryViewHolder extends RecyclerView.ViewHolder {

        TextView titleTextView;
        TextView messageTextView;
        TextView phoneTextView;
        TextView emailTextView;
        TextView websiteTextView;

        LinearLayout hasPhone;
        LinearLayout hasEmail;
        LinearLayout hasWebsite;

        public EntryViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.list_title);
            messageTextView = itemView.findViewById(R.id.list_message);
            phoneTextView = itemView.findViewById(R.id.list_phone);
            emailTextView = itemView.findViewById(R.id.list_email);
            websiteTextView = itemView.findViewById(R.id.list_website);

            hasPhone = itemView.findViewById(R.id.list_has_phone);
            hasEmail = itemView.findViewById(R.id.list_has_email);
            hasWebsite = itemView.findViewById(R.id.list_has_website);
        }
    }

    private void showTextView(TextView textView, String text, LinearLayout linearLayout) {
        textView.setText(text);
        Log.v("Is Empty Check", "Text View: " + textView.toString() + ", Text: " + text);
        if (text == null || text.isEmpty()) {

            if (linearLayout != null) {
                linearLayout.setVisibility(View.GONE);
            }
            else {
                textView.setVisibility(View.GONE);
            }
        }
        else {

            if (linearLayout != null) {
                linearLayout.setVisibility(View.VISIBLE);
            }
            else {
                textView.setVisibility(View.VISIBLE);
            }
        }
    }
}
