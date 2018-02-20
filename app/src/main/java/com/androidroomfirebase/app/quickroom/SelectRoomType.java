package com.androidroomfirebase.app.quickroom;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SelectRoomType extends AppCompatActivity {

    TextView desireroom;
    Button eblock,fblock,tanaudi,tcomplex,mainaudi,gblock,cos,labs,sign;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_room_type);

        desireroom=(TextView)findViewById(R.id.textView5);
        eblock=(Button)findViewById(R.id.button);
        fblock=(Button)findViewById(R.id.button2);
        tanaudi=(Button)findViewById(R.id.button3);
        tcomplex=(Button)findViewById(R.id.button4);
        mainaudi=(Button)findViewById(R.id.button5);
        gblock=(Button)findViewById(R.id.button6);
        cos=(Button)findViewById(R.id.button7);
        labs=(Button)findViewById(R.id.button8);
        sign=(Button)findViewById(R.id.button9);

        eblock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SelectRoomType.this, Eblock.class));
            }
        });






    }
}
