package com.example.savetosqlite;

/*
    Created by: fajar on 13/10/19.
*/

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ListDataActivity extends AppCompatActivity {

    private static final String TAG = "ListDataActivity";

    DatabaseHelper mDatabaseHelper;

    private ListView mListView;

    private Button btnAdd;
    //private Button btnRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_layout);

        mListView = findViewById(R.id.list_view);
        mDatabaseHelper = new DatabaseHelper(this);
        btnAdd = findViewById(R.id.btn_add_list_layout);
        //btnRefresh = findViewById(R.id.btn_refresh_list_layout);

        populateListView();

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListDataActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        /*btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                populateListView();
            }
        });*/
    }

    private void populateListView() {
        Log.d(TAG, "populateListView: Displaying data in the ListView");

        //get the data and append to the list
        Cursor data = mDatabaseHelper.getData();
        ArrayList<String> listData = new ArrayList<>();
        while(data.moveToNext()){
            //get the value from the database in column 1 (COL2)
            //then add it to the ArrayList
            listData.add(data.getString(1));
        }
        //create the list adapter and set the adapter
        final ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listData);
        mListView.setAdapter(adapter);

        //set on itemClickListener to the list view
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                //grab the name. it is object type, so we should convert it to string
                String name = adapterView.getItemAtPosition(position).toString();
                Log.d(TAG, "onItemClick: You clicked on " + name);

                //create cursor object and pass the name
                Cursor data = mDatabaseHelper.getItemID(name);  //get the id associated with that name
                int itemID = -1;
                while(data.moveToNext()) {
                    itemID = data.getInt(0);    //get id
                }
                if(itemID > -1){    //if there is data returned
                    Log.d(TAG, "onItemClick: the ID is " + itemID); //we log it
                    //move to edit activity
                    Intent editScreenIntent = new Intent(ListDataActivity.this, EditDataActivity.class);
                    editScreenIntent.putExtra("id", itemID);    //pass extra
                    editScreenIntent.putExtra("name", name);
                    startActivity(editScreenIntent);
                } else {
                    toastMessage("No ID associated with that name");
                }
            }
        });
    }

    public void toastMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
