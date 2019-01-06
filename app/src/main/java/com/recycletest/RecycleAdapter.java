package com.recycletest;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.recycletest.recycleview.RecyclerView;

import java.util.List;

public class RecycleAdapter extends RecyclerView.Adapter {

    Context mContext;
    List<Object> mObjectList;

    public RecycleAdapter(Context context, List<Object> objectList) {
        mContext = context;
        mObjectList = objectList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_layout, null);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mObjectList != null ? mObjectList.size() : 0;
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        ItemViewHolder(View itemView) {
            super(itemView);
        }
    }


}
