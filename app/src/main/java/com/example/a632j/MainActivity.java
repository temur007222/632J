package com.example.a632j;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton addNoteBtn;
     EditText mEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Realm.init(getApplicationContext());
        Realm realm = Realm.getDefaultInstance();

        addNoteBtn = findViewById(R.id.fab);
        addNoteBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View view) {

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.activity_dialog, null);
                mEmail = mView.findViewById(R.id.etEmail);
                mBuilder.setPositiveButton("SAVE", (dialogInterface, i) -> {

                });
                mBuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();

                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String title = mEmail.getText().toString();
                        long createdTime = System.currentTimeMillis();

                        realm.beginTransaction();
                        Note note = realm.createObject(Note.class);
                        note.setTitle(title);
                        note.setCreatedTime(createdTime);
                        realm.commitTransaction();
                        Toast.makeText(getApplicationContext(),"Note saved",Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
            }
        });

        Realm.init(getApplicationContext());
        Realm realm1 = Realm.getDefaultInstance();

        RealmResults<Note> notesList;
        notesList = realm1.where(Note.class).findAll().sort("createdTime", Sort.DESCENDING);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        MyAdapter myAdapter = new MyAdapter(getApplicationContext(),notesList);
        recyclerView.setAdapter(myAdapter);

        notesList.addChangeListener(new RealmChangeListener<RealmResults<Note>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChange(@NonNull RealmResults<Note> notes) {
                myAdapter.notifyDataSetChanged();
            }
        });

    }
}