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
import android.view.MenuItem;
import android.view.View;
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

public class All extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DBHandler DBHandler;
    private TextView primaryEmail;
    private TextView primaryUser;
    private ListView viewAllPlus;
    private ListView viewAllDelta;
    private TextView plusNoDataMsg;
    private TextView deltaNoDataMsg;
    private NoteArrayAdapter plusNoteArrayAdapter;
    private NoteArrayAdapter deltaNoteArrayAdapter;
    private List<Note> plusNotes = new ArrayList<>();
    private List<Note> deltaNotes = new ArrayList<>();
    private Boolean isThereAOne = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_all_acivity);
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

        DBHandler = new DBHandler(this, null, null, 1);

        primaryEmail = (TextView) header.findViewById(R.id.nav_primary_email);
        primaryUser = (TextView) header.findViewById(R.id.nav_primary_user);

        getPrimaryUserFromDb();

        plusNoteArrayAdapter = new NoteArrayAdapter(this, 0, plusNotes);
        deltaNoteArrayAdapter = new NoteArrayAdapter(this, 0, deltaNotes);

        viewAllPlus = (ListView) findViewById(R.id.viewAllPlusListView);
        viewAllPlus.setAdapter(plusNoteArrayAdapter);
        viewAllPlus.setClickable(false);

        viewAllDelta = (ListView) findViewById(R.id.viewAllDeltaListView);
        viewAllDelta.setAdapter(deltaNoteArrayAdapter);
        viewAllDelta.setClickable(false);

        refreshPlusNoteList();
        refreshDeltaNoteList();
        IsThereAUser();

    }

    private void checkTheListView() {
        plusNoDataMsg = (TextView) findViewById(R.id.emptyViewAllPlusTextView);
        deltaNoDataMsg = (TextView) findViewById(R.id.emptyViewAllDeltaTextView);

        if (viewAllPlus.getCount() == 0) {
            plusNoDataMsg.setVisibility(View.VISIBLE);
        } else {
            plusNoDataMsg.setVisibility(View.INVISIBLE);
        }

        if (viewAllDelta.getCount() == 0) {
            deltaNoDataMsg.setVisibility(View.VISIBLE);
        } else {
            deltaNoDataMsg.setVisibility(View.INVISIBLE);
        }
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
//        getMenuInflater().inflate(R.menu.view_all, menu);
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
            Intent intent = new Intent(getApplicationContext(), Plus.class);
            startActivity(intent);

        } else if (id == R.id.nav_delta) {
            Intent intent = new Intent(getApplicationContext(), Delta.class);
            startActivity(intent);

        } else if (id == R.id.nav_view_all) {

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

    private void refreshPlusNoteList() {
        AsyncTask<Void, Void, List<Note>> asyncTask = new AsyncTask<Void, Void, List<Note>>() {
            @Override
            protected List<Note> doInBackground(Void... params) {
                return DBHandler.notesArray(1);
            }


            @Override
            protected void onPostExecute(List<Note> notesList) {
                plusNotes.clear();
                plusNotes.addAll(notesList);
                plusNoteArrayAdapter.notifyDataSetChanged();
                checkTheListView();
            }
        };
        asyncTask.execute();

    }

    private void refreshDeltaNoteList() {
        AsyncTask<Void, Void, List<Note>> asyncTask = new AsyncTask<Void, Void, List<Note>>() {
            @Override
            protected List<Note> doInBackground(Void... params) {
                return DBHandler.notesArray(0);
            }


            @Override
            protected void onPostExecute(List<Note> notesList) {
                deltaNotes.clear();
                deltaNotes.addAll(notesList);
                deltaNoteArrayAdapter.notifyDataSetChanged();
                checkTheListView();
            }
        };
        asyncTask.execute();

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
                                    refreshPlusNoteList();
                                    refreshDeltaNoteList();
                                } else {
                                    DBHandler.deleteAllUser();
                                    getPrimaryUserFromDb();
                                }

                                Toast.makeText(All.this, StaticMessages.DELETED_MESSAGE, Toast.LENGTH_SHORT).show();
                            }
                        })
                .setNeutralButton("No",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                Toast.makeText(All.this, StaticMessages.CANCELED_MESSAGE, Toast.LENGTH_SHORT).show();
                            }
                        });
        deleteAlertDialogBuilder.show();
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
                    primaryEmail.setText("Please set up your users");
                    primaryUser.setText("Plus & Delta");
                    primaryEmail.setTextColor(Color.RED);
                }
            }
        };
        asyncTask.execute();
    }
}
