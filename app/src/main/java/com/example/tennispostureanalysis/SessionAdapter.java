package com.example.tennispostureanalysis;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SessionAdapter extends RecyclerView.Adapter<SessionAdapter.SessionViewHolder> {

    private List<Session> sessions;
    private boolean useSmallLayout; // true for FeedbackPage, false for View All
    private OnSessionClickListener clickListener;

    public interface OnSessionClickListener {
        void onSessionClick(Session session);
    }

    // Updated constructor with click listener
    public SessionAdapter(List<Session> sessions, boolean useSmallLayout, OnSessionClickListener clickListener) {
        this.sessions = sessions;
        this.useSmallLayout = useSmallLayout;
        this.clickListener = clickListener;
    }

    public static class SessionViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView time;

        public SessionViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.sessionTitle);
            time = view.findViewById(R.id.sessionTime);
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

        String sessionTitle = "Session " + (position + 1);
        holder.title.setText(sessionTitle);

        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy • h:mm a", Locale.getDefault());
        String formattedDate = sdf.format(new Date(session.getTimestamp()));
        holder.time.setText(formattedDate);

        // Make each item clickable
        holder.itemView.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.onSessionClick(session);
            }
        });
    }

    @Override
    public int getItemCount() {
        return sessions.size();
    }
}

