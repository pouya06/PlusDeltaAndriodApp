package com.example.pouyakarimi.testappone;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.pouyakarimi.testappone.objects.Note;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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


    public void openNewPage(View v) {
        Intent intent = new Intent(getApplicationContext(), PlusActivity.class);
        startActivity(intent);
    }

    public void viewAll(View v) {
        Intent intent = new Intent(getApplicationContext(), ViewAll.class);
        startActivity(intent);
    }

    public void openDeltaPage(View view) {
        Intent intent = new Intent(getApplicationContext(), DeltaActivity.class);
        startActivity(intent);
    }
}
