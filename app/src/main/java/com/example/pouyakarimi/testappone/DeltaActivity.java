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
import com.example.pouyakarimi.testappone.database.DBHandler;
import com.example.pouyakarimi.testappone.objects.Note;
import com.example.pouyakarimi.testappone.statics.StaticMessages;

import java.util.ArrayList;
import java.util.List;

public class DeltaActivity extends AppCompatActivity {

    private EditText userInput;
    private Note note;
    private DBHandler DBHandler;
    private Toast toast;
    private List<Note> notes = new ArrayList<>();
    private NoteArrayAdapter noteArrayAdapter;
    private TextView userInputTitle;
    private ListView deltaListView;
    private AdapterView.OnItemClickListener listViewListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Note noteForAction = (Note) parent.getItemAtPosition(position);
            TextView tv = (TextView) view.findViewById(R.id.noteText);
            String messege = String.valueOf(tv.getText() + "Id:\t" + note.getId());
            Toast.makeText(DeltaActivity.this, messege, Toast.LENGTH_LONG).show();
            openDialogActionDelta(noteForAction);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delta);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        note = new Note();
        DBHandler = new DBHandler(this, null, null, 1);

        noteArrayAdapter = new NoteArrayAdapter(this,0,notes);

        deltaListView = (ListView) findViewById(R.id.deltaListView);
        deltaListView.setAdapter(noteArrayAdapter);
        deltaListView.setOnItemClickListener(listViewListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshNoteList();
    }

    public void openDialogDelta(View view) {
        openDialogHelper(view, "DELTA ", "#FFDB4900", "Add", "Cancel", true, null);
    }

    private void openDialogActionDelta(final Note noteForAction) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Changing Item ...");
        alertDialogBuilder.setMessage("Do You Wanna Change This Entry?");
        alertDialogBuilder.setIcon(android.R.drawable.ic_menu_save);
        alertDialogBuilder.setCancelable(true)
                .setPositiveButton("Delete",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                onDeleteADialog(noteForAction);
                                Toast.makeText(DeltaActivity.this, StaticMessages.SAVED_MESSAGE, Toast.LENGTH_LONG).show();
                            }
                        })
                .setNeutralButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                Toast.makeText(DeltaActivity.this, StaticMessages.CANCELED_MESSAGE, Toast.LENGTH_LONG).show();
                            }
                        })
                .setNegativeButton("Edit",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                LayoutInflater factory = getLayoutInflater();
                                View view = factory.inflate(R.layout.prompts, null);
                                openDialogHelper(view, "Editing ... ", "#4D7AF4", "Edit", "Cancel", false, noteForAction);
                            }
                        });
        alertDialogBuilder.show();

    }

    private void onDeleteADialog(final Note noteForAction) {
        AlertDialog.Builder deleteAlertDialogBuilder = new AlertDialog.Builder(this);
        deleteAlertDialogBuilder.setTitle("Deleting Item ...");
        deleteAlertDialogBuilder.setMessage("Are you sure ???");
        deleteAlertDialogBuilder.setIcon(android.R.drawable.ic_menu_delete);
        deleteAlertDialogBuilder.setCancelable(true)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                DBHandler.deleteNoteRow(noteForAction);
                                refreshNoteList();
                                Toast.makeText(DeltaActivity.this, StaticMessages.DELETED_MESSAGE, Toast.LENGTH_LONG).show();
                            }
                        })
                .setNeutralButton("No",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                Toast.makeText(DeltaActivity.this, StaticMessages.CANCELED_MESSAGE, Toast.LENGTH_LONG).show();
                            }
                        });
        deleteAlertDialogBuilder.show();
    }

    private void openDialogHelper(View view, String title, String color, String buttonPositive, String buttonNegative, final boolean isSave, final Note noteForAction){
        LayoutInflater li = LayoutInflater.from(this);
        final View promptsView = li.inflate(R.layout.prompts, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setView(promptsView);

        userInputTitle = (TextView) promptsView.findViewById(R.id.textViewDialogUserInput);
        userInputTitle.setText(title);
        userInputTitle.setTextColor(Color.parseColor(color));

        userInput = (EditText) promptsView.findViewById(R.id.editTextDialogUserInput);
        if(!isSave){
            userInput.setText(noteForAction.getText());
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
                                    edit(noteForAction);
                            }
                        })
                .setNeutralButton(buttonNegative,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                Toast.makeText(DeltaActivity.this, StaticMessages.CANCELED_MESSAGE, Toast.LENGTH_LONG).show();
                            }
                        });
        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();
    }

    private void save() {
        note.setText(userInput.getText().toString());
        note.setIsItPlus(false);
        DBHandler.addNewNoteRow(note);
        refreshNoteList();
        Toast.makeText(DeltaActivity.this, StaticMessages.SAVED_MESSAGE, Toast.LENGTH_LONG).show();
    }

    private void edit(Note noteForAction) {
        noteForAction.setText(userInput.getText().toString());
        noteForAction.setIsItPlus(false);
        DBHandler.updateNoteRow(noteForAction);
        refreshNoteList();
        Toast.makeText(DeltaActivity.this, StaticMessages.EDITED_MESSAGE, Toast.LENGTH_LONG).show();
    }


    private void refreshNoteList() {
        AsyncTask<Void,Void,List<Note>> asyncTask = new AsyncTask<Void, Void, List<Note>>() {
            @Override
            protected List<Note> doInBackground(Void... params) {
                return DBHandler.notesArray(0);
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

    public void goUp(View view){

    }

    public void goDown(View view){

    }
}
