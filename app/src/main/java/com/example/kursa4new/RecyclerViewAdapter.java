package com.example.kursa4new;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private Context mContext;
    private List<Note> mData;


    public RecyclerViewAdapter(Context mContext, List<Note> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.cardiew_note_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        try {

            holder.theme_note_Id.setText(mData.get(position).getTheme());
            holder.message_node_Id.setText(mData.get(position).getMessage());
//        holder.idNote_Id.setText(mData.get(position).getId());
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, DetailPageNote.class);

                    // passing data to the book activity
                    Log.e("EEEEEE",mData.get(position).getTheme());
                    Log.e("EEEEEE", String.valueOf(position));
                    intent.putExtra("theme", mData.get(position).getTheme());
                    intent.putExtra("message", mData.get(position).getMessage());
                    intent.putExtra("id", mData.get(position).getId());
                    intent.putExtra("updatedAt", mData.get(position).getDateUpdate());
                    intent.putExtra("createdAt", mData.get(position).getDateCreate());
                    //     intent.putExtra("", mData.get(position).getId());
                    intent.putExtra("idItem", holder.getAdapterPosition());
                    // start the activity
                    //  mContext.startActivity(intent);
                    ((Activity) mContext).startActivityForResult(intent, 1);
                }
            });
        } catch (Exception e) {
            Log.e("Error_onBindViewHolder", e.getMessage());
        }

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView theme_note_Id, message_node_Id, idNote_Id, dateUpdate_Id, dateCreate_Id;
        CardView cardView;

        public MyViewHolder(View itemView) {
            super(itemView);

            theme_note_Id = itemView.findViewById(R.id.theme_note_Id);
            dateUpdate_Id = itemView.findViewById(R.id.dateUpdate_Id);
            dateCreate_Id = itemView.findViewById(R.id.dateCreate_Id);
            message_node_Id = itemView.findViewById(R.id.message_node_Id);
            idNote_Id = itemView.findViewById(R.id.idNotes);
            cardView = itemView.findViewById(R.id.cardViewId);


        }
    }


}