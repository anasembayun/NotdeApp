package com.example.notdeapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.notdeapp.API.APIRequestData;
import com.example.notdeapp.API.RetroServer;
import com.example.notdeapp.Model.ResponseModel;
import com.example.notdeapp.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateActivity extends AppCompatActivity {
    private EditText etJudul, etDes, etIsi;
    private Button btnSave;
    private String judul, deskripsi, isi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        etJudul = findViewById(R.id.et_judul);
        etDes = findViewById(R.id.et_des);
        etIsi = findViewById(R.id.et_isi);
        btnSave = findViewById(R.id.btn_save);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                judul = etJudul.getText().toString();
                deskripsi = etDes.getText().toString();
                isi = etIsi.getText().toString();

                createNote();
            }
        });
    }

    public void createNote(){
        APIRequestData ardData = RetroServer.retrofitConnection().create(APIRequestData.class);
        Call<ResponseModel> saveData = ardData.insertData(judul,deskripsi,isi);

        saveData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                int status = response.body().getStatus();
                String message = response.body().getMessage();

                Toast.makeText(CreateActivity.this, "Status : "+status+" | Message : "+message, Toast.LENGTH_SHORT).show();
                finish();

            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(CreateActivity.this, "Gagal Menghubungi Server : "+t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

}