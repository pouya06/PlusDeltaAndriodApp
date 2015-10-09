package com.example.pouyakarimi.testappone;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.pouyakarimi.testappone.objects.Notes;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Notes> notesList = new ArrayList<>();
    public  final static String SER_KEY = "com.easyinfogeek.objectPass.ser";

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

    @Override
    public void onResume(){
        super.onResume();
        if (notesList.get(0) != null){
            System.out.println("list" + notesList.get(0).getText());
        }
    }

    public void openNewPage(View v){
        Intent intent = new Intent(getApplicationContext(), PlusActivity.class);
        Bundle mBundle = new Bundle();
        mBundle.putSerializable(SER_KEY, notesList);
        intent.putExtras(mBundle);
        startActivity(intent);
    }

    public void viewAll(View v){
        Intent intent = new Intent(getApplicationContext(), ViewAll.class);
        Bundle mBundle = new Bundle();
        mBundle.putSerializable(SER_KEY, notesList);
        intent.putExtras(mBundle);
        startActivity(intent);
    }
}
