package com.example.sudokulight;

import android.widget.TextView;

public class Cell {
   public String name="";
    public Boolean content=false;
    public int val;
    public TextView textView;

    public Cell(String name, Boolean select, int val, TextView view )
    {
     this.name=name;
     this.textView=view;
     this.content=select;
     this.val=val;

    }
}
