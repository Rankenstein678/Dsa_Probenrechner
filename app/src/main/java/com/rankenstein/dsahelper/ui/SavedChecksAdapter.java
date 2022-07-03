package com.rankenstein.dsahelper.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.rankenstein.dsahelper.R;
import com.rankenstein.dsahelper.logic.Check;
import com.rankenstein.dsahelper.logic.CheckHelper;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class SavedChecksAdapter extends RecyclerView.Adapter<SavedChecksAdapter.ViewHolder> {
    private final ArrayList<Check> localCheckList;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView txtName, txtStats, txtChance,txtTaw,txtMod;

        private final ImageButton btnDelete;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            txtName = itemView.findViewById(R.id.txtName);
            txtStats = itemView.findViewById(R.id.txtStats);
            txtChance = itemView.findViewById(R.id.txtChance);
            txtTaw = itemView.findViewById(R.id.txtTaw);
            txtMod = itemView.findViewById(R.id.txtMod);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }

    public SavedChecksAdapter(ArrayList<Check> localCheckList) {
        this.localCheckList = localCheckList;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.check_list_item, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        Check check = localCheckList.get(position);
        holder.txtName.setText(check.getName());
        DecimalFormat df = new DecimalFormat("##.##");
        String chance = df.format(check.getChance() * 100) + "%";
        holder.txtChance.setText(chance);
        String stats = check.getE1() + "-" + check.getE2() + "-" + check.getE3();
        holder.txtStats.setText(stats);
        holder.txtTaw.setText(holder.txtTaw.getContext().getString(R.string.taw,check.getTaw()));
        holder.txtMod.setText(holder.txtMod.getContext().getString(R.string.mod,check.getMod()));

        holder.btnDelete.setOnClickListener(v -> {
            localCheckList.remove(position);
            notifyItemRemoved(position);
            CheckHelper.deleteCheck(position);
        });
    }


    @Override
    public int getItemCount() {
        return localCheckList.size();
    }
}
