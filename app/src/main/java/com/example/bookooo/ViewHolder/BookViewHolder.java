package com.example.bookooo.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import android.widget.TextView;

import com.example.bookooo.R;


public class BookViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


    public TextView book_name;
    public ImageView book_image;

    private com.example.bookooo.interfac.itemClickListener itemClickListener;

    public void setItemClickListener(com.example.bookooo.interfac.itemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public BookViewHolder(@NonNull View itemView) {
        super(itemView);


        book_name = (TextView)itemView.findViewById(R.id.book_name);
        book_image = (ImageView)itemView.findViewById(R.id.book_image);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view,getAdapterPosition(),false);

    }
}
