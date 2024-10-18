package com.example.myapplication;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText etName, etPhone;
    private Button btnInsert;
    private RecyclerView recyclerView;
    private DataAdapter dataAdapter;
    private DatabaseHelper databaseHelper;
    private ArrayList<DataModel> dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etName = findViewById(R.id.et_name);
        etPhone = findViewById(R.id.et_phone);
        btnInsert = findViewById(R.id.btn_insert);
        recyclerView = findViewById(R.id.recyclerView);

        // Inisialisasi DatabaseHelper dan ArrayList untuk menampung data
        databaseHelper = new DatabaseHelper(this);
        dataList = new ArrayList<>();

        // Set RecyclerView Layout Manager dan Adapter
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        dataAdapter = new DataAdapter(dataList);
        recyclerView.setAdapter(dataAdapter);

        // Menampilkan data awal dari database
        displayData();

        // Aksi ketika tombol insert diklik
        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString();
                String phone = etPhone.getText().toString();

                // Validasi input tidak kosong
                if (!name.isEmpty() && !phone.isEmpty()) {
                    boolean isInserted = databaseHelper.insertData(name, phone);
                    if (isInserted) {
                        Toast.makeText(MainActivity.this, "Data inserted", Toast.LENGTH_SHORT).show();
                        displayData(); // Update data di RecyclerView setelah insert
                        etName.setText("");
                        etPhone.setText("");
                    } else {
                        Toast.makeText(MainActivity.this, "Insert failed", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Please enter both name and phone", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Method untuk menampilkan data dari SQLite ke RecyclerView
    private void displayData() {
        Cursor cursor = databaseHelper.getAllData();
        dataList.clear();  // Bersihkan list sebelum menambahkan data baru

        // Jika ada data, masukkan ke ArrayList
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String phone = cursor.getString(2);
                dataList.add(new DataModel(id, name, phone));  // Menggunakan DataModel untuk menyimpan data
            }
        } else {
            Toast.makeText(this, "No data available", Toast.LENGTH_SHORT).show();
        }

        // Notifikasi bahwa dataset telah berubah (untuk RecyclerView)
        dataAdapter.notifyDataSetChanged();
    }
}
