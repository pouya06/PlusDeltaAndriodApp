package com.example.pouyakarimi.testappone;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pouyakarimi.testappone.adapters.UserArrayAdaptor;
import com.example.pouyakarimi.testappone.database.DBHandler;
import com.example.pouyakarimi.testappone.objects.User;
import com.example.pouyakarimi.testappone.statics.StaticMessages;

import java.util.ArrayList;
import java.util.List;

public class Users extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TextView textViewUser;
    private EditText userNameInput;
    private EditText userEmailInput;
    private Switch primarySwitch;
    private DBHandler userDBHandler;
    private UserArrayAdaptor userArrayAdaptor;
    private User user;
    private List<User> users = new ArrayList<>();
    private ListView userListView;
    private AdapterView.OnItemClickListener listViewListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            User userForAction = (User) parent.getItemAtPosition(position);
            onDeleteADialog(userForAction);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.users);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        user = new User();
        userDBHandler = new DBHandler(this, null, null, 1);

        userArrayAdaptor = new UserArrayAdaptor(this, 0, users);
        userListView = (ListView) findViewById(R.id.userListView);
        userListView.setAdapter(userArrayAdaptor);
        userListView.setOnItemClickListener(listViewListener);

    }

    private void onDeleteADialog(final User userForAction) {
        AlertDialog.Builder deleteAlertDialogBuilder = new AlertDialog.Builder(this);
        deleteAlertDialogBuilder.setTitle("Deleting Item ...");
        deleteAlertDialogBuilder.setMessage("Are you sure ???");
        deleteAlertDialogBuilder.setIcon(android.R.drawable.ic_menu_delete);
        deleteAlertDialogBuilder.setCancelable(true)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                userDBHandler.deleteUserRow(userForAction);
                                refreshUserList();
                                Toast.makeText(Users.this, StaticMessages.DELETED_MESSAGE, Toast.LENGTH_LONG).show();
                            }
                        })
                .setNeutralButton("No",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                Toast.makeText(Users.this, StaticMessages.CANCELED_MESSAGE, Toast.LENGTH_LONG).show();
                            }
                        });
        deleteAlertDialogBuilder.show();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.users, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_users) {

        } else if (id == R.id.nav_plus) {
            Intent intent = new Intent(getApplicationContext(), Plus.class);
            startActivity(intent);
        } else if (id == R.id.nav_delta) {
            Intent intent = new Intent(getApplicationContext(), DeltaActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshUserList();
    }

    public void openUserDialog(View view) {
        LayoutInflater li = LayoutInflater.from(this);
        final View promptsView = li.inflate(R.layout.userprompts, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setView(promptsView);

        textViewUser = (TextView) promptsView.findViewById(R.id.textViewUserDialogUserInput);
        textViewUser.setText("User");

        userNameInput = (EditText) promptsView.findViewById(R.id.editTextNameDialogUserInput);
        userEmailInput = (EditText) promptsView.findViewById(R.id.editTextEmailDialogUserInput);
        primarySwitch = (Switch) promptsView.findViewById(R.id.primarySwitch);

        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Add",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                user.setName(userNameInput.getText().toString());
                                user.setEmail(userEmailInput.getText().toString());
                                user.setisItPrimary(primarySwitch.isChecked());
                                userDBHandler.addNewUserRow(user);
                                refreshUserList();
                                Toast.makeText(Users.this, StaticMessages.SAVED_MESSAGE, Toast.LENGTH_LONG).show();
                            }
                        })
                .setNeutralButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                Toast.makeText(Users.this, StaticMessages.CANCELED_MESSAGE, Toast.LENGTH_LONG).show();
                            }
                        });
        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();
    }


    private void refreshUserList() {
        AsyncTask<Void, Void, List<User>> asyncTask = new AsyncTask<Void, Void, List<User>>() {
            @Override
            protected List<User> doInBackground(Void... params) {
                return userDBHandler.listOfUsers();
            }


            @Override
            protected void onPostExecute(List<User> userList) {
                users.clear();
                users.addAll(userList);
                userArrayAdaptor.notifyDataSetChanged();
            }
        };
        asyncTask.execute();

    }
}
