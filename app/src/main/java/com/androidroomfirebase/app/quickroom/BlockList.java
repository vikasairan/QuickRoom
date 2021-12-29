package com.androidroomfirebase.app.quickroom;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
public class BlockList extends AppCompatActivity {
    DatabaseReference dref;
    ListView listview;
    ArrayList<String> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_block_list);
        listview = (ListView) findViewById(R.id.listview);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list, list);
        listview.setAdapter(adapter);
        dref = FirebaseDatabase.getInstance().getReference();
        dref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                        if(dsp.getRef().getParent().getKey().equalsIgnoreCase("Room Type")) {
                            list.add(dsp.getKey());
                            adapter.notifyDataSetChanged();
                        }
                    }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                list.remove(dataSnapshot.getValue(String.class));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               final String type = (String) listview.getItemAtPosition(i);
                Bundle extras = getIntent().getExtras();
                String name = null;
                String email = null;
                String contact = null;
                if (extras != null) {
                    name = extras.getString("name");
                    email = extras.getString("email");
                    contact = extras.getString("contact");
                }
                Bundle bundle=new Bundle();
                bundle.putString("type", type);
                bundle.putString("name", name);
                bundle.putString("email", email);
                bundle.putString("contact",contact);
                Intent intent = new Intent(BlockList.this, RoomList.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
}