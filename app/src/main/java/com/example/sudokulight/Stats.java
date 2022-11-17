package com.example.sudokulight;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;


public class Stats extends DialogFragment implements View.OnClickListener
//        ,LoaderManager.LoaderCallbacks<Cursor>
{

    TextView back;
    RecyclerView statistics;
    RecyclerViewPlayerAdapter dataAdapter;
    PlayersDB db;

    ArrayList<Player> players;

    public Stats() {

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Проверка на реализацию слушателя и занесение в fr2Listener

        Log.d("SUDOKU", "statsFragment onAttach");
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        players=new ArrayList<Player>();
        Log.d("SUDOKU", "statsFragment onCreate");
        if (getArguments() != null) {
            String inputData = getArguments().getString("data");

            if (inputData.length()>0)
            {
                String[] params1=inputData.split("/--__--/");
                Log.d("SUDOKU",String.valueOf(params1.length));
                if (params1.length>0)
                {
                    for (int i=0; i<params1.length; i++) {
                        String[] params2 = params1[i].split("/-_-/");
                        Log.d("SUDOKU", "Game activity+PLAYER: " + params2[0] + " " + params2[1] + " " + params2[2] + " " + params2[3] + " " + params2[4] + " " + params2[5]);
                        players.add(new Player(Integer.parseInt(params2[0]),
                                params2[1],
                                Integer.parseInt(params2[2]),
                                Integer.parseInt(params2[3]),
                                Float.parseFloat(params2[4]),
                                Integer.parseInt(params2[5])));
                    }
                }
            }
        }
    }

    public static Stats newInstance(String data)
    {
        Stats myFragment = new Stats();
        Log.d("SUDOKU", "STATS receive data: "+data);
        Bundle args = new Bundle();
        args.putString("data", data);
        myFragment.setArguments(args);
        return myFragment;
    }

    // метод запускается для формирования разметки на фрагменте
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        Log.d("SUDOKU", "statsFragment onCreateView");

        View v = inflater.inflate(R.layout.fragment_stats, null);

        back=(TextView)v.findViewById(R.id.returnView);
        back.setOnClickListener(this);
        statistics=(RecyclerView)v.findViewById(R.id.statsView);
        displayRecyclerView();

        return v;
    }

    private void displayRecyclerView() {

        db=new PlayersDB(getActivity());
        db.open();

        GridLayoutManager layoutManager = new GridLayoutManager(statistics.getContext(), 1);
        layoutManager.setOrientation(GridLayoutManager.VERTICAL);

        statistics.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(statistics.getContext(),
                layoutManager.getOrientation());
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(statistics.getContext(), R.drawable.divider));
        statistics.addItemDecoration(dividerItemDecoration);

        dataAdapter = new RecyclerViewPlayerAdapter(getActivity(), players);
        statistics.setAdapter(dataAdapter);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.returnView: {
                    dismiss();
                    break;
            }
        }
    }

    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    // окно закрывается по нажатию за пределами окна
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        Toast.makeText(getActivity(), "No selection made", Toast.LENGTH_SHORT).show();
    }

//    @NonNull
//    @Override
//    public Loader onCreateLoader(int id, @Nullable Bundle bundle) {
//        // Получить параметры из контейнера
//        String order = bundle.getString("order");
//        if(order == null)
//            order="Success";
//
//        // Создание лоадера
//        return new CustomLoader(getActivity(), db);
//    }
//
//    @Override
//    public void onLoadFinished(@NonNull Loader loader, Cursor data) {
//        dataAdapter.swapCursor((Cursor)data);
//        dataAdapter.notifyDataSetChanged();
//    }
//
//    @Override
//    public void onLoaderReset(@NonNull Loader loader) {
//        dataAdapter.swapCursor(null);
//        dataAdapter.notifyDataSetChanged();
//    }
}