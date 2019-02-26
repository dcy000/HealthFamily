package com.ml.module_shouhuan.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ml.module_shouhuan.R;

public class MsgSystemAdapter extends RecyclerView.Adapter<MsgSystemAdapter.MsgHolder> {
    public LayoutInflater mInflater;

    public MsgSystemAdapter(Context context){
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public MsgHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MsgHolder(mInflater.inflate(R.layout.item_msg_system, null));
    }

    @Override
    public void onBindViewHolder(@NonNull MsgHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 8;
    }

    class MsgHolder extends RecyclerView.ViewHolder{
        public MsgHolder(View view){
            super(view);
        }
    }
}
