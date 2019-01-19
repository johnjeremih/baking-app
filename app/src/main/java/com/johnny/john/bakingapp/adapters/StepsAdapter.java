package com.johnny.john.bakingapp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.johnny.john.bakingapp.R;
import com.johnny.john.bakingapp.model.Steps;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.MyViewHolder> {




    public int mPosition;
    private int focusedItem = 0;
    private List<Steps> list;
    private Context context;
    private final StepsAdapter.StepAdapterClickHandler mClickHandler;
    private int checker = 10;


    public interface StepAdapterClickHandler {
        void onClickHandler(int position);
    }
    public StepsAdapter(Context context, List<Steps> list, StepAdapterClickHandler handler, int position) {
        this.context = context;
        this.list = list;
        mClickHandler = handler;
        mPosition = position;

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.title_steps)
        TextView shortDescriptionView;
        @BindView(R.id.item_steps_linear_layout)
        LinearLayout item_steps;
        @BindView(R.id.step_card)
        CardView stepCard;



        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);


        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        final Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.steps_item, parent, false);



        final MyViewHolder viewHolder = new MyViewHolder(view);

        viewHolder.item_steps.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View vi) {

                int position = viewHolder.getAdapterPosition();

                focusedItem = position;
                notifyDataSetChanged();

                if (checker != position) {
                    mClickHandler.onClickHandler(position);
                    checker = position;


                } else {
                    Toast.makeText(context,"You are clicking the same step ",Toast.LENGTH_SHORT).show();


                }

            }
        });

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.shortDescriptionView.setText(list.get(position).getShortDescription());
        holder.stepCard.setBackgroundColor(Color.parseColor("#ffffff"));

       if (mPosition == 10){
           mPosition=position;
       }else {
           ItemSelected(holder,position);
       }
    }

    private void ItemSelected(MyViewHolder holder, int position) {
        if (focusedItem == position){
            holder.stepCard.setBackgroundColor(Color.parseColor("#640277BD"));

        }

    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public int getItemCount() {
        return list.size();
    }


}
