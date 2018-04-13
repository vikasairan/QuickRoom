package com.androidroomfirebase.app.quickroom;

import android.app.DatePickerDialog;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Calendar;

public class Room extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    int finishval=0;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
        Button btn=(Button)findViewById(R.id.datepicker);
        Button book=(Button)findViewById(R.id.book);
        final TimePicker intime=(TimePicker)findViewById(R.id.intime);
        final TimePicker outtime=(TimePicker)findViewById(R.id.outtime);
        intime.setIs24HourView(true);
        outtime.setIs24HourView(true);
        intime.setCurrentHour(12);
        intime.setMinute(0);
        outtime.setCurrentHour(12);
        outtime.setMinute(0);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datepicker= new DatePickerFragment();
                datepicker.show(getSupportFragmentManager(),"Date Picker");
            }
        });
        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView txt=(TextView)findViewById(R.id.date);
                final String currentDateString=txt.getText().toString();
                if(currentDateString=="")
                {
                    Toast.makeText(Room.this, "Pick A Date", Toast.LENGTH_SHORT).show();
                }
                else {
                    Bundle extras = getIntent().getExtras();
                    String type = null;
                    String room = null;
                    if (extras != null) {
                        type = extras.getString("type");
                        room = extras.getString("room");
                    }

                    final FirebaseDatabase database = FirebaseDatabase.getInstance();
                    final DatabaseReference myRef = database.getReference().child("Room Type").child(type).child(room);
                    final String[] t = {""};
                    myRef.addValueEventListener(new ValueEventListener() {@Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.child(currentDateString).exists()) {
                                t[0] = (String) dataSnapshot.child(currentDateString).getValue();}

                                TimePicker intime=(TimePicker)findViewById(R.id.intime);
                                TimePicker outtime=(TimePicker)findViewById(R.id.outtime);
                                int inhour =intime.getHour();
                                int outhour =outtime.getHour();

                                if(inhour<outhour) {
                                    int flag = 0;
                                    if (finishval == 0) {
                                        for (int i = 0; i < outhour - inhour; i++) {
                                            if (t[0].contains(String.valueOf(inhour + i))) {
                                                flag = 1;
                                                Toast.makeText(Room.this, "Room Not Available At Selected time! Please Choose other time", Toast.LENGTH_SHORT).show();
                                                break;
                                            }
                                        }

                                        if (flag == 0) {
                                            for (int i = 0; i < outhour - inhour; i++) {
                                                t[0] = t[0].concat(String.valueOf(inhour + i));
                                                t[0] = t[0].concat(" ");
                                            }
                                            myRef.child(currentDateString).setValue(t[0]);
                                            Toast.makeText(Room.this, "Room Booking Request Sent", Toast.LENGTH_SHORT).show();
                                            finish();
                                            finishval = 1;
                                        }
                                    }
                                }
                                else
                                {
                                    Toast.makeText(Room.this, "Please Choose Accurate time", Toast.LENGTH_SHORT).show();
                                }
                            }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });

                }}});}

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c=Calendar.getInstance();
        c.set(Calendar.YEAR,year);
        c.set(Calendar.MONTH,month);
        c.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        String currentDateString= DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        TextView txt=(TextView)findViewById(R.id.date);
        txt.setText(currentDateString);

    }
}
