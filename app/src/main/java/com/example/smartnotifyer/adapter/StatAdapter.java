package com.example.smartnotifyer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartnotifyer.R;
import com.example.smartnotifyer.model.Stat;

import org.w3c.dom.Text;

import java.util.List;

public class StatAdapter extends RecyclerView.Adapter<StatAdapter.StatViewHolder> {

    Context context;
    List<Stat> stats;

    public StatAdapter(Context context, List<Stat> stats) {
        this.context = context;
        this.stats = stats;
    }

    @NonNull
    @Override
    public StatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View statItems = LayoutInflater.from(context).inflate(R.layout.item_usage, parent, false);
        return new StatAdapter.StatViewHolder(statItems);
    }

    @Override
    public void onBindViewHolder(@NonNull StatViewHolder holder, int position) {
        holder.packageName.setText(stats.get(position).getPackageName());
        holder.timeUsed.setText(stats.get(position).getTimeUsed());
        holder.statIcon.setImageDrawable(stats.get(position).getPackageIcon());
    }


    @Override
    public int getItemCount() {
        return stats.size();
    }

    public static final class StatViewHolder extends RecyclerView.ViewHolder{

        TextView packageName;
        TextView timeUsed;
        ImageView statIcon;

        public StatViewHolder(@NonNull View itemView) {
            super(itemView);

            packageName = itemView.findViewById(R.id.item_tv_UsageStat_name);
            timeUsed = itemView.findViewById(R.id.item_tv_UsageStat_time);
            statIcon = itemView.findViewById(R.id.item_iv_UsageStat_icon);
        }
    }
}
