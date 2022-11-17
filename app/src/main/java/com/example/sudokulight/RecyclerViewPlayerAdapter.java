package com.example.sudokulight;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerViewPlayerAdapter  extends RecyclerView.Adapter<RecyclerViewPlayerAdapter.ViewHolder>

{
//    public interface ItemClickListener {
//        void onItemClick(View view, int position);
//    }

    private LayoutInflater mInflater;

    // ссылка на активность, которой будет сообщено о нажатии на пункт списка
    //private ItemClickListener mClickListener;

    // Вначале не выделен ни один элемент
    private int selectedPos = RecyclerView.NO_POSITION;

    private Context context;
    private ArrayList<Player>players;


    public RecyclerViewPlayerAdapter(Context context, ArrayList<Player>players)
    {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.players = players;
    }


    // запускается однажды только при создании пунктов RecyclerView
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    // Запускается для каждой строки списка, когда её нужно заполнить
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        if(players.size()>0 && position<= players.size())
        {
            String name = players.get(position).Player_Name;
            String games = String.valueOf(players.get(position).Games);
            String wins = String.valueOf(players.get(position).Wins);
            String success = String.valueOf(players.get(position).Success);
            String current = String.valueOf(players.get(position).Current);

            Log.d("SUDOKU", "BINDING DATA: "+name+" "+games +" "+wins+" "+success+" "+current);

            holder.name.setText(name);
            holder.games.setText("Games played: "+games);
            holder.wins.setText("Games won: "+wins);
            holder.success.setText(success);

            if(current.equals("1"))
                    holder.itemView.setBackgroundColor(Color.rgb(255, 0, 0));
                else
                    holder.itemView.setBackgroundColor(Color.rgb(255, 196, 255));
            }
    }

    @Override
    public int getItemCount()
    {
        return players.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        public TextView name;
        public TextView games;
        public TextView wins;
        public TextView success;

        public ViewHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.name);
            games = (TextView) itemView.findViewById(R.id.gamesView);
            wins = (TextView) itemView.findViewById(R.id.winsView);
            success = (TextView) itemView.findViewById(R.id.successView);

            // Разрешить нажатия на пунктах
           // itemView.setClickable(true);

            // устанвить обработчик нажатия на пункт списка
           // itemView.setOnClickListener(this);
        }

        @Override
        // Реакция на нажатие
        public void onClick(View view)
        {

        }
    }

//    public void setClickListener(ItemClickListener itemClickListener) {
//        this.mClickListener = itemClickListener;
//    }
}