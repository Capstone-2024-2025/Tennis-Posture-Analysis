package com.example.tennispostureanalysis;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class DrillAdapter extends RecyclerView.Adapter<DrillAdapter.DrillViewHolder> {

    private List<Drill> drills;
    private Context context;

    public DrillAdapter(Context context, List<Drill> drills) {
        this.context = context;
        this.drills = drills;
    }

    public static class DrillViewHolder extends RecyclerView.ViewHolder {
        ImageView thumbnail;
        TextView title;

        public DrillViewHolder(View view) {
            super(view);
            thumbnail = view.findViewById(R.id.drillThumbnail);
            title = view.findViewById(R.id.drillTitle);
        }
    }

    @Override
    public DrillViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_drill, parent, false);
        return new DrillViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DrillViewHolder holder, int position) {
        Drill drill = drills.get(position);
        holder.title.setText(drill.getTitle());
        Glide.with(context).load(drill.getThumbnailUrl()).into(holder.thumbnail);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(drill.getYoutubeUrl()));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return drills.size();
    }
}
