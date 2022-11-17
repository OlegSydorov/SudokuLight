package com.example.sudokulight;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

interface ExitFragmentListener
{
    public abstract void onExitSelected(String message);
}
public class ExitDialogue extends DialogFragment implements View.OnClickListener
{

    TextView yes;
    TextView no;
    ExitFragmentListener efListener;

    public ExitDialogue() {
        // Required empty public constructor
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Проверка на реализацию слушателя и занесение в fr2Listener
        if(activity instanceof ExitFragmentListener)
            efListener = (ExitFragmentListener) activity;

        Log.d("SUDOKU", "exitFragment onAttach");
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("SUDOKU", "exitFragment onCreate");
    }

    public static ExitDialogue newInstance()
    {
        ExitDialogue myFragment = new ExitDialogue();
        Bundle args = new Bundle();
        args.putString("data", null);
        myFragment.setArguments(args);
        return myFragment;
    }

    // метод запускается для формирования разметки на фрагменте
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        Log.d("SUDOKU", "exitFragment onCreateView");

        View v = inflater.inflate(R.layout.fragment_exit_dialogue, null);

        yes=(TextView)v.findViewById(R.id.yesView);
       yes.setOnClickListener(this);
        no=(TextView)v.findViewById(R.id.noView);
        no.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.yesView: {
                if(efListener != null) {
                    efListener.onExitSelected("0");
                    dismiss();
                    break;
                }
            }
            case R.id.noView:
            {
                if(efListener != null) {
                    efListener.onExitSelected("1");
                    dismiss();
                    break;
                }
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
}