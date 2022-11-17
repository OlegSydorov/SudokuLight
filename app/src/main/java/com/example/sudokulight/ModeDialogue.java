package com.example.sudokulight;

import android.app.Activity;
import android.app.Fragment;
import android.content.DialogInterface;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;
interface ModeFragmentListener
{
    public abstract void onModeSelected(String message);
}
public class ModeDialogue  extends DialogFragment implements View.OnClickListener
    {
        TextView continue_mode;
        TextView new_mode;
        ModeFragmentListener mfListener;

        public ModeDialogue() {
            // Required empty public constructor
        }


        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);

            // Проверка на реализацию слушателя и занесение в fr2Listener
            if(activity instanceof ModeFragmentListener)
                mfListener = (ModeFragmentListener) activity;

            Log.d("SUDOKU", "modeFragment onAttach");
        }

        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            Log.d("SUDOKU", "modeFragment onCreate");
        }

        public static ModeDialogue newInstance(String currentPlayer)
        {
            ModeDialogue myFragment = new ModeDialogue();

            // создание контейнера для передачи параметров
            Bundle args = new Bundle();
            args.putString("data", currentPlayer);
            Log.d("SUDOKU", "newInstance: "+currentPlayer);
            // передача параметров в созданный фрагмент
            myFragment.setArguments(args);

            // возвратить созданный фрагмент
            return myFragment;
        }

        // метод запускается для формирования разметки на фрагменте
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
        {
            Log.d("SUDOKU", "modeFragment onCreateView");

            View v = inflater.inflate(R.layout.fragment_start, null);

            continue_mode=(TextView)v.findViewById(R.id.textView);
            continue_mode.setOnClickListener(this);
            new_mode=(TextView)v.findViewById(R.id.textView2);
            new_mode.setOnClickListener(this);


            String param = this.getArguments().getString("data");
            Log.d("SUDOKU", param);
            if(param!=null)
            {
                if(param.length()>0) {
                    continue_mode.setText("Continue as "+param);
                }
            }
            return v;
        }
        @Override
        public void onClick(View v) {
            switch (v.getId())
            {
                case R.id.textView: {
                    if(mfListener != null) {
                        mfListener.onModeSelected("1");
                        dismiss();
                        break;
                    }
                }
                case R.id.textView2:
                {
                    if(mfListener != null) {
                        mfListener.onModeSelected("0");
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
