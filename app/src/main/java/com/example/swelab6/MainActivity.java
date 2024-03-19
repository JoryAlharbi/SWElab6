package com.example.swelab6;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    Button btn_add, btn_view;
    EditText et_name, et_age;
    ListView lv_StudentList;
    ArrayAdapter<StudentMod> studentArrayAdapter;
    DataBaseHelper dataBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views and database helper
        btn_add = findViewById(R.id.btn_add);
        btn_view = findViewById(R.id.btn_view);
        et_age = findViewById(R.id.et_age);
        et_name = findViewById(R.id.et_name);
        lv_StudentList = findViewById(R.id.lv_StudentList);
        dataBaseHelper = new DataBaseHelper(MainActivity.this);

        // Show students on ListView
        ShowStudentsOnListView();

        // Add button click listener
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create StudentMod object
                StudentMod studentMod;
                try {
                    studentMod = new StudentMod(-1,
                            et_name.getText().toString(),
                            Integer.parseInt(et_age.getText().toString()));
                    Toast.makeText(MainActivity.this,
                            studentMod.toString(), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "Enter Valid input",
                            Toast.LENGTH_SHORT).show();
                    studentMod = new StudentMod(-1, "ERROR", 0);
                }
                // Add student to database
                boolean success = dataBaseHelper.addOne(studentMod);
                Toast.makeText(MainActivity.this, "SUCCESS= " + success,
                        Toast.LENGTH_SHORT).show();
                // Update ListView
                ShowStudentsOnListView();
            }
        });

        // View button click listener
        btn_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Refresh ListView
                ShowStudentsOnListView();
            }
        });

        // ListView item click listener for deletion
        lv_StudentList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                StudentMod clickedStudent = (StudentMod) parent.getItemAtPosition(position);
                boolean deleted = dataBaseHelper.deleteOne(clickedStudent);
                if (deleted) {
                    Toast.makeText(MainActivity.this, "Deleted: " + clickedStudent.toString(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Failed to delete", Toast.LENGTH_SHORT).show();
                }
                // Refresh ListView
                ShowStudentsOnListView();
            }
        });
    }

    private void ShowStudentsOnListView() {
        List<StudentMod> students = dataBaseHelper.getEveryone();
        studentArrayAdapter = new ArrayAdapter<>(MainActivity.this,
                android.R.layout.simple_list_item_1, students);
        lv_StudentList.setAdapter(studentArrayAdapter);
    }

}
