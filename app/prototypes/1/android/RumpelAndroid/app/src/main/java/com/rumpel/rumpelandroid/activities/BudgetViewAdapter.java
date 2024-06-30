package com.rumpel.rumpelandroid.activities;

import static com.rumpel.rumpelandroid.utils.DateUtils.formatDate;
import static com.rumpel.rumpelandroid.utils.NumUtils.formatCurrency;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rumpel.rumpelandroid.R;
import com.rumpel.rumpelandroid.activities.interfaces.InjectorBudget;
import com.szaumoor.rumple.model.entities.Budget;
import com.szaumoor.rumple.model.entities.PaymentMethod;
import com.szaumoor.rumple.model.utils.types.Interval;
import com.szaumoor.rumple.model.utils.types.Limit;

import java.util.List;

public class BudgetViewAdapter extends RecyclerView.Adapter<BudgetViewAdapter.BudgetViewHolder> {
    static class BudgetViewHolder extends RecyclerView.ViewHolder {
        TextView header;
        TextView interval;
        TextView budgetLimit;
        RelativeLayout frame;

        public BudgetViewHolder(@NonNull View itemView) {
            super(itemView);
            header = itemView.findViewById(R.id.txt_budget_header);
            interval = itemView.findViewById(R.id.txt_interval_id);
            budgetLimit = itemView.findViewById(R.id.txt_budget_limit);
            frame = itemView.findViewById(R.id.budget_frame_id);
        }
    }

    private final List<Budget> list;
    private final InjectorBudget injector;

    public BudgetViewAdapter(final List<Budget> items, final InjectorBudget injector) {
        this.list = items;
        this.injector = injector;
    }

    @NonNull
    @Override
    public BudgetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemList = inflater.inflate(R.layout.budget_list_item, parent, false);
        return new BudgetViewHolder(itemList);
    }

    @Override
    public void onBindViewHolder(@NonNull BudgetViewHolder holder, int position) {
        final Budget item = list.get(position);
        final PaymentMethod pm = item.getPaymentMethod();
        String str = "#" + position + " - " + (pm == null ? "Universal" : pm);
        holder.header.setText(str);
        final Interval interval = item.getInterval();
        str = formatDate(interval.getStartDate()) + " - " + formatDate(interval.getEndDate());
        holder.interval.setText(str);
        final Limit limit = item.getLimit();
        str = formatCurrency(limit.getSoftLimit()) + " - " + formatCurrency(limit.getHardLimit());
        holder.budgetLimit.setText(str + "\n" + pm);
        holder.frame.setOnClickListener(v -> {
            if (injector == null) throw new NullPointerException("Injector can't be null");
            injector.apply(item);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}