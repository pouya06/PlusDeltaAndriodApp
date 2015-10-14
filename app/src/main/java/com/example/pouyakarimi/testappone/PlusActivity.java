package com.example.pouyakarimi.testappone;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import com.example.pouyakarimi.testappone.database.NoteDBHandler;
import com.example.pouyakarimi.testappone.objects.Note;

import java.util.ArrayList;
import java.util.logging.Logger;

public class PlusActivity extends AppCompatActivity {

    private Button next;
    private Button save;
    private TableLayout tableLayout;
    private TableRow tableRow;
    private EditText editText;
    private Note note;
    private NoteDBHandler noteDBHandler;
    private Toast toast;
    private ContentValues values;
    int counter = 0;

    private static final String SAVED_MESSAGE = "Saved!";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plus);
        next = (Button) findViewById(R.id.newPlus);
        save = (Button) findViewById(R.id.saveRow);
        tableLayout = (TableLayout) findViewById(R.id.tableLayout1);
        tableLayout.setColumnStretchable(0, true);
        noteDBHandler = new NoteDBHandler(this, null, null, 1);
        note = new Note();
        values = new ContentValues();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_plus, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void nextRow(View v) {
//        note = new Note();
//        note.setText(editText.getText().toString());
//        note.setIsItPlus(true);
//        noteDBHandler.addNewRow(note);
        tableRow = new TableRow(this);

        editText = new EditText(this);
        editText.requestFocus();
        editText.setId(counter ++);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                toast.makeText(getApplicationContext(), String.valueOf(editText.getId()), Toast.LENGTH_LONG).show();
                values.put(String.valueOf(editText.getId()), s.toString());
            }
        });

        tableRow.addView(editText);
        tableLayout.addView(tableRow);
    }

    public void saveRow(View v) {

        toast.makeText(this, SAVED_MESSAGE, Toast.LENGTH_LONG).show();

    }
}
