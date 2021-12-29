package com.androidroomfirebase.app.quickroom;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class MainActivity extends AppCompatActivity {
    TextView Name,contact,email;
    ImageView image;
    private StorageReference mStorageRef;
    private FirebaseAuth mAuth;
    private static Button logout,book;
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        Name = (TextView) findViewById(R.id.name);
        email = (TextView) findViewById(R.id.email);
        contact=(TextView)findViewById(R.id.contact);
        image = (ImageView) findViewById(R.id.image);
        final View progress = (View)findViewById(R.id.progressBar);
        logout=(Button)findViewById(R.id.logout);
        book=(Button)findViewById(R.id.book);
        progress.setVisibility(View.VISIBLE);
        final SwipeRefreshLayout swipeView = (SwipeRefreshLayout) findViewById(R.id.swipe);
        swipeView.setColorScheme(android.R.color.holo_blue_dark,
                android.R.color.holo_blue_light,
                android.R.color.holo_green_light,
                android.R.color.holo_green_light);
        swipeView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeView.setRefreshing(true);
                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeView.setRefreshing(false);
                        mStorageRef = FirebaseStorage.getInstance().getReference();
                        final long ONE_MEGABYTE = 1024 * 1024;
                        mStorageRef.child("User/" + mAuth.getCurrentUser().getUid() + ".jpg").getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                            @Override
                            public void onSuccess(byte[] bytes) {
                                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                image.setImageBitmap(bitmap);
                            }
                        });
                    }
                }, 3000);
            }
        });
        mStorageRef = FirebaseStorage.getInstance().getReference();
        final long ONE_MEGABYTE = 1024 * 1024;

        mStorageRef.child("User/" + mAuth.getCurrentUser().getUid() + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                mStorageRef.child("User/" + mAuth.getCurrentUser().getUid() + ".jpg").getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        image.setImageBitmap(bitmap);
                        progress.setVisibility(View.GONE);

                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                progress.setVisibility(View.GONE);
            }
        });
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, UploadImage.class));
            }
        });
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference dbRef = database.getReference().child("Societies");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    if(dsp.child("email").exists()) {
                        if (dsp.child("email").getValue().toString().equalsIgnoreCase(mAuth.getCurrentUser().getEmail())) {
                            Name.setText(dsp.getKey().toString());
                            contact.setText(dsp.child("contact").getValue().toString());
                            email.setText(mAuth.getCurrentUser().getEmail().toString());
                        }
                    }}
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }}
        );
        book.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("name",Name.getText().toString());
                bundle.putString("contact",contact.getText().toString());
                bundle.putString("email",email.getText().toString());
                Intent intent = new Intent(MainActivity.this, BlockList.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                finishAfterTransition();
                mAuth.signOut();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });
    }
}
