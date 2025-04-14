package com.example.tennispostureanalysis;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import java.util.List;

public class SessionAdapter extends RecyclerView.Adapter<SessionAdapter.SessionViewHolder> {

    private List<Session> sessions;

    public SessionAdapter(List<Session> sessions) {
        this.sessions = sessions;
    }

    public static class SessionViewHolder extends RecyclerView.ViewHolder {
        ImageView thumbnail;
        TextView title;

        public SessionViewHolder(View view) {
            super(view);
            thumbnail = view.findViewById(R.id.thumbnail);
            title = view.findViewById(R.id.sessionTitle);
        }
    }

    @Override
    public SessionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_session, parent, false);
        return new SessionViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SessionViewHolder holder, int position) {
        Session session = sessions.get(position);
        holder.title.setText(session.getTitle());
        Glide.with(holder.thumbnail.getContext()).load(session.getThumbnailPath()).into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return sessions.size();
    }
}

