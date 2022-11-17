package com.example.sudokulight;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class PlayersDB extends SQLiteOpenHelper
{

    final static String databaseName = "SudokuLight.db";
    final static int databaseVersion = 1;

    private static final String SQLITE_TABLE = "Players";

    // специальное стандартное поле, необходимое для работы CursorAdapter
    public static final String KEY_ROWID = "rowid _id";
    public static final String KEY_PLAYER_ID = "PLayer_ID";
    public static final String KEY_NAME = "Player_Name";
    public static final String KEY_GAMES = "Games";
    public static final String KEY_WINS = "Wins";
    public static final String KEY_SUCCESS = "Success";
    public static final String KEY_CURRENT = "Current";

    Context appContext;

    private SQLiteDatabase db;

    public PlayersDB(Context context)
    {
        super(context, databaseName, null, databaseVersion);
        appContext = context;

        // при запуске приложения сделать копию из резерва (asssets)
        try {
            copyDataBase(databaseName);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //db.execSQL("CREATE TABLE authors(au_id INTEGER PRIMARY KEY, au_lname VARCHAR(40), au_fname VARCHAR(20), city VARCHAR(20), state VARCHAR(2))");
    }

    // Копирование из папки assets в папку для работы
    private void copyDataBase(String dbname) throws IOException
    {
        // Открыть бд в assets
        InputStream myInput = appContext.getAssets().open(dbname);

        //Toast.makeText(appContext, appContext.getAssets().toString(), Toast.LENGTH_LONG).show();

        // Путь в бд в папке databases
        String dbPath = appContext.getFilesDir().getPath().replace("files","databases/");
        String outFileName = dbPath + dbname;

        File f = new File(outFileName);
        if(!f.exists()) {
//                f.delete();
//        }
//        else
//        {
            File f2 = new File(dbPath);
            f2.mkdir();
            OutputStream myOutput = new FileOutputStream(outFileName);
            byte[] buffer = new byte[102400];
            int length;
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }
            myOutput.flush();
            myOutput.close();
            myInput.close();
        }


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

    }

    public SQLiteDatabase open() throws SQLException
    {
        db = this.getWritableDatabase();
        return db;
    }


    public Cursor fetchAllPlayers()
    {
        Cursor cursor = db.query(SQLITE_TABLE, new String[] {KEY_ROWID, KEY_PLAYER_ID, KEY_NAME, KEY_GAMES, KEY_WINS, KEY_SUCCESS, KEY_CURRENT},
                null, null, null, null, null);

        // Переместить курсор на первую строку результата запроса
        if (cursor != null) cursor.moveToFirst();
        Log.d("SUDOKU", String.valueOf(cursor.getCount()));
        return cursor;
    }

    public Cursor fetchAllPlayersOrdered(String order)
    {
        Cursor cursor = db.query(SQLITE_TABLE, new String[] {KEY_ROWID, KEY_PLAYER_ID, KEY_NAME, KEY_GAMES, KEY_WINS, KEY_SUCCESS, KEY_CURRENT},
                null, null, null, null, order);

        if (cursor != null) cursor.moveToFirst();
        Log.d("SUDOKU", String.valueOf(cursor.getCount()));
        return cursor;
    }

    public Player fetchPlayer(int id)
    {
        Cursor cursor = db.query(SQLITE_TABLE, new String[] {KEY_ROWID, KEY_PLAYER_ID, KEY_NAME, KEY_GAMES, KEY_WINS, KEY_SUCCESS, KEY_CURRENT},
                KEY_PLAYER_ID+"= ?", new String[]{String.valueOf(id)}, null, null, null);
        if (cursor != null) cursor.moveToFirst();
        return new Player(Integer.parseInt(cursor.getString(1)),
                cursor.getString(2),
                Integer.parseInt(cursor.getString(3)),
                Integer.parseInt(cursor.getString(4)),
                Float.parseFloat(cursor.getString(5)),
                Integer.parseInt(cursor.getString(6)));
    }


    public Player fetchCurrentPlayer()
    {
        Cursor cursor = db.query(SQLITE_TABLE, new String[] {KEY_ROWID, KEY_PLAYER_ID, KEY_NAME, KEY_GAMES, KEY_WINS, KEY_SUCCESS, KEY_CURRENT},
                KEY_CURRENT+"= ?", new String[]{String.valueOf(1)}, null, null, null);
        if (cursor != null) cursor.moveToFirst();
        return new Player(Integer.parseInt(cursor.getString(1)),
                            cursor.getString(2),
                            Integer.parseInt(cursor.getString(3)),
                            Integer.parseInt(cursor.getString(4)),
                            Float.parseFloat(cursor.getString(5)),
                            Integer.parseInt(cursor.getString(6)));
    }

    public int checkName(String name)
    {
        Cursor cursor = db.query(SQLITE_TABLE, new String[] {KEY_ROWID, KEY_PLAYER_ID, KEY_NAME, KEY_GAMES, KEY_WINS, KEY_SUCCESS, KEY_CURRENT},
                KEY_NAME+"= ?", new String[]{name}, null, null, null);
        if (cursor.getCount()>0)
        {
            cursor.moveToFirst();
            Log.d("SUDOKU", cursor.getString(1));
            return Integer.parseInt(cursor.getString(1));
        }
        else return 0;
       // String countQuery = "SELECT Player_Id FROM Players WHERE Player_Name="+name+";";
       // Cursor temp=db.rawQuery(countQuery, null);
       // temp.moveToFirst();
       // Log.d("SUDOKU", temp.getString(0));
       // return Integer.parseInt(temp.getString(0));
    }


    public long addPlayer(String Player_name, int games, int wins, float success, int current)
    {
        try {
            ContentValues initialValues = new ContentValues();


        initialValues.put(KEY_NAME, Player_name);
        initialValues.put(KEY_GAMES, games);
        initialValues.put(KEY_WINS, wins);
        initialValues.put(KEY_SUCCESS, success);
        initialValues.put(KEY_CURRENT, current);

        Log.d("SUDOKU", String.valueOf(initialValues));

        return db.insertOrThrow(SQLITE_TABLE, null, initialValues);
        } catch (SQLException e) {
            Log.d("SUDOKU", e.getMessage());
            return -1;
        }
    }

    public long editPlayer(int Player_id, String Player_name, int games, int wins, float success, int current)
    {
        ContentValues initialValues = new ContentValues();

        initialValues.put(KEY_NAME, Player_name);
        initialValues.put(KEY_GAMES, games);
        initialValues.put(KEY_WINS, wins);
        initialValues.put(KEY_SUCCESS, success);
        initialValues.put(KEY_CURRENT, current);

        String filter = KEY_PLAYER_ID+" = ?";

        return db.update(SQLITE_TABLE, initialValues, filter, new String[] {String.valueOf(Player_id)});
    }

    public void delPlayer(int id)
    {
        String[] args = new String[]{String.valueOf(id)};

        db.delete(SQLITE_TABLE, KEY_PLAYER_ID+"= ?", args);
    }

}