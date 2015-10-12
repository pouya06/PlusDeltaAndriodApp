package com.example.pouyakarimi.testappone;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.example.pouyakarimi.testappone.objects.Note;

import java.util.ArrayList;

public class PlusActivity extends AppCompatActivity {

    Button next;
    TableLayout tableLayout;
    TableRow tableRow;
    EditText editText;
    Note note;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plus);
        next = (Button) findViewById(R.id.newPlus);
        tableLayout = (TableLayout) findViewById(R.id.tableLayout1);
        tableLayout.setColumnStretchable(0, true);

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

    public void nextRow(View v){
        tableRow = new TableRow(this);
//        textView = new TextView(this);
//        textView.setText("hello");
//        textView.setTextSize(15);
//        textView.setGravity(Gravity.CENTER);
        note = new Note();
        editText = new EditText(this);
        editText.setFocusable(true);
        editText.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
        note.setText(editText.getText().toString());
        note.setIsItPlus(true);
        tableRow.addView(editText);
        tableLayout.addView(tableRow);
    }
}
