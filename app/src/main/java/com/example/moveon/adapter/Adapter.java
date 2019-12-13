package com.example.moveon.adapter;

import android.media.MediaCodec;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.moveon.R;

import com.example.moveon.api.RetroClient;
import com.example.moveon.model.MoveOn;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {

    private List<MoveOn> list;
    private MoveOn moveOn;
    private AdapterClick click;
    private AdapterClickLongo clickLongo;


    public Adapter(List<MoveOn> listMoveon, AdapterClick adapterClick, AdapterClickLongo adapterClickLongo) {
        this.list = new ArrayList<>();
        this.list.addAll(listMoveon);
        click = adapterClick;
        clickLongo = adapterClickLongo;
        //MoveOn move = list.get(0);
        //Log.d("Logmove", move.getTitulo());

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemLista = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_dados, viewGroup, false);
        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {


        moveOn = list.get(i);
        String s = moveOn.getCreateadAt();
        int positionS = s.indexOf("T");
        String string = s.substring(0, positionS);

        Log.d("stringC", string);
        Log.d("adapter", moveOn.getCreateadAt());

        myViewHolder.titulo.setText(moveOn.getTitulo());
        myViewHolder.textData.setText(string);
        myViewHolder.ratingBar.setRating(Float.parseFloat(moveOn.getClassific()));
        Picasso.get().load(RetroClient.URL + "/files/" + moveOn.getImage()).into(myViewHolder.imageView);


        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                click.onClickAdapter(list.get(i));
            }
        });

        myViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                clickLongo.onClickLongAdapter(list.get(i));
                return myViewHolder.itemView.isLongClickable();
            }
        });


    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView titulo;
        TextView textData;
        ImageView imageView;
        RatingBar ratingBar;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            titulo = itemView.findViewById(R.id.textListTitulo);
            textData = itemView.findViewById(R.id.textListdata);
            imageView = itemView.findViewById(R.id.imageListView);
            ratingBar = itemView.findViewById(R.id.ratingBarList);

//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    click.onClickAdapter(list.get(itemView.getVerticalScrollbarPosition()));
//
//                }
//            });


        }
    }

}
