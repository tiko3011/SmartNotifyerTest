package com.example.smartnotifyer.ui.stats;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartnotifyer.R;
import com.example.smartnotifyer.database.Stat;
import com.example.smartnotifyer.mvvm.StatsViewModel;
import com.example.smartnotifyer.ui.UsageConverter;

import java.util.ArrayList;
import java.util.List;

public class StatsFragment extends Fragment {

    private final long hour = 60 * 60 * 1000;
    private long end = System.currentTimeMillis();
    private long start = end - hour;

    private StatAdapter statAdapter;
    private StatsViewModel statsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_stats, container, false);

        RecyclerView recyclerView = root.findViewById(R.id.stat_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        statAdapter = new StatAdapter();
        recyclerView.setAdapter(statAdapter);

        statsViewModel = new ViewModelProvider(requireActivity()).get(StatsViewModel.class);
        statsViewModel.getStats().observe(getViewLifecycleOwner(), stats -> {
            statAdapter.setStatsList(stats);
        });

        statsViewModel.addStatSFromSystem(start, end);
        TextView tv = root.findViewById(R.id.tv_interval);
        SeekBar bar = root.findViewById(R.id.bar_set_interval);
        bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tv.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                statsViewModel.deleteAllStats();
                start = end - hour * bar.getProgress();
                statsViewModel.addStatSFromSystem(start, end);
            }
        });
        return root;
    }

    private class StatAdapter extends RecyclerView.Adapter<StatAdapter.StatCardHolder> {
        List<Stat> statsList;

        public void setStatsList(List<Stat> statsList) {
            this.statsList = new ArrayList<>(statsList);
            notifyDataSetChanged();
        }
        public void setFilteredList(List<Stat> filteredList){
            this.statsList = filteredList;
            notifyDataSetChanged();
        }
        public StatCardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);
            return new StatCardHolder(inflater.inflate(R.layout.view_stat_list_item, parent, false));
        }

        @Override
        public void onBindViewHolder (@NonNull StatCardHolder holder, int position) {
            String name = statsList.get(position).statName;
            String time = statsList.get(position).statTime;
            name = UsageConverter.changePackageName(name);
            time = UsageConverter.convertMinuteToString(Long.parseLong(time));
            holder.nameText.setText(name);
            holder.timeText.setText(time);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }


        @Override
        public int getItemCount() {
            return statsList != null ? statsList.size() : 0;
        }

        public class StatCardHolder extends RecyclerView.ViewHolder {
            TextView nameText;
            TextView timeText;

            public StatCardHolder(View view) {
                super(view);
                nameText = view.findViewById(R.id.item_stat_name_tv);
                timeText = view.findViewById(R.id.item_stat_time_tv);

            }
        }
    }
}