package com.example.notdeapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
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

    private NotificationManager mNotificationManager;
    private final static  String CHANNEL_ID = "channel_id";
    private final static int NOTIF_ID = 0 ;

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

        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel =  new NotificationChannel(CHANNEL_ID, "channel-name", NotificationManager.IMPORTANCE_HIGH);
            mNotificationManager.createNotificationChannel(channel);
        }

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                yJudul = etJudul.getText().toString();
                yDes = etDes.getText().toString();
                yIsi = etIsi.getText().toString();

                updateNote();
                showNotification();
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

    public void showNotification(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);
        builder.setContentTitle("Note berhasil diubah!");
        builder.setContentText("Klik untuk melihat");
        builder.setSmallIcon(R.drawable.ic_notification);
        builder.setPriority(NotificationCompat.PRIORITY_HIGH);

        Intent contentIntent = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent pendingContentIntent = PendingIntent.getActivity(getApplicationContext(), NOTIF_ID, contentIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingContentIntent);

        Notification notification = builder.build();
        mNotificationManager.notify(NOTIF_ID, notification);

    }

}