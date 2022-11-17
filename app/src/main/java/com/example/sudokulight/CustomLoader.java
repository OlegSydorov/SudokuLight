package com.example.sudokulight;

import android.content.Context;
import android.database.Cursor;

import androidx.loader.content.CursorLoader;

import java.util.concurrent.TimeUnit;

public class CustomLoader extends CursorLoader
{
    PlayersDB db;
    Context context;
    String order = "Success";

    public CustomLoader(Context context, PlayersDB db)
    {
        super(context);
        this.db = db;
        this.context = context;
    }


    @Override
    public Cursor loadInBackground() {
        Cursor cursor = db.fetchAllPlayersOrdered(order);
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return cursor;
    }
}