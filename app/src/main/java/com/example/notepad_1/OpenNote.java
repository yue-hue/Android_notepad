package com.example.notepad_1;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class OpenNote extends AppCompatActivity {
    DBManager dbManager;
    EditText name_edit, note_edit;
    int note_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_note);

        name_edit = findViewById(R.id.name_edit);
        note_edit = findViewById(R.id.note_edit);

        Intent intent = getIntent();
        note_id = intent.getIntExtra("_id", 0);

        dbManager = new DBManager(this);
        dbManager.openDB();

        Note note = dbManager.openNoteDB(note_id);

        name_edit.setText(note.getNoteName());
        note_edit.setText(note.getNoteText());
    }

    @Override
    public void onBackPressed() {
        SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        String note_date = df.format(Calendar.getInstance().getTime());

        String note_name;
        if (TextUtils.isEmpty(name_edit.getText().toString())) {
            note_name = "Text note " + note_date;
        }
        else { note_name = name_edit.getText().toString(); }

        if (!TextUtils.isEmpty(note_edit.getText().toString())) {
            dbManager.updateDB(note_name, note_date, note_edit.getText().toString(), note_id);
            finish();
        }
        else {
            AlertDialog.Builder alert_builder = new AlertDialog.Builder(OpenNote.this);
            alert_builder.setMessage("Do you want save an empty note?");
            alert_builder.setCancelable(true);

            alert_builder.setPositiveButton(
                    "Yes",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dbManager.updateDB(note_name, note_date, note_edit.getText().toString(), note_id);
                            dialog.cancel();
                            finish();
                        }
                    });

            alert_builder.setNegativeButton(
                    "Delete",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dbManager.deleteDB(note_id);
                            dialog.cancel();
                            finish();
                        }
                    });

            alert_builder.setCancelable(true);

            AlertDialog alert_dialog = alert_builder.create();
            alert_dialog.show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        dbManager.closeDB();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id) {
            case R.id.settings_save: {
                try {
                    SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
                    String note_date = df.format(Calendar.getInstance().getTime());

                    String note_name;
                    if (TextUtils.isEmpty(name_edit.getText().toString())) {
                        note_name = "Text note " + note_date;
                    }
                    else { note_name = name_edit.getText().toString(); }

                    File newFile = new File("NP1/" + note_name + ".txt");

                    if (newFile.createNewFile()) {

                        FileOutputStream file = openFileOutput(note_name + ".txt", MODE_PRIVATE);
                        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(file);

                        outputStreamWriter.write(note_edit.getText().toString());

                        outputStreamWriter.flush();
                        outputStreamWriter.close();
                        Toast.makeText(OpenNote.this, "Successfully saved", Toast.LENGTH_SHORT)
                                .show();
                    } else {
                        Toast.makeText(OpenNote.this, "Filed create file", Toast.LENGTH_SHORT)
                                .show();
                    }

                } catch (IOException e) {
                    Toast.makeText(OpenNote.this, e.getMessage(), Toast.LENGTH_SHORT)
                            .show();
                }
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}