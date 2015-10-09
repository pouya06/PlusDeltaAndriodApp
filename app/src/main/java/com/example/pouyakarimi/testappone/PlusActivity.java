package com.example.pouyakarimi.testappone;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.pouyakarimi.testappone.objects.Notes;

import java.util.ArrayList;

public class PlusActivity extends AppCompatActivity {

    Button next;
    TableLayout tableLayout;
    TableRow tableRow;
    EditText editText;
    Notes notes;
    ArrayList<Notes> notesList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plus);
        next = (Button) findViewById(R.id.newPlus);
        tableLayout = (TableLayout) findViewById(R.id.tableLayout1);
        tableLayout.setColumnStretchable(0, true);
        notesList = (ArrayList) getIntent().getSerializableExtra(MainActivity.SER_KEY);

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
        notes = new Notes();
        editText = new EditText(this);
        notes.setText(editText.getText().toString());
        notes.setIsItPlus(true);
        notesList.add(notes);
        tableRow.addView(editText);
        tableLayout.addView(tableRow);
    }
}
