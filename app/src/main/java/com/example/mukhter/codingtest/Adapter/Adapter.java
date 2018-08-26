package com.example.mukhter.codingtest.Adapter;

/**
 * Created by MUKHTER on 25/08/2018.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mukhter.codingtest.Model.Model;
import com.example.mukhter.codingtest.R;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<Model> namemodelArrayList;



    public Adapter(Context ctx, ArrayList<Model> nameArrayList){

        inflater = LayoutInflater.from(ctx);

        this.namemodelArrayList = nameArrayList;


    }

    public void removeItem(int position) {
        namemodelArrayList.remove(position);

        notifyItemRemoved(position);

        notifyItemRangeChanged(position, namemodelArrayList.size());
    }
    public void restoreItem(Model model, int position) {
        namemodelArrayList.add(position, model);

        // notify item added by position
        notifyItemInserted(position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.rv_item, parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.time.setText(namemodelArrayList.get(position).getName());
           holder.curr.setText(namemodelArrayList.get(position).getCurrency());
        holder.lang.setText(namemodelArrayList.get(position).getLanguage());
    }

    @Override
    public int getItemCount() {
        return namemodelArrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView time,curr,lang;


        public MyViewHolder(View itemView) {
            super(itemView);

            time = (TextView) itemView.findViewById(R.id.tv);
            curr=(TextView)itemView.findViewById(R.id.currency);
            lang=(TextView)itemView.findViewById(R.id.language);

        }

    }
}
