package com.example.tennispostureanalysis;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SessionAdapter extends RecyclerView.Adapter<SessionAdapter.SessionViewHolder> {

    private List<Session> sessions;
    private boolean useSmallLayout; // true for FeedbackPage, false for View All

    public SessionAdapter(List<Session> sessions, boolean useSmallLayout) {
        this.sessions = sessions;
        this.useSmallLayout = useSmallLayout;
    }

    public static class SessionViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView time;
        ImageView thumbnail;

        public SessionViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.sessionTitle);
            time = view.findViewById(R.id.sessionTime);
            thumbnail = view.findViewById(R.id.thumbnail); // hook to your layout
        }
    }


    @Override
    public SessionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layout = useSmallLayout ? R.layout.item_session_small : R.layout.item_session_large;
        View itemView = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new SessionViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SessionViewHolder holder, int position) {
        Session session = sessions.get(position);

        // Title and timestamp
        holder.title.setText("Session " + (position + 1));
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy • h:mm a", Locale.getDefault());
        holder.time.setText(sdf.format(new Date(session.getTimestamp())));

        // Load thumbnail from video URI
        Glide.with(holder.itemView.getContext())
                .load(Uri.parse(session.getVideoUri()))
                .thumbnail(0.1f) // Optional: shows a low-res preview while loading
                .into(holder.thumbnail);
    }


    @Override
    public int getItemCount() {
        return sessions.size();
    }
}

