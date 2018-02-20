package com.androidroomfirebase.app.quickroom;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Eblock extends AppCompatActivity {
Button e101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eblock);
        e101=(Button)findViewById(R.id.button13);
        e101.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Eblock.this, Room.class));
            }
        });
    }
}
