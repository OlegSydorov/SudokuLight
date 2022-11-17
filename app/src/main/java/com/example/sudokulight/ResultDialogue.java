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

interface ResultFragmentListener
{
    public abstract void onResultSelected(String message);
}
public class ResultDialogue extends DialogFragment implements View.OnClickListener
{

    TextView goOn;
    TextView exit;
    TextView message;
   ResultFragmentListener rfListener;

    public ResultDialogue() {
        // Required empty public constructor
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Проверка на реализацию слушателя и занесение в fr2Listener
        if(activity instanceof ResultFragmentListener)
            rfListener = (ResultFragmentListener) activity;

        Log.d("SUDOKU", "modeFragment onAttach");
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("SUDOKU", "modeFragment onCreate");
    }

    public static ResultDialogue newInstance(String index)
    {
        ResultDialogue myFragment = new ResultDialogue();
        Bundle args = new Bundle();
        args.putString("data", index);
        myFragment.setArguments(args);
        return myFragment;
    }

    // метод запускается для формирования разметки на фрагменте
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        Log.d("SUDOKU", "resultFragment onCreateView");

        View v = inflater.inflate(R.layout.fragment_result_dialogue, null);

        goOn=(TextView)v.findViewById(R.id.continueView);
        goOn.setOnClickListener(this);
        exit=(TextView)v.findViewById(R.id.exitView);
        exit.setOnClickListener(this);
      message=(TextView)v.findViewById(R.id.messageView);


        String param = this.getArguments().getString("data");
        Log.d("SUDOKU", param);
        if(param!=null)
        {
            if(param.length()>0) {
                if (param=="1")
                {
                    message.setText("Congratulations! You won!");
                }
                else if (param=="0")
                {
                    message.setText("You lost!");
                }
            }
        }
        return v;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.continueView: {
                if(rfListener != null) {
                    rfListener.onResultSelected("1");
                    dismiss();
                    break;
                }
            }
            case R.id.exitView:
            {
                if(rfListener != null) {
                    rfListener.onResultSelected("0");
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
