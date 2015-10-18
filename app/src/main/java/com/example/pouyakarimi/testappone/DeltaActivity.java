package com.example.pouyakarimi.testappone;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pouyakarimi.testappone.adapters.NoteArrayAdapter;
import com.example.pouyakarimi.testappone.database.NoteDBHandler;
import com.example.pouyakarimi.testappone.objects.Note;

import java.util.ArrayList;
import java.util.List;

public class DeltaActivity extends AppCompatActivity {

    private EditText userInput;
    private Note note;
    private NoteDBHandler noteDBHandler;
    private Toast toast;
    private List<Note> notes = new ArrayList<>();
    private NoteArrayAdapter noteArrayAdapter;
    private TextView userInputTitle;
    private ListView plusListView;


    private static final String SAVED_MESSAGE = "Saved!";
    private static final String EDITED_MESSAGE = "Edited!";
    private static final String CANCELED_MESSAGE = "Canceled!";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delta);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        note = new Note();
        noteDBHandler = new NoteDBHandler(this, null, null, 1);
        noteArrayAdapter = new NoteArrayAdapter(this,0,notes);
        plusListView = (ListView) findViewById(R.id.deltaListView);
        plusListView.setAdapter(noteArrayAdapter);
        openListViewActionDialog();
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshNoteList();
    }

    public void openDialogDelta(View view) {
        openDialogHelper(view, "DELTA ", "#FFDB4900", "Add", "Cancel", true);
    }

    private void openListViewActionDialog() {
        plusListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tv = (TextView) view.findViewById(R.id.noteText);
                String messege = String.valueOf(tv.getId()) + "\t" + tv.getText();
                toast.makeText(DeltaActivity.this, messege, Toast.LENGTH_LONG).show();
                openDialogActionDelta(view);
            }
        });
    }

    private void openDialogActionDelta(View view) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Changing Item ...");
        alertDialogBuilder.setMessage("Do You Wanna Change This Entry?");
        alertDialogBuilder.setIcon(android.R.drawable.ic_menu_save);
        alertDialogBuilder.setCancelable(true)
                .setPositiveButton("Delete",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                toast.makeText(DeltaActivity.this, SAVED_MESSAGE, Toast.LENGTH_LONG).show();
                            }
                        })
                .setNeutralButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                toast.makeText(DeltaActivity.this, CANCELED_MESSAGE, Toast.LENGTH_LONG).show();
                            }
                        })
                .setNegativeButton("Edit",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                LayoutInflater factory = getLayoutInflater();
                                View view = factory.inflate(R.layout.prompts, null);
                                openDialogHelper(view, "Editing ... ", "#4D7AF4", "Edit", "Cancel", false);
                            }
                        });
        alertDialogBuilder.show();

    }

    private void openDialogHelper(View view, String title, String color, String buttonPositive, String buttonNegative, final boolean isSave){
        LayoutInflater li = LayoutInflater.from(this);
        final View promptsView = li.inflate(R.layout.prompts, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setView(promptsView);

        userInputTitle = (TextView) promptsView.findViewById(R.id.textViewDialogUserInput);
        userInputTitle.setText(title);
        userInputTitle.setTextColor(Color.parseColor(color));

        userInput = (EditText) promptsView.findViewById(R.id.editTextDialogUserInput);
        if(!isSave){
            TextView savedText = (TextView) view.findViewById(R.id.noteText);
            //toast.makeText(DeltaActivity.this, savedText.getText(), Toast.LENGTH_LONG).show();
            //userInput.setText(savedText.getText().toString());
        }

        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton(buttonPositive,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                if (isSave)
                                    save();
                                else
                                    edit();
                            }
                        })
                .setNeutralButton(buttonNegative,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                toast.makeText(DeltaActivity.this, CANCELED_MESSAGE, Toast.LENGTH_LONG).show();
                            }
                        });
        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();
    }

    private void save() {
        note.setText(userInput.getText().toString());
        note.setIsItPlus(false);
        noteDBHandler.addNewRow(note);
        refreshNoteList();
        toast.makeText(DeltaActivity.this, SAVED_MESSAGE, Toast.LENGTH_LONG).show();
    }

    private void edit() {
        toast.makeText(DeltaActivity.this, EDITED_MESSAGE, Toast.LENGTH_LONG).show();
    }


    private void refreshNoteList() {
        AsyncTask<Void,Void,List<Note>> asyncTask = new AsyncTask<Void, Void, List<Note>>() {
            @Override
            protected List<Note> doInBackground(Void... params) {
                return noteDBHandler.notesArray(0);
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
}
