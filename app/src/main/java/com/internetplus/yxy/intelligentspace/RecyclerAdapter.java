package com.internetplus.yxy.intelligentspace;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Y.X.Y on 2017/4/27 0027.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> implements View.OnClickListener {


    private onItemClickListener onItemClickListener = null;

    public static interface onItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(onItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                parent.getContext()).inflate(R.layout.roomitem, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position)
    {
        Button roomname = holder.roomname;
        roomname.setText(MainActivity.roomlist.get(position));
        holder.roomname.setTag(position);
        holder.roomname.setOnClickListener(this);
    }

    @Override
    public int getItemCount()
    {
        return MainActivity.roomlist.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {

        Button roomname;

        public MyViewHolder(View view)
        {
            super(view);
            roomname = (Button) view.findViewById(R.id.roomname);
        }
    }

    public void onClick(View v) {
        if (onItemClickListener != null) {
            onItemClickListener.onItemClick(v, (int)v.getTag());
        }
    }

}
