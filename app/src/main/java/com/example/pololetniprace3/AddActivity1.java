// AddActivity1.java

package com.example.pololetniprace3;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.List;

public class AddActivity1 extends AppCompatActivity {

    private ListView listView;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add1);

        listView = findViewById(R.id.listView);

        dbHelper = new DatabaseHelper(this);
        List<String> itemList = dbHelper.getAllCardSets();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, itemList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = (String) parent.getItemAtPosition(position);
                openAddActivity2(selectedItem);
            }
        });
    }

    private void openAddActivity2(String cardSetName) {
        Intent intent = new Intent(this, AddActivity2.class);
        intent.putExtra("card_set_name", cardSetName);
        startActivity(intent);
    }
}