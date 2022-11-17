package com.example.sudokulight;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.loader.content.CursorLoader;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Game extends AppCompatActivity
        implements View.OnClickListener,
                    ResultFragmentListener,
                    ExitFragmentListener
{


    TextView select1;
    TextView select2;
    TextView select3;
    TextView select4;
    TextView select5;
    TextView select6;
    TextView select7;
    TextView select8;
    TextView select9;
    TextView select10;

    TextView cell11;
    TextView cell12;
    TextView cell13;
    TextView cell21;
    TextView cell22;
    TextView cell23;
    TextView cell31;
    TextView cell32;
    TextView cell33;

    ImageView sound;
    ImageView stats;
    TextView exit;

    MediaPlayer mp;
    SoundPool soundPool;
    int music;
    int musicPlayed;

    Boolean selectFlag;
    Boolean soundFlag;
    int selectedValue=0;
    String currentPlayerName;
    HashMap<String, Player> players;

    Activity activity;

    HashMap <String, Cell>selects;
    HashMap <String, Cell>cells;
    int sum, a, b, c, d, e, f, g, h, i;
    Random rand;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        activity=this;
        selects=new HashMap<String, Cell>();
        cells=new HashMap<String, Cell>();
        players=new HashMap<String, Player>();

        select1=(TextView)findViewById(R.id.select1);
        select1.setOnClickListener(this);
        selects.put("select1", new Cell("select1", false, 1, select1));

        select2=(TextView)findViewById(R.id.select2);
        select2.setOnClickListener(this);
        selects.put("select2", new Cell("select2", false, 2, select2));

        select3=(TextView)findViewById(R.id.select3);
        select3.setOnClickListener(this);
        selects.put("select3", new Cell("select3", false, 3, select3));

        select4=(TextView)findViewById(R.id.select4);
        select4.setOnClickListener(this);
        selects.put("select4", new Cell("select4", false, 4, select4));

        select5=(TextView)findViewById(R.id.select5);
        select5.setOnClickListener(this);
        selects.put("select5", new Cell("select5", false, 5, select5));

        select6=(TextView)findViewById(R.id.select6);
        select6.setOnClickListener(this);
        selects.put("select6", new Cell("select6", false, 6, select6));

        select7=(TextView)findViewById(R.id.select7);
        select7.setOnClickListener(this);
        selects.put("select7", new Cell("select7", false, 7, select7));

        select8=(TextView)findViewById(R.id.select8);
        select8.setOnClickListener(this);
        selects.put("select8", new Cell("select8", false, 8, select8));

        select9=(TextView)findViewById(R.id.select9);
        select9.setOnClickListener(this);
        selects.put("select9", new Cell("select9", false, 9, select9));

        select10=(TextView)findViewById(R.id.select10);
        select10.setOnClickListener(this);
        selects.put("select10", new Cell("select10", false, 10, select10));


        cell11=(TextView)findViewById(R.id.cellA);
        cell11.setOnClickListener(this);
        cells.put("A", new Cell("A", false, 0, cell11));

        cell12=(TextView)findViewById(R.id.cellB);
        cell12.setOnClickListener(this);
        cells.put("B", new Cell("B", false, 0, cell12));

        cell13=(TextView)findViewById(R.id.cellC);
        cell13.setOnClickListener(this);
        cells.put("C", new Cell("C", false, 0, cell13));

        cell21=(TextView)findViewById(R.id.cellD);
        cell21.setOnClickListener(this);
        cells.put("D", new Cell("D", false, 0, cell21));

        cell22=(TextView)findViewById(R.id.cellE);
        cell22.setOnClickListener(this);
        cells.put("E", new Cell("E", false, 0, cell22));

        cell23=(TextView)findViewById(R.id.cellF);
        cell23.setOnClickListener(this);
        cells.put("F", new Cell("F", false, 0, cell23));

        cell31=(TextView)findViewById(R.id.cellG);
        cell31.setOnClickListener(this);
        cells.put("G", new Cell("G", false, 0, cell31));

        cell32=(TextView)findViewById(R.id.cellH);
        cell32.setOnClickListener(this);
        cells.put("H", new Cell("H", false, 0, cell32));

        cell33=(TextView)findViewById(R.id.cellI);
        cell33.setOnClickListener(this);
        cells.put("I", new Cell("I", false, 0, cell33));

        soundPool= new SoundPool(1, AudioManager.STREAM_MUSIC,0);
        music= soundPool.load(this, R.raw.sudoku_background1,1);
        musicPlayed=0;

        sound=(ImageView) findViewById(R.id.soundButton);
        sound.setOnClickListener(this);
        stats=(ImageView) findViewById(R.id.statsButton);
        stats.setOnClickListener(this);
        exit=(TextView)findViewById(R.id.exitButton);
        exit.setOnClickListener(this);



         selectFlag=false;
         soundFlag=false;

        rand=new Random();


        Intent intent = getIntent();
        String inputData=intent.getStringExtra("data");
        if (inputData.length()>0)
        {
            Log.d("SUDOKU", "GAME receive data: "+inputData);
            String[] params1=inputData.split("/--__--/");
            Log.d("SUDOKU",String.valueOf(params1.length));
            if (params1.length>0)
            {
                for (int i=0; i<params1.length; i++) {
                    String[] params2 = params1[i].split("/-_-/");

                    Log.d("SUDOKU", "Game activity+PLAYER: " + params2[0] + " " + params2[1] + " " + params2[2] + " " + params2[3] + " " + params2[4] + " " + params2[5]);
                    players.put(params2[1], new Player(Integer.parseInt(params2[0]), params2[1], Integer.parseInt(params2[2]), Integer.parseInt(params2[3]), Float.parseFloat(params2[4]), Integer.parseInt(params2[5])));
                    if (params2[5].equals("1"))
                        currentPlayerName = params2[1];
                }
            }
        }
        Log.d("SUDOKU", "Current player: "+currentPlayerName);
        StartGame();

    }

    public void StartGame()
    {
        a =  rand.nextInt(10)+1;
        b =  rand.nextInt(10)+1;
        c =  rand.nextInt(10)+1;

        d = -1;
        f = -1;
        e = -1;
        h = -1;

        sum = a + b + c;

        while (d <= 0 || f <= 0 || e <= 0 || h <= 0 || d>10 || f >10 || e >10 || h >10)
        {
            g = rand.nextInt(10)+1;
            while (sum - (g + a) > 10 || sum - (g + a) <= 0)
            {
                g = rand.nextInt(10)+1;
            }

            i = rand.nextInt(10)+1;
            while (sum - (i + c) > 10 || sum - (i + c) <= 0 || sum - (i + g) > 10 || sum - (i + g) <= 0)
            {
                i = rand.nextInt(10)+1;
            }

            d = sum - (a + g);
            f = sum - (c + i);
            e = sum - (d + f);
            h = sum - (g + i);
        }
        Log.d("SUDOKU", "FIGURES:"+ String.valueOf(a)+ " "+String.valueOf(b)+ " "+String.valueOf(c)+ " "+
                                            String.valueOf(d)+ " "+String.valueOf(e)+ " "+String.valueOf(f)+ " "+
                                            String.valueOf(g)+ " "+String.valueOf(h)+ " "+String.valueOf(i));

        int loc=  rand.nextInt(3)+1;
        int indicator =  rand.nextInt(2)+1;

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        if (indicator == 1)
        {
            switch (loc)
            {
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                case 1:
                {
                    cells.get("A").val = a;
                    cells.get("A").content = true;
                    cells.get("A").textView.setText(String.valueOf(a));
                    cells.get("A").textView.setBackground(ContextCompat.getDrawable(this, getResource(a)));

                    cells.get("B").val = b;
                    cells.get("B").content = true;
                    cells.get("B").textView.setText(String.valueOf(b));
                    cells.get("B").textView.setBackground(ContextCompat.getDrawable(this, getResource(b)));

                    cells.get("C").val = c;
                    cells.get("C").content = true;
                    cells.get("C").textView.setText(String.valueOf(c));
                    cells.get("C").textView.setBackground(ContextCompat.getDrawable(this, getResource(c)));

                    int loc2 =  rand.nextInt(3)+1;
                    switch (loc2)
                    {
                        case 1:
                        {
                            cells.get("D").val = d;
                            cells.get("D").content = true;
                            cells.get("D").textView.setText(String.valueOf(d));
                            cells.get("D").textView.setBackground(ContextCompat.getDrawable(this, getResource(d)));

                            int loc3 =  rand.nextInt(2)+1;

                            switch (loc3)
                            {
                                case 1:
                                {
                                    cells.get("H").val = h;
                                    cells.get("H").content = true;
                                    cells.get("H").textView.setText(String.valueOf(h));
                                    cells.get("H").textView.setBackground(ContextCompat.getDrawable(this, getResource(h)));
                                    break;
                                }
                                case 2:
                                {
                                    cells.get("I").val = i;
                                    cells.get("I").content = true;
                                    cells.get("I").textView.setText(String.valueOf(i));
                                    cells.get("I").textView.setBackground(ContextCompat.getDrawable(this, getResource(i)));
                                    break;
                                }
                            }
                            break;
                        }
                        case 2:
                        {
                            cells.get("E").val = e;
                            cells.get("E").content = true;
                            cells.get("E").textView.setText(String.valueOf(e));
                            cells.get("E").textView.setBackground(ContextCompat.getDrawable(this, getResource(e)));

                            int loc3 =  rand.nextInt(2)+1;

                            switch (loc3)
                            {
                                case 1:
                                {
                                    cells.get("G").val = g;
                                    cells.get("G").content = true;
                                    cells.get("G").textView.setText(String.valueOf(g));
                                    cells.get("G").textView.setBackground(ContextCompat.getDrawable(this, getResource(g)));
                                    break;
                                }
                                case 2:
                                {
                                    cells.get("I").val = i;
                                    cells.get("I").content = true;
                                    cells.get("I").textView.setText(String.valueOf(i));
                                    cells.get("I").textView.setBackground(ContextCompat.getDrawable(this, getResource(i)));
                                    break;
                                }
                            }
                            break;
                        }
                        case 3:
                        {
                            cells.get("F").val = f;
                            cells.get("F").content = true;
                            cells.get("F").textView.setText(String.valueOf(f));
                            cells.get("F").textView.setBackground(ContextCompat.getDrawable(this, getResource(f)));

                            int loc3 =  rand.nextInt(2)+1;

                            switch (loc3)
                            {
                                case 1:
                                {
                                    cells.get("G").val = g;
                                    cells.get("G").content = true;
                                    cells.get("G").textView.setText(String.valueOf(g));
                                    cells.get("G").textView.setBackground(ContextCompat.getDrawable(this, getResource(g)));
                                    break;
                                }
                                case 2:
                                {
                                    cells.get("H").val = h;
                                    cells.get("H").content = true;
                                    cells.get("H").textView.setText(String.valueOf(h));
                                    cells.get("H").textView.setBackground(ContextCompat.getDrawable(this, getResource(h)));
                                    break;
                                }
                            }
                            break;
                        }
                    }
                    break;

                }
                //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                case 2:
                {
                    cells.get("D").val = d;
                    cells.get("D").content = true;
                    cells.get("D").textView.setText(String.valueOf(d));
                    cells.get("D").textView.setBackground(ContextCompat.getDrawable(this, getResource(d)));

                    cells.get("E").val = e;
                    cells.get("E").content = true;
                    cells.get("E").textView.setText(String.valueOf(e));
                    cells.get("E").textView.setBackground(ContextCompat.getDrawable(this, getResource(e)));

                    cells.get("F").val = f;
                    cells.get("F").content = true;
                    cells.get("F").textView.setText(String.valueOf(f));
                    cells.get("F").textView.setBackground(ContextCompat.getDrawable(this, getResource(f)));

                    int loc2 =  rand.nextInt(3)+1;

                    switch (loc2)
                    {
                        case 1:
                        {
                            cells.get("A").val = a;
                            cells.get("A").content = true;
                            cells.get("A").textView.setText(String.valueOf(a));
                            cells.get("A").textView.setBackground(ContextCompat.getDrawable(this, getResource(a)));

                            int loc3 =  rand.nextInt(2)+1;

                            switch (loc3)
                            {
                                case 1:
                                {
                                    cells.get("H").val = h;
                                    cells.get("H").content = true;
                                    cells.get("H").textView.setText(String.valueOf(h));
                                    cells.get("H").textView.setBackground(ContextCompat.getDrawable(this, getResource(h)));
                                    break;
                                }
                                case 2:
                                {
                                    cells.get("I").val = i;
                                    cells.get("I").content = true;
                                    cells.get("I").textView.setText(String.valueOf(i));
                                    cells.get("I").textView.setBackground(ContextCompat.getDrawable(this, getResource(i)));
                                    break;
                                }
                            }
                            break;
                        }
                        case 2:
                        {
                            cells.get("B").val = b;
                            cells.get("B").content = true;
                            cells.get("B").textView.setText(String.valueOf(b));
                            cells.get("B").textView.setBackground(ContextCompat.getDrawable(this, getResource(b)));
                            int loc3 = rand.nextInt(2)+1;

                            switch (loc3)
                            {
                                case 1:
                                {
                                    cells.get("G").val = g;
                                    cells.get("G").content = true;
                                    cells.get("G").textView.setText(String.valueOf(g));
                                    cells.get("G").textView.setBackground(ContextCompat.getDrawable(this, getResource(g)));
                                    break;
                                }
                                case 2:
                                {
                                    cells.get("I").val = i;
                                    cells.get("I").content = true;
                                    cells.get("I").textView.setText(String.valueOf(i));
                                    cells.get("I").textView.setBackground(ContextCompat.getDrawable(this, getResource(i)));
                                    break;
                                }
                            }
                            break;
                        }
                        case 3:
                        {
                            cells.get("C").val = c;
                            cells.get("C").content = true;
                            cells.get("C").textView.setText(String.valueOf(c));
                            cells.get("C").textView.setBackground(ContextCompat.getDrawable(this, getResource(c)));

                            int loc3 =  rand.nextInt(2)+1;

                            switch (loc3)
                            {
                                case 1:
                                {
                                    cells.get("G").val = g;
                                    cells.get("G").content = true;
                                    cells.get("G").textView.setText(String.valueOf(g));
                                    cells.get("G").textView.setBackground(ContextCompat.getDrawable(this, getResource(g)));
                                    break;
                                }
                                case 2:
                                {
                                    cells.get("H").val = h;
                                    cells.get("H").content = true;
                                    cells.get("H").textView.setText(String.valueOf(h));
                                    cells.get("H").textView.setBackground(ContextCompat.getDrawable(this, getResource(h)));
                                    break;
                                }
                            }
                            break;
                        }
                    }
                    break;
                }

////////////////////////////////////////////////////////////////////////////////////////////////////// ////////////////////////////////////////////////

                case 3:
                {
                    cells.get("G").val = g;
                    cells.get("G").content = true;
                    cells.get("G").textView.setText(String.valueOf(g));
                    cells.get("G").textView.setBackground(ContextCompat.getDrawable(this, getResource(g)));

                    cells.get("H").val = h;
                    cells.get("H").content = true;
                    cells.get("H").textView.setText(String.valueOf(h));
                    cells.get("H").textView.setBackground(ContextCompat.getDrawable(this, getResource(h)));

                    cells.get("I").val = i;
                    cells.get("I").content = true;
                    cells.get("I").textView.setText(String.valueOf(i));
                    cells.get("I").textView.setBackground(ContextCompat.getDrawable(this, getResource(i)));

                    int loc2 =  rand.nextInt(3)+1;

                    switch (loc2)
                    {
                        case 1:
                        {
                            cells.get("A").val = a;
                            cells.get("A").content = true;
                            cells.get("A").textView.setText(String.valueOf(a));
                            cells.get("A").textView.setBackground(ContextCompat.getDrawable(this, getResource(a)));

                            int loc3 =  rand.nextInt(2)+1;

                            switch (loc3)
                            {
                                case 1:
                                {
                                    cells.get("E").val = e;
                                    cells.get("E").content = true;
                                    cells.get("E").textView.setText(String.valueOf(e));
                                    cells.get("E").textView.setBackground(ContextCompat.getDrawable(this, getResource(e)));
                                    break;
                                }
                                case 2:
                                {
                                    cells.get("F").val = f;
                                    cells.get("F").content = true;
                                    cells.get("F").textView.setText(String.valueOf(f));
                                    cells.get("F").textView.setBackground(ContextCompat.getDrawable(this, getResource(f)));
                                    break;
                                }
                            }
                            break;
                        }
                        case 2:
                        {
                            cells.get("B").val = b;
                            cells.get("B").content = true;
                            cells.get("B").textView.setText(String.valueOf(b));
                            cells.get("B").textView.setBackground(ContextCompat.getDrawable(this, getResource(b)));
                            int loc3 = rand.nextInt(2)+1;

                            switch (loc3)
                            {
                                case 1:
                                {
                                    cells.get("D").val = d;
                                    cells.get("D").content = true;
                                    cells.get("D").textView.setText(String.valueOf(d));
                                    cells.get("D").textView.setBackground(ContextCompat.getDrawable(this, getResource(d)));
                                    break;
                                }
                                case 2:
                                {
                                    cells.get("F").val = f;
                                    cells.get("F").content = true;
                                    cells.get("F").textView.setText(String.valueOf(f));
                                    cells.get("F").textView.setBackground(ContextCompat.getDrawable(this, getResource(f)));

                                    break;
                                }
                            }
                            break;
                        }
                        case 3:
                        {
                            cells.get("C").val = c;
                            cells.get("C").content = true;
                            cells.get("C").textView.setText(String.valueOf(c));
                            cells.get("C").textView.setBackground(ContextCompat.getDrawable(this, getResource(c)));

                            int loc3 =  rand.nextInt(2)+1;

                            switch (loc3)
                            {
                                case 1:
                                {
                                    cells.get("D").val = d;
                                    cells.get("D").content = true;
                                    cells.get("D").textView.setText(String.valueOf(d));
                                    cells.get("D").textView.setBackground(ContextCompat.getDrawable(this, getResource(d)));
                                    break;
                                }
                                case 2:
                                {
                                    cells.get("E").val = e;
                                    cells.get("E").content = true;
                                    cells.get("E").textView.setText(String.valueOf(e));
                                    cells.get("E").textView.setBackground(ContextCompat.getDrawable(this, getResource(e)));
                                    break;
                                }
                            }
                            break;
                        }
                    }
                    break;
                }
            }
        }

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        else if (indicator == 2)
        {
            switch (loc)
            {
                case 1:
                {
                    cells.get("A").val = a;
                    cells.get("A").content = true;
                    cells.get("A").textView.setText(String.valueOf(a));
                    cells.get("A").textView.setBackground(ContextCompat.getDrawable(this, getResource(a)));

                    cells.get("D").val = d;
                    cells.get("D").content = true;
                    cells.get("D").textView.setText(String.valueOf(d));
                    cells.get("D").textView.setBackground(ContextCompat.getDrawable(this, getResource(d)));

                    cells.get("G").val = g;
                    cells.get("G").content = true;
                    cells.get("G").textView.setText(String.valueOf(g));
                    cells.get("G").textView.setBackground(ContextCompat.getDrawable(this, getResource(g)));

                    int loc2 =  rand.nextInt(3)+1;

                    switch (loc2)
                    {
                        case 1:
                        {
                            cells.get("B").val = b;
                            cells.get("B").content = true;
                            cells.get("B").textView.setText(String.valueOf(b));
                            cells.get("B").textView.setBackground(ContextCompat.getDrawable(this, getResource(b)));

                            int loc3 = rand.nextInt(2)+1;

                            switch (loc3)
                            {
                                case 1:
                                {
                                    cells.get("F").val = f;
                                    cells.get("F").content = true;
                                    cells.get("F").textView.setText(String.valueOf(f));
                                    cells.get("F").textView.setBackground(ContextCompat.getDrawable(this, getResource(f)));
                                    break;
                                }
                                case 2:
                                {
                                    cells.get("I").val = i;
                                    cells.get("I").content = true;
                                    cells.get("I").textView.setText(String.valueOf(i));
                                    cells.get("I").textView.setBackground(ContextCompat.getDrawable(this, getResource(i)));

                                    break;
                                }
                            }
                            break;
                        }
                        case 2:
                        {
                            cells.get("E").val = e;
                            cells.get("E").content = true;
                            cells.get("E").textView.setText(String.valueOf(e));
                            cells.get("E").textView.setBackground(ContextCompat.getDrawable(this, getResource(e)));

                            int loc3 =  rand.nextInt(2)+1;

                            switch (loc3)
                            {
                                case 1:
                                {
                                    cells.get("C").val = c;
                                    cells.get("C").content = true;
                                    cells.get("C").textView.setText(String.valueOf(c));
                                    cells.get("C").textView.setBackground(ContextCompat.getDrawable(this, getResource(c)));

                                    break;
                                }
                                case 2:
                                {
                                    cells.get("I").val = i;
                                    cells.get("I").content = true;
                                    cells.get("I").textView.setText(String.valueOf(i));
                                    cells.get("I").textView.setBackground(ContextCompat.getDrawable(this, getResource(i)));

                                    break;
                                }
                            }
                            break;
                        }
                        case 3:
                        {
                            cells.get("H").val = h;
                            cells.get("H").content = true;
                            cells.get("H").textView.setText(String.valueOf(h));
                            cells.get("H").textView.setBackground(ContextCompat.getDrawable(this, getResource(h)));

                            int loc3 =  rand.nextInt(2)+1;

                            switch (loc3)
                            {
                                case 1:
                                {
                                    cells.get("C").val = c;
                                    cells.get("C").content = true;
                                    cells.get("C").textView.setText(String.valueOf(c));
                                    cells.get("C").textView.setBackground(ContextCompat.getDrawable(this, getResource(c)));

                                    break;
                                }
                                case 2:
                                {
                                    cells.get("F").val = f;
                                    cells.get("F").content = true;
                                    cells.get("F").textView.setText(String.valueOf(f));
                                    cells.get("F").textView.setBackground(ContextCompat.getDrawable(this, getResource(f)));
                                    break;
                                }
                            }
                            break;
                        }
                    }
                    break;
                }

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                case 2:
                {
                    cells.get("B").val = b;
                    cells.get("B").content = true;
                    cells.get("B").textView.setText(String.valueOf(b));
                    cells.get("B").textView.setBackground(ContextCompat.getDrawable(this, getResource(b)));

                    cells.get("E").val = e;
                    cells.get("E").content = true;
                    cells.get("E").textView.setText(String.valueOf(e));
                    cells.get("E").textView.setBackground(ContextCompat.getDrawable(this, getResource(e)));

                    cells.get("H").val = h;
                    cells.get("H").content = true;
                    cells.get("H").textView.setText(String.valueOf(h));
                    cells.get("H").textView.setBackground(ContextCompat.getDrawable(this, getResource(h)));

                    int loc2 =  rand.nextInt(3)+1;

                    switch (loc2)
                    {
                        case 1:
                        {
                            cells.get("A").val = a;
                            cells.get("A").content = true;
                            cells.get("A").textView.setText(String.valueOf(a));
                            cells.get("A").textView.setBackground(ContextCompat.getDrawable(this, getResource(a)));

                            int loc3 =  rand.nextInt(2)+1;

                            switch (loc3)
                            {
                                case 1:
                                {
                                    cells.get("F").val = f;
                                    cells.get("F").content = true;
                                    cells.get("F").textView.setText(String.valueOf(f));
                                    cells.get("F").textView.setBackground(ContextCompat.getDrawable(this, getResource(f)));

                                    break;
                                }
                                case 2:
                                {
                                    cells.get("I").val = i;
                                    cells.get("I").content = true;
                                    cells.get("I").textView.setText(String.valueOf(i));
                                    cells.get("I").textView.setBackground(ContextCompat.getDrawable(this, getResource(i)));

                                    break;
                                }
                            }
                            break;
                        }
                        case 2:
                        {
                            cells.get("D").val = d;
                            cells.get("D").content = true;
                            cells.get("D").textView.setText(String.valueOf(d));
                            cells.get("D").textView.setBackground(ContextCompat.getDrawable(this, getResource(d)));
                            int loc3 =  rand.nextInt(2)+1;

                            switch (loc3)
                            {
                                case 1:
                                {
                                    cells.get("C").val = c;
                                    cells.get("C").content = true;
                                    cells.get("C").textView.setText(String.valueOf(c));
                                    cells.get("C").textView.setBackground(ContextCompat.getDrawable(this, getResource(c)));

                                    break;
                                }
                                case 2:
                                {
                                    cells.get("I").val = i;
                                    cells.get("I").content = true;
                                    cells.get("I").textView.setText(String.valueOf(i));
                                    cells.get("I").textView.setBackground(ContextCompat.getDrawable(this, getResource(i)));
                                    break;
                                }
                            }
                            break;
                        }
                        case 3:
                        {
                            cells.get("G").val = g;
                            cells.get("G").content = true;
                            cells.get("G").textView.setText(String.valueOf(g));
                            cells.get("G").textView.setBackground(ContextCompat.getDrawable(this, getResource(g)));

                            int loc3 =  rand.nextInt(2)+1;

                            switch (loc3)
                            {
                                case 1:
                                {
                                    cells.get("C").val = c;
                                    cells.get("C").content = true;
                                    cells.get("C").textView.setText(String.valueOf(c));
                                    cells.get("C").textView.setBackground(ContextCompat.getDrawable(this, getResource(c)));

                                    break;
                                }
                                case 2:
                                {
                                    cells.get("F").val = f;
                                    cells.get("F").content = true;
                                    cells.get("F").textView.setText(String.valueOf(f));
                                    cells.get("F").textView.setBackground(ContextCompat.getDrawable(this, getResource(f)));
                                    break;
                                }
                            }
                            break;
                        }
                    }
                    break;
                }

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

                case 3:
                {
                    cells.get("C").val = c;
                    cells.get("C").content = true;
                    cells.get("C").textView.setText(String.valueOf(c));
                    cells.get("C").textView.setBackground(ContextCompat.getDrawable(this, getResource(c)));

                    cells.get("F").val = f;
                    cells.get("F").content = true;
                    cells.get("F").textView.setText(String.valueOf(f));
                    cells.get("F").textView.setBackground(ContextCompat.getDrawable(this, getResource(f)));

                    cells.get("I").val = i;
                    cells.get("I").content = true;
                    cells.get("I").textView.setText(String.valueOf(i));
                    cells.get("I").textView.setBackground(ContextCompat.getDrawable(this, getResource(i)));

                    int loc2 =  rand.nextInt(3)+1;

                    switch (loc2)
                    {
                        case 1:
                        {
                            cells.get("A").val = a;
                            cells.get("A").content = true;
                            cells.get("A").textView.setText(String.valueOf(a));
                            cells.get("A").textView.setBackground(ContextCompat.getDrawable(this, getResource(a)));

                            int loc3 =  rand.nextInt(2)+1;

                            switch (loc3)
                            {
                                case 1:
                                {
                                    cells.get("E").val = e;
                                    cells.get("E").content = true;
                                    cells.get("E").textView.setText(String.valueOf(e));
                                    cells.get("E").textView.setBackground(ContextCompat.getDrawable(this, getResource(e)));
                                    break;
                                }
                                case 2:
                                {
                                    cells.get("H").val = h;
                                    cells.get("H").content = true;
                                    cells.get("H").textView.setText(String.valueOf(h));
                                    cells.get("H").textView.setBackground(ContextCompat.getDrawable(this, getResource(h)));

                                    break;
                                }
                            }
                            break;
                        }
                        case 2:
                        {
                            cells.get("D").val = d;
                            cells.get("D").content = true;
                            cells.get("D").textView.setText(String.valueOf(d));
                            cells.get("D").textView.setBackground(ContextCompat.getDrawable(this, getResource(d)));
                            int loc3 =  rand.nextInt(2)+1;

                            switch (loc3)
                            {
                                case 1:
                                {
                                    cells.get("B").val = b;
                                    cells.get("B").content = true;
                                    cells.get("B").textView.setText(String.valueOf(b));
                                    cells.get("B").textView.setBackground(ContextCompat.getDrawable(this, getResource(b)));

                                    break;
                                }
                                case 2:
                                {
                                    cells.get("H").val = h;
                                    cells.get("H").content = true;
                                    cells.get("H").textView.setText(String.valueOf(h));
                                    cells.get("H").textView.setBackground(ContextCompat.getDrawable(this, getResource(h)));

                                    break;
                                }
                            }
                            break;
                        }
                        case 3:
                        {
                            cells.get("G").val = g;
                            cells.get("G").content = true;
                            cells.get("G").textView.setText(String.valueOf(g));
                            cells.get("G").textView.setBackground(ContextCompat.getDrawable(this, getResource(g)));

                            int loc3 =  rand.nextInt(2)+1;

                            switch (loc3)
                            {
                                case 1:
                                {
                                    cells.get("B").val = b;
                                    cells.get("B").content = true;
                                    cells.get("B").textView.setText(String.valueOf(b));
                                    cells.get("B").textView.setBackground(ContextCompat.getDrawable(this, getResource(b)));

                                    break;
                                }
                                case 2:
                                {
                                    cells.get("E").val = e;
                                    cells.get("E").content = true;
                                    cells.get("E").textView.setText(String.valueOf(e));
                                    cells.get("E").textView.setBackground(ContextCompat.getDrawable(this, getResource(e)));
                                    break;
                                }
                            }
                            break;
                        }
                    }
                    break;
                }


            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.select1: {
                if (selects.get("select1").content == false) {
                    selectedValue = 1;
                    FlushSelects();
                    selects.get("select1").content = true;
                    selects.get("select1").textView.setTextColor(Color.RED);
                    selects.get("select1").textView.setShadowLayer(10, 4, 4, Color.WHITE);
                } else {
                    selectedValue = 0;
                    selects.get("select1").content = false;
                    selects.get("select1").textView.setTextColor(Color.parseColor("#FFFFFF"));
                   selects.get("select1").textView.setShadowLayer(10, 4, 4, Color.BLACK);
                }
                break;
            }
            case R.id.select2: {
                if (selects.get("select2").content == false) {
                    selectedValue = 2;
                    FlushSelects();
                    selects.get("select2").content = true;
                    selects.get("select2").textView.setTextColor(Color.RED);
                    selects.get("select2").textView.setShadowLayer(10, 4, 4, Color.WHITE);
                } else {
                    selectedValue = 0;
                    selects.get("select2").content = false;
                    selects.get("select2").textView.setTextColor(Color.parseColor("#FFFFFF"));
                    selects.get("select2").textView.setShadowLayer(10, 4, 4, Color.BLACK);
                }
                break;
            }
            case R.id.select3: {
                if (selects.get("select3").content == false) {
                    selectedValue = 3;
                    FlushSelects();
                    selects.get("select3").content = true;
                    selects.get("select3").textView.setTextColor(Color.RED);
                    selects.get("select3").textView.setShadowLayer(10, 4, 4, Color.WHITE);
                } else {
                    selectedValue = 0;
                    selects.get("select3").content = false;
                    selects.get("select3").textView.setTextColor(Color.parseColor("#FFFFFF"));
                    selects.get("select3").textView.setShadowLayer(10, 4, 4, Color.BLACK);
                }
                break;
            }
            case R.id.select4: {
                if (selects.get("select4").content == false) {
                    selectedValue = 4;
                    FlushSelects();
                    selects.get("select4").content = true;
                    selects.get("select4").textView.setTextColor(Color.RED);
                    selects.get("select4").textView.setShadowLayer(10, 4, 4, Color.WHITE);
                } else {
                    selectedValue = 0;
                    selects.get("select4").content = false;
                    selects.get("select4").textView.setTextColor(Color.parseColor("#FFFFFF"));
                    selects.get("select4").textView.setShadowLayer(10, 4, 4, Color.BLACK);
                }
                break;
            }
            case R.id.select5: {
                if (selects.get("select5").content == false) {
                    selectedValue = 5;
                    FlushSelects();
                    selects.get("select5").content = true;
                    selects.get("select5").textView.setTextColor(Color.RED);
                    selects.get("select5").textView.setShadowLayer(10, 4, 4, Color.WHITE);
                } else {
                    selectedValue = 0;
                    selects.get("select5").content = false;
                    selects.get("select5").textView.setTextColor(Color.parseColor("#FFFFFF"));
                    selects.get("select5").textView.setShadowLayer(10, 4, 4, Color.BLACK);
                }
                break;
            }
            case R.id.select6: {
                if (selects.get("select6").content == false) {
                    selectedValue = 6;
                    FlushSelects();
                    selects.get("select6").content = true;
                    selects.get("select6").textView.setTextColor(Color.RED);
                    selects.get("select6").textView.setShadowLayer(10, 4, 4, Color.WHITE);
                } else {
                    selectedValue = 0;
                    selects.get("select6").content = false;
                    selects.get("select6").textView.setTextColor(Color.parseColor("#FFFFFF"));
                    selects.get("select6").textView.setShadowLayer(10, 4, 4, Color.BLACK);
                }
                break;
            }
            case R.id.select7: {
                if (selects.get("select7").content == false) {
                    selectedValue = 7;
                    FlushSelects();
                    selects.get("select7").content = true;
                    selects.get("select7").textView.setTextColor(Color.RED);
                    selects.get("select7").textView.setShadowLayer(10, 4, 4, Color.WHITE);
                } else {
                    selectedValue = 0;
                    selects.get("select7").content = false;
                    selects.get("select7").textView.setTextColor(Color.parseColor("#FFFFFF"));
                    selects.get("select7").textView.setShadowLayer(10, 4, 4, Color.BLACK);
                }
                break;
            }
            case R.id.select8: {

                if (selects.get("select8").content == false) {
                    selectedValue = 8;
                    FlushSelects();
                    selects.get("select8").content = true;
                    selects.get("select8").textView.setTextColor(Color.RED);
                    selects.get("select8").textView.setShadowLayer(10, 4, 4, Color.WHITE);
                } else {
                    selectedValue = 0;
                    selects.get("select8").content = false;
                    selects.get("select8").textView.setTextColor(Color.parseColor("#FFFFFF"));
                    selects.get("select8").textView.setShadowLayer(10, 4, 4, Color.BLACK);
                }
                break;
            }
            case R.id.select9: {
                if (selects.get("select9").content == false) {
                    selectedValue = 9;
                    FlushSelects();
                    selects.get("select9").content = true;
                    selects.get("select9").textView.setTextColor(Color.RED);
                    selects.get("select9").textView.setShadowLayer(10, 4, 4, Color.WHITE);
                } else {
                    selectedValue = 0;
                    selects.get("select9").content = false;
                    selects.get("select9").textView.setTextColor(Color.parseColor("#FFFFFF"));
                    selects.get("select9").textView.setShadowLayer(10, 4, 4, Color.BLACK);
                }
                break;
            }
            case R.id.select10: {
                if (selects.get("select10").content == false) {
                    selectedValue = 10;
                    FlushSelects();
                    selects.get("select10").content = true;
                    selects.get("select10").textView.setTextColor(Color.RED);
                    selects.get("select10").textView.setShadowLayer(10, 4, 4, Color.WHITE);
                } else {
                    selectedValue = 0;
                    selects.get("select10").content = false;
                    selects.get("select10").textView.setTextColor(Color.parseColor("#FFFFFF"));
                    selects.get("select10").textView.setShadowLayer(10, 4, 4, Color.BLACK);
                }
                break;
            }
            case R.id.cellA: {
                if (selectedValue != 0) {
                    if (cells.get("A").content == false) {
                        cells.get("A").content = true;
                        cells.get("A").val=selectedValue;
                        cells.get("A").textView.setText(String.valueOf(selectedValue));
                        cells.get("A").textView.setBackground(ContextCompat.getDrawable(this, getResource(selectedValue)));
                        selectedValue = 0;
                        FlushSelects();
                        StateCheck();
                    }
                }
                break;
            }
            case R.id.cellB: {
                if (selectedValue != 0) {
                    if (cells.get("B").content == false) {
                        cells.get("B").content=true;
                        cells.get("B").val=selectedValue;
                        cells.get("B").textView.setText(String.valueOf(selectedValue));
                        cells.get("B").textView.setBackground(ContextCompat.getDrawable(this, getResource(selectedValue)));
                        selectedValue=0;
                        FlushSelects();
                        StateCheck();
                    }
                }
                break;
            }
            case R.id.cellC: {
                if (selectedValue != 0) {
                    if (cells.get("C").content == false) {
                        cells.get("C").content=true;
                        cells.get("C").val=selectedValue;
                        cells.get("C").textView.setText(String.valueOf(selectedValue));
                        cells.get("C").textView.setBackground(ContextCompat.getDrawable(this, getResource(selectedValue)));
                        selectedValue=0;
                        FlushSelects();
                        StateCheck();
                    }
                }
                break;
            }
            case R.id.cellD: {
                if (selectedValue != 0) {
                    if (cells.get("D").content == false) {
                        cells.get("D").content=true;
                        cells.get("D").val=selectedValue;
                        cells.get("D").textView.setText(String.valueOf(selectedValue));
                        cells.get("D").textView.setBackground(ContextCompat.getDrawable(this, getResource(selectedValue)));
                        selectedValue=0;
                        FlushSelects();
                        StateCheck();
                    }
                }
                break;
            }
            case R.id.cellE: {
                if (selectedValue != 0) {
                    if (cells.get("E").content == false) {
                        cells.get("E").content=true;
                        cells.get("E").val=selectedValue;
                        cells.get("E").textView.setText(String.valueOf(selectedValue));
                        cells.get("E").textView.setBackground(ContextCompat.getDrawable(this, getResource(selectedValue)));
                        selectedValue=0;
                        FlushSelects();
                        StateCheck();
                    }
                }
                break;
            }
            case R.id.cellF: {
                if (selectedValue != 0) {
                    if (cells.get("F").content == false) {
                        cells.get("F").content=true;
                        cells.get("F").val=selectedValue;
                        cells.get("F").textView.setText(String.valueOf(selectedValue));
                        cells.get("F").textView.setBackground(ContextCompat.getDrawable(this, getResource(selectedValue)));
                        selectedValue=0;
                        FlushSelects();
                        StateCheck();
                    }
                }
                break;
            }
            case R.id.cellG: {
                if (selectedValue != 0) {
                    if (cells.get("G").content == false) {
                        cells.get("G").content=true;
                        cells.get("G").val=selectedValue;
                        cells.get("G").textView.setText(String.valueOf(selectedValue));
                        cells.get("G").textView.setBackground(ContextCompat.getDrawable(this, getResource(selectedValue)));
                        selectedValue=0;
                        FlushSelects();
                        StateCheck();
                    }
                }
                break;
            }
            case R.id.cellH: {
                if (selectedValue != 0) {
                    if (cells.get("H").content == false) {
                        cells.get("H").content=true;
                        cells.get("H").val=selectedValue;
                        cells.get("H").textView.setText(String.valueOf(selectedValue));
                        cells.get("H").textView.setBackground(ContextCompat.getDrawable(this, getResource(selectedValue)));
                        selectedValue=0;
                        FlushSelects();
                        StateCheck();
                    }
                }
                break;
            }
            case R.id.cellI: {
                if (selectedValue != 0) {
                    if (cells.get("I").content == false) {
                        cells.get("I").content=true;
                        cells.get("I").val=selectedValue;
                        cells.get("I").textView.setText(String.valueOf(selectedValue));
                        cells.get("I").textView.setBackground(ContextCompat.getDrawable(this, getResource(selectedValue)));
                        selectedValue=0;
                        FlushSelects();
                        StateCheck();
                    }
                }
                break;
            }
            case R.id.soundButton: {
                if (soundFlag==false)
                {
                    musicPlayed=soundPool.play(music, 1, 1, 0, -1, 1);
                    soundFlag=true;
                    sound.setImageResource(R.drawable.off_sound);
                }
                else if (soundFlag)
                {
                    soundPool.stop(musicPlayed);
                    soundFlag=false;
                    sound.setImageResource(R.drawable.sound);
                }
                break;
            }
            case R.id.exitButton: {
                ExitDialogue exitDialog = ExitDialogue.newInstance();
                exitDialog.show(getSupportFragmentManager(), "listDialog");
                break;
            }
            case R.id.statsButton:{


                String message="";
                for(Player cf: players.values())
                {
                    message += String.valueOf(cf.Player_Id) + "/-_-/" +
                            cf.Player_Name + "/-_-/" +
                            String.valueOf(cf.Games)+ "/-_-/" +
                            String.valueOf(cf.Wins)+ "/-_-/" +
                            String.valueOf(cf.Success)+ "/-_-/" +
                            String.valueOf(cf.Current) + "/--__--/";
                }
                Log.d("SUDOKU", "GAME send data: "+message);
               Stats statsFragment = Stats.newInstance(message);
                statsFragment.show(getSupportFragmentManager(), "listDialog");
                break;
            }
        }
    }

    @Override
    public void onExitSelected(String data) {
        Log.d("SUDOKU", "result selected: "+data);
        if (data != null)
        {
            switch (data)
            {
                case "1": {

                    break;
                }
                case "0":
                {
                    soundPool.stop(musicPlayed);
                    soundFlag=false;
                    sound.setImageResource(R.drawable.sound);
                    String resultMessage=players.get(currentPlayerName).Player_Id+"/-_-/"+
                            players.get(currentPlayerName).Player_Name+"/-_-/"+
                            players.get(currentPlayerName).Games+"/-_-/"+
                            players.get(currentPlayerName).Wins+"/-_-/"+
                            players.get(currentPlayerName).Success+"/-_-/"+
                            players.get(currentPlayerName).Current+"/-_-/";
                    Intent intent = new Intent();
                    intent.putExtra("data", resultMessage);
                    this.setResult(RESULT_OK, intent);
                    finish();
                    break;
                }
            }

        }
    }

    public int getResource(int index)
    {
        int result=0;
        switch (index) {
            case 1:
            {
                result= R.drawable.select1_template;
                break;
            }
            case 2:
            {
                result=R.drawable.select2_template;
                break;
            }
            case 3:
            {
                result= R.drawable.select3_template;
                break;
            }
            case 4:
            {
                result= R.drawable.select4_template;
                break;
            }
            case 5:
            {
                result= R.drawable.select5_template;
                break;
            }
            case 6:
            {
                result=R.drawable.select6_template;
                break;
            }
            case 7:            {
                result= R.drawable.select7_template;
                break;
            }
            case 8:
            {
                result= R.drawable.select8_template;
                break;
            }
            case 9:
            {
                result= R.drawable.select9_template;
                break;
            }
            case 10:
            {
                result= R.drawable.select10_template;
                break;
            }
        }
        return result;
    }

    public void FlushSelects()
    {
        for(Cell c: selects.values())
        {
            c.content = false;
            c.textView.setTextColor(Color.parseColor("#FFFFFF"));
           c.textView.setShadowLayer(10, 4, 4, Color.BLACK);
        }
    }

    public void FlushCells()
    {
        for(Cell c: cells.values())
        {
            c.content = false;
            c.val=0;
            c.textView.setText("");
            c.textView.setBackground(ContextCompat.getDrawable(this, R.drawable.blank_cell));
        }
    }

public void StateCheck()
{
    if (cells.get("A").content == true && cells.get("B").content == true && cells.get("C").content == true &&
            cells.get("D").content == true && cells.get("E").content == true && cells.get("F").content == true &&
            cells.get("G").content == true && cells.get("H").content == true && cells.get("I").content == true)
    {
        int sum1 = cells.get("A").val + cells.get("C").val + cells.get("B").val;
        int sum2 = cells.get("D").val + cells.get("E").val + cells.get("F").val;
        int sum3 = cells.get("G").val + cells.get("H").val + cells.get("I").val;
        int sum4 = cells.get("A").val+ cells.get("D").val+ cells.get("G").val;
        int sum5 = cells.get("B").val  + cells.get("E").val + cells.get("H").val ;
        int sum6 = cells.get("C").val + cells.get("F").val + cells.get("I").val;

        if (sum1 == sum2 && sum2 == sum3 && sum3 == sum4 && sum4 == sum5 && sum5 == sum6)
        {
            players.get(currentPlayerName).Games+=1;
            players.get(currentPlayerName).Wins+=1;
            players.get(currentPlayerName).Success=(float)players.get(currentPlayerName).Wins/players.get(currentPlayerName).Games;

            Log.d("SUDOKU", "CURRENT PLAYER AFTER GAME: "+players.get(currentPlayerName).Player_Name+" "+players.get(currentPlayerName).Games+" "+players.get(currentPlayerName).Wins+" "+players.get(currentPlayerName).Success);

            FlushCells();
            ResultDialogue resultDialog = ResultDialogue.newInstance("1");
            resultDialog.show(getSupportFragmentManager(), "listDialog");
        }
        else
        {
            players.get(currentPlayerName).Games+=1;
            players.get(currentPlayerName).Success=(float)players.get(currentPlayerName).Wins/players.get(currentPlayerName).Games;

            Log.d("SUDOKU", "CURRENT PLAYER AFTER GAME: "+players.get(currentPlayerName).Player_Name+" "+players.get(currentPlayerName).Games+" "+players.get(currentPlayerName).Wins+" "+players.get(currentPlayerName).Success);

            FlushCells();
            ResultDialogue resultDialog = ResultDialogue.newInstance("0");
            resultDialog.show(getSupportFragmentManager(), "listDialog");
        }
    }
}

    @Override
    public void onResultSelected(String data) {
        Log.d("SUDOKU", "result selected: "+data);
        if (data != null)
        {
            switch (data)
            {
                case "1": {
                   StartGame();
                    break;
                }
                case "0":
                {
                    String resultMessage=players.get(currentPlayerName).Player_Id+"/-_-/"+
                            players.get(currentPlayerName).Player_Name+"/-_-/"+
                            players.get(currentPlayerName).Games+"/-_-/"+
                            players.get(currentPlayerName).Wins+"/-_-/"+
                            players.get(currentPlayerName).Success+"/-_-/"+
                            players.get(currentPlayerName).Current+"/-_-/";
                    Intent intent = new Intent();
                    intent.putExtra("data", resultMessage);
                    this.setResult(RESULT_OK, intent);
                    finish();
                    break;
                }
            }

        }
    }

}