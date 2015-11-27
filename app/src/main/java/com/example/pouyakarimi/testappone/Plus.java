package com.example.pouyakarimi.testappone;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pouyakarimi.testappone.adapters.NoteArrayAdapter;
import com.example.pouyakarimi.testappone.database.DBHandler;
import com.example.pouyakarimi.testappone.objects.Note;
import com.example.pouyakarimi.testappone.objects.User;
import com.example.pouyakarimi.testappone.statics.StaticMessages;
import com.example.pouyakarimi.testappone.utils.EmailUtil;

import java.util.ArrayList;
import java.util.List;

public class Plus extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private EditText userInput;
    private Note note;
    private DBHandler DBHandler;
    private Toast toast;
    private List<Note> notes = new ArrayList<>();
    private NoteArrayAdapter noteArrayAdapter;
    private TextView userInputTitle;
    private ListView plusListView;
    private TextView primaryEmail;
    private TextView primaryUser;
    private String[] bodyOfEmail;
    private TextView plusNoDataMsg;
    private Boolean isThereAOne = false;
    private AdapterView.OnItemClickListener listViewListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Note noteForAction = (Note) parent.getItemAtPosition(position);
            openDialogActionDelta(noteForAction);
        }
    };
    private ArrayList<User> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plus2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = View.inflate(this, R.layout.drawer_header, null);
        navigationView.addHeaderView(header);

        note = new Note();
        DBHandler = new DBHandler(this, null, null, 1);

        noteArrayAdapter = new NoteArrayAdapter(this,0,notes);

        primaryEmail = (TextView) header.findViewById(R.id.nav_primary_email);
        primaryUser = (TextView) header.findViewById(R.id.nav_primary_user);

        getPrimaryUserFromDb();

        plusListView = (ListView) findViewById(R.id.plusListView);
        plusListView.setAdapter(noteArrayAdapter);
        plusListView.setOnItemClickListener(listViewListener);

        IsThereAUser();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.plus, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_users) {
            Intent intent = new Intent(getApplicationContext(), Users.class);
            startActivity(intent);
        } else if (id == R.id.nav_plus) {

        } else if (id == R.id.nav_delta) {
            Intent intent = new Intent(getApplicationContext(), Delta.class);
            startActivity(intent);

        } else if (id == R.id.nav_view_all) {
            Intent intent = new Intent(getApplicationContext(), All.class);
            startActivity(intent);

        } else if (id == R.id.nav_delete_all_item) {
            deleteAllDialog(true);

        } else if (id == R.id.nav_delete_all_users) {
            deleteAllDialog(false);

        } else if (id == R.id.nav_send) {
            if(isThereAOne){
                startActivity(EmailUtil.sendEmail(DBHandler.notesArray(1), DBHandler.notesArray(0), DBHandler.listOfUsers()));
            }else{
                alertNoUser();
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshNoteList();
    }

    public void openDialogDelta(View view) {
        openDialogHelper(view, "PLUS ", "#95FC94", "Add", "Cancel", true, null);
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
                            }
                        })
                .setNeutralButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                Toast.makeText(Plus.this, StaticMessages.CANCELED_MESSAGE, Toast.LENGTH_SHORT).show();
                            }
                        })
                .setNegativeButton("Edit",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                LayoutInflater factory = getLayoutInflater();
                                View view = factory.inflate(R.layout.prompts, null);
                                openDialogHelper(view, "Editing ... ", "#95FC94", "Edit", "Cancel", false, noteForAction);
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
                                Toast.makeText(Plus.this, StaticMessages.DELETED_MESSAGE, Toast.LENGTH_SHORT).show();
                            }
                        })
                .setNeutralButton("No",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                Toast.makeText(Plus.this, StaticMessages.CANCELED_MESSAGE, Toast.LENGTH_SHORT).show();
                            }
                        });
        deleteAlertDialogBuilder.show();
    }

    private void deleteAllDialog(final boolean isNotes) {
        AlertDialog.Builder deleteAlertDialogBuilder = new AlertDialog.Builder(this);
        if (isNotes) {
            deleteAlertDialogBuilder.setTitle("Deleting All Items ...");
        } else {
            deleteAlertDialogBuilder.setTitle("Deleting All Users ...");
        }
        deleteAlertDialogBuilder.setMessage("Are you sure ???");
        deleteAlertDialogBuilder.setIcon(android.R.drawable.ic_menu_delete);
        deleteAlertDialogBuilder.setCancelable(true)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                if (isNotes) {
                                    DBHandler.deleteAllNotes();
                                    refreshNoteList();
                                } else {
                                    DBHandler.deleteAllUser();
                                    getPrimaryUserFromDb();
                                }
                                Toast.makeText(Plus.this, StaticMessages.DELETED_MESSAGE, Toast.LENGTH_SHORT).show();
                            }
                        })
                .setNeutralButton("No",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                Toast.makeText(Plus.this, StaticMessages.CANCELED_MESSAGE, Toast.LENGTH_SHORT).show();
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
                                    if (userInput.getText().toString().isEmpty()) {
                                        Toast.makeText(Plus.this, StaticMessages.VALID_ENTRY_MESSAGE, Toast.LENGTH_SHORT).show();
                                    } else {
                                        save();
                                    }
                                else
                                    edit(noteForAction);
                            }
                        })
                .setNeutralButton(buttonNegative,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                Toast.makeText(Plus.this, StaticMessages.CANCELED_MESSAGE, Toast.LENGTH_SHORT).show();
                            }
                        });
        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();
    }

    private void save() {
        note.setText(userInput.getText().toString());
        note.setIsItPlus(true);
        DBHandler.addNewNoteRow(note);
        refreshNoteList();
        Toast.makeText(Plus.this, StaticMessages.SAVED_MESSAGE, Toast.LENGTH_SHORT).show();
    }

    private void edit(Note noteForAction) {
        noteForAction.setText(userInput.getText().toString());
        noteForAction.setIsItPlus(true);
        DBHandler.updateNoteRow(noteForAction);
        refreshNoteList();
        Toast.makeText(Plus.this, StaticMessages.EDITED_MESSAGE, Toast.LENGTH_SHORT).show();
    }

    private void checkTheListView() {
        plusNoDataMsg = (TextView) findViewById(R.id.emptyPlusTextView);

        if (plusListView.getCount() == 0) {
            plusNoDataMsg.setVisibility(View.VISIBLE);
        } else {
            plusNoDataMsg.setVisibility(View.INVISIBLE);
        }
    }

    private void refreshNoteList() {
        AsyncTask<Void,Void,List<Note>> asyncTask = new AsyncTask<Void, Void, List<Note>>() {
            @Override
            protected List<Note> doInBackground(Void... params) {
                return DBHandler.notesArray(1);
            }

            @Override
            protected void onPostExecute(List<Note> notesList) {
                notes.clear();
                notes.addAll(notesList);
                noteArrayAdapter.notifyDataSetChanged();
                checkTheListView();
            }
        };
        asyncTask.execute();

    }

    private void alertNoUser(){
        AlertDialog.Builder noUserMessage = new AlertDialog.Builder(this);
        noUserMessage.setTitle("Warning!!");
        noUserMessage.setMessage("You need to have at least one user");
        noUserMessage.setIcon(android.R.drawable.stat_sys_warning);
        noUserMessage.setCancelable(true)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        noUserMessage.show();
    }

    private void IsThereAUser() {

        AsyncTask<Void, Void, Boolean> asyncTask = new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Void... params) {
                return isThereAOne = DBHandler.isThereExistAUser();
            }

        };
        asyncTask.execute();
    }

    private void getPrimaryUserFromDb() {
        AsyncTask<Void, Void, User> asyncTask = new AsyncTask<Void, Void, User>() {

            @Override
            protected User doInBackground(Void... params) {
                return DBHandler.primaryUser();
            }

            @Override
            protected void onPostExecute(User user) {
                if (user.getEmail() != null) {
                    primaryEmail.setText(user.getEmail());
                    primaryUser.setText(user.getName());
                    primaryEmail.setTextColor(Color.WHITE);
                } else {
                    primaryEmail.setText("Please set up your primary user!");
                    primaryUser.setText("Plus & Delta");
                    primaryEmail.setTextColor(Color.RED);
                }
            }
        };
        asyncTask.execute();
    }

}
