 package com.example.notdeapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.notdeapp.API.APIRequestData;
import com.example.notdeapp.API.RetroServer;
import com.example.notdeapp.Adapter.AdapterData;
import com.example.notdeapp.Model.DataModel;
import com.example.notdeapp.Model.ResponseModel;
import com.example.notdeapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

 public class MainActivity extends AppCompatActivity {
     private RecyclerView rvData;
     private RecyclerView.Adapter adData;
     private RecyclerView.LayoutManager lmData;
     private List<DataModel> listNote = new ArrayList<>();
     private SwipeRefreshLayout srlData;
     private ProgressBar pbData;
     private FloatingActionButton fabAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvData = findViewById(R.id.rv_data);
        srlData = findViewById(R.id.srl_data);
        pbData = findViewById(R.id.pb_data);
        lmData = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvData.setLayoutManager(lmData);
        fabAdd = findViewById(R.id.fab_add);

        //getDataNote();

        srlData.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                srlData.setRefreshing(true);
                getDataNote();
                srlData.setRefreshing(false);
            }
        });

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, CreateActivity.class));
            }
        });

    }

     @Override
     protected void onResume() {
         super.onResume();
         getDataNote();
     }

     public void getDataNote(){
         pbData.setVisibility(View.VISIBLE);

         APIRequestData ardData = RetroServer.retrofitConnection().create(APIRequestData.class);
         Call<ResponseModel> tampilData = ardData.getData();

         tampilData.enqueue(new Callback<ResponseModel>() {
             @Override
             public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                int status = response.body().getStatus();
                String message = response.body().getMessage();

                listNote = response.body().getData();

                 adData = new AdapterData(MainActivity.this, listNote);
                 rvData.setAdapter(adData);
                 adData.notifyDataSetChanged();

                 pbData.setVisibility(View.INVISIBLE);
             }

             @Override
             public void onFailure(Call<ResponseModel> call, Throwable t) {
                 Toast.makeText(MainActivity.this, "Gagal Menghubungi Server : "+t.getMessage(), Toast.LENGTH_SHORT).show();
                 pbData.setVisibility(View.INVISIBLE);

             }
         });
     }
}