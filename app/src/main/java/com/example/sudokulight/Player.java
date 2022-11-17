package com.example.sudokulight;

import android.content.Context;
import android.database.Cursor;

import androidx.loader.content.CursorLoader;

import java.util.concurrent.TimeUnit;

public class Player {
    public int Player_Id;
    public String Player_Name;
    public int Games;
    public int Wins;
    public float Success;
    public int Current;

    public Player(int id, String name, int games, int wins, float success, int current) {
        this.Player_Id = id;
        this.Player_Name = name;
        this.Games = games;
        this.Wins = wins;
        this.Success = success;
        this.Current = current;

    }

    public static class CustomCursorLoader extends CursorLoader {
        PlayersDB db;
        Context context;
        String order = "state";

        public CustomCursorLoader(Context context, PlayersDB db, String order) {
            super(context);
            this.db = db;
            this.order = order;
            this.context = context;
        }

        // Загрузка данных из БД в отдельном потоке
        @Override
        public Cursor loadInBackground() {
            Cursor cursor = db.fetchAllPlayers();
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return cursor;
        }
    }
}