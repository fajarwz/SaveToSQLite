package com.example.savetosqlite;

/*
    Created by: fajar on 14/10/19.
*/

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EditDataActivity extends AppCompatActivity {

    private static final String TAG = "EditDataActivity";

    private Button btnDelete, btnSave;
    private EditText editData;

    DatabaseHelper mDatabaseHelper;

    private int selectedID;
    private String selectedName;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_data_layout);

        btnDelete = findViewById(R.id.btn_delete_input_data);
        btnSave = findViewById(R.id.btn_save_input_data);
        editData = findViewById(R.id.edit_data);
        mDatabaseHelper = new DatabaseHelper(this);

        //get the intent extra from listDataActivity
        Intent receivedIntent = getIntent();

        //get the itemID, passed as an extra
        selectedID = receivedIntent.getIntExtra("id", -1);  //-1 is the default value

        //get the name, passed as an extra
        selectedName = receivedIntent.getStringExtra("name");

        //set the edit text to show the selected name
        editData.setText(selectedName);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String item = editData.getText().toString();
                if(!item.equals("")){   //if the item doesnt empty
                    mDatabaseHelper.updateName(item, selectedID, selectedName);
                    Intent intent = new Intent(EditDataActivity.this, ListDataActivity.class);
                    startActivity(intent);
                    toastMessage("Edited successfully!");
                } else {
                    toastMessage("You must enter a name");
                }
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabaseHelper.deleteName(selectedID, selectedName);
                Intent intent = new Intent(EditDataActivity.this, ListDataActivity.class);
                startActivity(intent);
                toastMessage("removed from database successfully!");
            }
        });
    }

    public void toastMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
