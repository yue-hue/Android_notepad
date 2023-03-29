package com.example.notepad_1;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class CreateNote extends AppCompatActivity {
    private EditText name_edit, note_edit;
    DBManager dbManager;
    private String file_note_text, file_note_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);

        dbManager = new DBManager(this);

        name_edit = findViewById(R.id.name_edit);
        note_edit = findViewById(R.id.note_text_edit);

        Intent intent = getIntent();
        if (intent != null) {
            file_note_text = intent.getStringExtra("file_note_text");
            file_note_name = intent.getStringExtra("file_note_name");

            name_edit.setText(file_note_name);
            note_edit.setText(file_note_text);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        dbManager.openDB();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        String note_date = df.format(Calendar.getInstance().getTime());

        String note_name;
        if (TextUtils.isEmpty(name_edit.getText().toString())) {
            note_name = "Text note " + note_date;
        }
        else { note_name = name_edit.getText().toString(); }

        if (!TextUtils.isEmpty(note_edit.getText().toString())) {
            dbManager.insertDB(note_name, note_date, note_edit.getText().toString());
            finish();
        }
        else {
            AlertDialog.Builder alert_builder = new AlertDialog.Builder(CreateNote.this);
            alert_builder.setMessage("Do you want save an empty note?");
            alert_builder.setCancelable(true);

            alert_builder.setPositiveButton(
                    "Yes",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dbManager.insertDB(note_name, note_date, note_edit.getText().toString());
                            dialog.cancel();
                            finish();
                        }
                    });

            alert_builder.setNegativeButton(
                    "No",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
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
    protected void onDestroy() {
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
                SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
                String note_date = df.format(Calendar.getInstance().getTime());

                String note_name;
                if (TextUtils.isEmpty(name_edit.getText().toString())) {
                    note_name = "Text note " + note_date;
                }
                else { note_name = name_edit.getText().toString(); }

                dbManager.insertDB(note_name, note_date, note_edit.getText().toString());

                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}