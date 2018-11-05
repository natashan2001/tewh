package com.example.natasha.mynotes2;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashSet;

public class MainActivity extends AppCompatActivity {
    static ArrayList<String> notes= new ArrayList<>();
    static ArrayAdapter adapter;
    SharedPreferences sharedPreferences;
    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        MenuInflater menuInflater= getMenuInflater();
        menuInflater.inflate(R.menu.add_note_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView listView= (ListView) findViewById(R.id.listView);
        sharedPreferences= getApplicationContext()
                .getSharedPreferences("com.example.natasha.mynotes2", Context.MODE_PRIVATE);
        HashSet<String> set= (HashSet<String>) sharedPreferences.getStringSet("notes", null);
        if (set==null){
            notes.add("add new note");
        } else{
            notes=new ArrayList(set);
        }
        adapter= new ArrayAdapter(this, android.R.layout.simple_list_item_1, notes);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent= new Intent (MainActivity.this, NotesActivity.class);
                intent.putExtra("noteId", position);
                startActivity(intent);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final int itemToRemove= position;
                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(android.R.drawable.alert_dark_frame)
                        .setTitle("are you sure?")
                        .setMessage("Do you want to delete this note?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                notes.remove(itemToRemove);
                                adapter.notifyDataSetChanged();
                                SharedPreferences sharedPreferences= getApplicationContext()
                                        .getSharedPreferences("com.example.natasha.mynotes2", Context.MODE_PRIVATE);
                                HashSet<String> set= new HashSet<>(MainActivity.notes);
                                sharedPreferences.edit().putStringSet("notes", set). apply();
                            }
                        }).setNegativeButton("No",null)
                        .show();
                return true;

            }
        });
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.add_note){
            Intent intent= new Intent(this, NotesActivity.class);
            startActivity(intent);
            return true;
        }
        return false;
    }
}
