package com.example.sudokulight;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.example.sudokulight.ModeDialogue;
import com.example.sudokulight.ModeFragmentListener;
import com.example.sudokulight.R;
interface NameFragmentListener
{
    public abstract void onNameSelected(String message);
}

public class NameDialogue extends DialogFragment implements View.OnClickListener
{

    EditText name_text_view;
    TextView ok;
   NameFragmentListener nfListener;

    public NameDialogue() {
        // Required empty public constructor
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Проверка на реализацию слушателя и занесение в fr2Listener
        if(activity instanceof NameFragmentListener)
            nfListener = (NameFragmentListener) activity;

        Log.d("SUDOKU", "modeFragment onAttach");
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("SUDOKU", "modeFragment onCreate");
    }

    public static NameDialogue newInstance()
    {
        NameDialogue myFragment = new NameDialogue();

        // создание контейнера для передачи параметров
        Bundle args = new Bundle();
        args.putString("data", "");

        // передача параметров в созданный фрагмент
        myFragment.setArguments(args);

        // возвратить созданный фрагмент
        return myFragment;
    }

    // метод запускается для формирования разметки на фрагменте
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        Log.d("SUDOKU", "modeFragment onCreateView");

        View v = inflater.inflate(R.layout.fragment_name, null);

        name_text_view=(EditText)v.findViewById(R.id.nameTextView);
        ok=(TextView)v.findViewById(R.id.okButton);
        ok.setOnClickListener(this);
        return v;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.okButton: {
                if (name_text_view.getText().toString().length() > 0) {
                    if(nfListener != null) {
                        nfListener.onNameSelected(name_text_view.getText().toString());
                        dismiss();

                    }

                } else {
                    Toast.makeText(getActivity(), "No name entered!", Toast.LENGTH_SHORT).show();
                }
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
        Toast.makeText(getActivity(), "No image selected", Toast.LENGTH_SHORT).show();
    }
}