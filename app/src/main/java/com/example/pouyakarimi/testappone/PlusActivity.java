package com.example.pouyakarimi.testappone;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.pouyakarimi.testappone.adapters.NoteArrayAdapter;
import com.example.pouyakarimi.testappone.database.NoteDBHandler;
import com.example.pouyakarimi.testappone.objects.Note;

import java.util.ArrayList;
import java.util.List;

public class PlusActivity extends AppCompatActivity {

//    private Button next;
//    private Button save;
//    private TableLayout tableLayout;
//    private TableRow tableRow;
//    private EditText editText;
    private Note note;
    private NoteDBHandler noteDBHandler;
    private Toast toast;
//    private ContentValues values;
//    int counter = 0;
//
    private static final String SAVED_MESSAGE = "Saved!";
    private static final String CANCELED_MESSAGE = "Canceled!";

    private EditText userInput;
    private List<Note> notes = new ArrayList<>();
    private NoteArrayAdapter noteArrayAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plus);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.plusButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
//        next = (Button) findViewById(R.id.newPlus);
//        save = (Button) findViewById(R.id.saveRow);
//        tableLayout = (TableLayout) findViewById(R.id.tableLayout1);
//        tableLayout.setColumnStretchable(0, true);
        note = new Note();
        noteDBHandler = new NoteDBHandler(this, null, null, 1);
        //rrayList<String> list = new ArrayList<>(); //must remove
        //ListAdapter listAdapter = new ArrayAdapter<Note>(this, android.R.layout.simple_list_item_1, noteDBHandler.notesArray());
        noteArrayAdapter = new NoteArrayAdapter(this,0,notes);
        ListView plusListView = (ListView) findViewById(R.id.plusListView);
        plusListView.setAdapter(noteArrayAdapter);

    }


    @Override
    public void onResume()
    {
        super.onResume();
        refreshNoteList();
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

    public void openDialog(View view) {
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.prompts, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setView(promptsView);

        userInput = (EditText) promptsView.findViewById(R.id.editTextDialogUserInput);

        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Add",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                note.setText(userInput.getText().toString());
                                note.setIsItPlus(true);
                                noteDBHandler.addNewRow(note);
                                toast.makeText(PlusActivity.this, SAVED_MESSAGE, Toast.LENGTH_LONG).show();
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                toast.makeText(PlusActivity.this, CANCELED_MESSAGE, Toast.LENGTH_LONG).show();
                            }
                        });
        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();
    }



    private void refreshNoteList()
    {
        AsyncTask<Void,Void,List<Note>> asyncTask = new AsyncTask<Void, Void, List<Note>>() {
            @Override
            protected List<Note> doInBackground(Void... params) {
                return noteDBHandler.notesArray(1);
            }


            @Override
            protected void onPostExecute(List<Note> notesList)
            {
                notes.clear();
                notes.addAll(notesList);
                noteArrayAdapter.notifyDataSetChanged();
            }
        };
        asyncTask.execute();


    }


//    public void nextRow(View v) {
//
//        tableRow = new TableRow(this);
//
//        editText = new EditText(this);
//        editText.requestFocus();
//        editText.setId(counter ++);
//        editText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                toast.makeText(getApplicationContext(), String.valueOf(editText.getId()), Toast.LENGTH_LONG).show();
//                values.put(String.valueOf(editText.getId()), s.toString());
//            }
//        });
//
//        tableRow.addView(editText);
//        tableLayout.addView(tableRow);
//    }
//
//    public void saveRow(View v) {
//
//        toast.makeText(this, SAVED_MESSAGE, Toast.LENGTH_LONG).show();
//
//    }
}
