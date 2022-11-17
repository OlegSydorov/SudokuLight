package com.example.sudokulight;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener,
                    ModeFragmentListener,
                    NameFragmentListener
{
 TextView startView;
 Intent gameIntent;
 final MainActivity activity = this;
 PlayersDB playersDB;
 Player currentPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startView = (TextView) findViewById(R.id.startView);
        startView.setOnClickListener(this);

        playersDB = new PlayersDB(this);
      //  playersDB.getWritableDatabase();
        playersDB.open();
        gameIntent = new Intent(this, Game.class);

        currentPlayer=playersDB.fetchCurrentPlayer();
        Log.d("SUDOKU", currentPlayer.toString());
        Log.d("SUDOKU", String.valueOf(playersDB.fetchAllPlayers().getCount()));
    }

    @Override
    public void onClick(View v) {
        ModeDialogue modeDialog = ModeDialogue.newInstance(currentPlayer.Player_Name);
        modeDialog.show(getSupportFragmentManager(), "listDialog");
    }

    @Override
    public void onModeSelected(String data) {
        Log.d("SUDOKU", "DETAILS - mode selected: "+data);
        if (data != null)
        {
            switch (data)
            {
                case "1": {
                    String message="";
                    Cursor cf = playersDB.fetchAllPlayers();
                    if (cf.getCount()>0)
                    {
                        cf.moveToFirst();
                        message += cf.getString(1) + "/-_-/" + cf.getString(2) + "/-_-/" + cf.getString(3)+ "/-_-/" + cf.getString(4)+ "/-_-/" + cf.getString(5)+ "/-_-/" + cf.getString(6) + "/--__--/";
                        while (cf.moveToNext())
                        {
                            message += cf.getString(1) + "/-_-/" + cf.getString(2) + "/-_-/" + cf.getString(3) + "/-_-/" + cf.getString(4)+ "/-_-/" + cf.getString(5)+ "/-_-/" + cf.getString(6) + "/--__--/";
                        }
                    }
                    Log.d("SUDOKU", "MAIN send data: "+message);
//                    String message=currentPlayer.Player_Id+"/-_-/"+
//                                    currentPlayer.Player_Name+"/-_-/"+
//                                    currentPlayer.Games+"/-_-/"+
//                                    currentPlayer.Wins+"/-_-/"+
//                                    currentPlayer.Success+"/-_-/"+
//                                    currentPlayer.Current+"/-_-/";
                    gameIntent.putExtra("data", message);
                    Log.i("AddNew", "none");
                    startActivityForResult(gameIntent, 1);
                    break;
                }
                case "0":
                {
                   NameDialogue newNameDialog = NameDialogue.newInstance();
                    newNameDialog.show(getSupportFragmentManager(), "listDialog");
                    break;
                }
            }

        }
    }

    @Override
    public void onNameSelected(String data) {
        if (playersDB.checkName(data)==0)
        {
            playersDB.addPlayer(data, 0, 0, 0, 1);
            playersDB.editPlayer(currentPlayer.Player_Id, currentPlayer.Player_Name, currentPlayer.Games, currentPlayer.Wins, currentPlayer.Success, 0);
            //currentPlayer=new Player(playersDB.checkName(data), data, 0, 0, 0, 1);
        }
        else
        {
            playersDB.editPlayer(currentPlayer.Player_Id, currentPlayer.Player_Name, currentPlayer.Games, currentPlayer.Wins, currentPlayer.Success, 0);
            Player tempPlayer=playersDB.fetchPlayer(playersDB.checkName(data));
            playersDB.editPlayer(tempPlayer.Player_Id, tempPlayer.Player_Name, tempPlayer.Games, tempPlayer.Wins, tempPlayer.Success, 1);
            //currentPlayer=playersDB.fetchCurrentPlayer();
        }
        String message="";
        Cursor cf = playersDB.fetchAllPlayers();
        if (cf.getCount()>0)
        {
            cf.moveToFirst();
            message += cf.getString(1) + "/-_-/" + cf.getString(2) + "/-_-/" + cf.getString(3)+ "/-_-/" + cf.getString(4)+ "/-_-/" + cf.getString(5)+ "/-_-/" + cf.getString(6) + "/--__--/";
            while (cf.moveToNext())
            {
                message += cf.getString(1) + "/-_-/" + cf.getString(2) + "/-_-/" + cf.getString(3) + "/-_-/" + cf.getString(4)+ "/-_-/" + cf.getString(5)+ "/-_-/" + cf.getString(6) + "/--__--/";
            }
        }

//        String message=currentPlayer.Player_Id+"/-_-/"+
//                currentPlayer.Player_Name+"/-_-/"+
//                currentPlayer.Games+"/-_-/"+
//                currentPlayer.Wins+"/-_-/"+
//                currentPlayer.Success+"/-_-/"+
//                currentPlayer.Current+"/-_-/";
        gameIntent.putExtra("data", message);
        Log.i("AddNew", "none");
        startActivityForResult(gameIntent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
        {
            if (requestCode == 1)
            {
                if (data != null)
                {
                    String resultData = data.getStringExtra("data");
                    Log.d("SUDOKU", "Main activity+CURRENT PLAYER: "+resultData);

                    String[] params2 = resultData.split("/-_-/");
                    Log.d("SUDOKU","Game  CURRENT PLAYER: "+params2[0]+" "+params2[1]+" "+params2[2]+" "+params2[3]+" "+params2[4]+" "+params2[5]);
                    playersDB.editPlayer(Integer.parseInt(params2[0]), params2[1], Integer.parseInt(params2[2]), Integer.parseInt(params2[3]), Float.parseFloat(params2[4]), Integer.parseInt(params2[5]));
                }
            }
        }
    }
}