package com.example.notdeapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

public class UpdateActivity extends AppCompatActivity {
    private int xId;
    private String xJudul, xDes, xIsi;
    private EditText etJudul, etDes, etIsi;
    private Button btnUpdate, btnCancel;
    private String yJudul, yDes, yIsi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        Intent accept = getIntent();
        xId = accept.getIntExtra("xId", -1);
        xJudul = accept.getStringExtra("xJudul");
        xDes = accept.getStringExtra("xDes");
        xIsi = accept.getStringExtra("xIsi");

        etJudul = findViewById(R.id.et_judul);
        etDes = findViewById(R.id.et_des);
        etIsi = findViewById(R.id.et_isi);
        btnUpdate = findViewById(R.id.btn_update);
        btnCancel  = findViewById(R.id.btn_cancel);

        etJudul.setText(xJudul);
        etDes.setText(xDes);
        etIsi.setText(xIsi);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                yJudul = etJudul.getText().toString();
                yDes = etDes.getText().toString();
                yIsi = etIsi.getText().toString();

                updateNote();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UpdateActivity.this, MainActivity.class));
            }
        });
    }

    private void updateNote(){
        APIRequestData ardData = RetroServer.retrofitConnection().create(APIRequestData.class);
        Call<ResponseModel> editData = ardData.updateData(xId,yJudul,yDes,yIsi);

        editData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                int status = response.body().getStatus();
                String message = response.body().getMessage();

                Toast.makeText(UpdateActivity.this, "Status : "+status+" | Message : "+message, Toast.LENGTH_SHORT).show();
                finish();

            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(UpdateActivity.this, "Gagal Menghubungi Server : "+t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }
}