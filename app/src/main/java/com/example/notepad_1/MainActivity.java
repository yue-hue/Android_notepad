package com.example.notepad_1;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {
    public ArrayList<Note> notes = new ArrayList<Note>();
    private ArrayList<Note> findNotes = new ArrayList<Note>();
    private DBManager dbManager;
    private EditText search_edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbManager = new DBManager(this);
        dbManager.openDB();

        search_edit = findViewById(R.id.search_name);

        RecyclerView recyclerView = findViewById(R.id.list);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        notes = dbManager.getAllDB();

        NoteAdapter.OnNoteClickListener noteClickListener = new NoteAdapter.OnNoteClickListener() {
            @Override
            public void onNoteClick(Note note, int position) {
                int id = note.getNoteId();
                Intent intent = new Intent(getApplicationContext(), OpenNote.class);
                intent.putExtra("_id", id);
                startActivity(intent);
            }
        };

        NoteAdapter.OnNoteLongClickListener noteLongClickListener = new NoteAdapter.OnNoteLongClickListener() {
            @Override
            public void onNoteLongClick(Note note, int position) {
                int note_id = note.getNoteId();

                AlertDialog.Builder alert_builder = new AlertDialog.Builder(MainActivity.this);
                alert_builder.setMessage("Do you want delete this note?");
                alert_builder.setCancelable(true);

                alert_builder.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dbManager.deleteDB(note_id);
                                dialog.cancel();
                                finish();
                                startActivity(getIntent());
                            }
                        });

                alert_builder.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                finish();
                                startActivity(getIntent());
                            }
                        });

                alert_builder.setCancelable(true);

                AlertDialog alert_dialog = alert_builder.create();
                alert_dialog.show();
            }
        };

        NoteAdapter adapter = new NoteAdapter(this, notes, noteClickListener, noteLongClickListener);
        recyclerView.setAdapter(adapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), CreateNote.class));
            }
        });

        search_edit.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String search_text = search_edit.getText().toString();
                if (!search_text.isEmpty()) {//.contains("\n")) {
                    findNotes = dbManager.searchDB(search_text.substring(0, search_text.length() - 1));
                    notes = findNotes;
                    NoteAdapter adapter = new NoteAdapter(MainActivity.this, notes, noteClickListener, noteLongClickListener);
                    recyclerView.setAdapter(adapter);
                }
                else {
                    notes = dbManager.getAllDB();
                    NoteAdapter adapter = new NoteAdapter(MainActivity.this, notes, noteClickListener, noteLongClickListener);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @Override
    protected void onRestart() {
        super.onRestart();

        finish();
        startActivity(getIntent());

        notes = dbManager.getAllDB();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id) {
            case R.id.settings_open: {
                // do what you want)
            }
            case R.id.settings_exit: {
                finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        dbManager.closeDB();
    }
}