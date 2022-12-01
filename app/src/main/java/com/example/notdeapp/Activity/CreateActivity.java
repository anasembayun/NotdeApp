package com.example.notdeapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.notdeapp.API.APIRequestData;
import com.example.notdeapp.API.RetroServer;
import com.example.notdeapp.BuildConfig;
import com.example.notdeapp.Model.ResponseModel;
import com.example.notdeapp.R;
import com.google.android.material.internal.TextWatcherAdapter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateActivity extends AppCompatActivity {
    private EditText etJudul, etDes, etIsi;
    private Button btnSave, btnCancel;
    private String judul, deskripsi, isi;

    private NotificationManager mNotificationManager;
    private final static  String CHANNEL_ID = "channel_id";
    private final static int NOTIF_ID = 0 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        etJudul = findViewById(R.id.et_judul);
        etDes = findViewById(R.id.et_des);
        etIsi = findViewById(R.id.et_isi);
        btnSave = findViewById(R.id.btn_save);
        btnCancel = findViewById(R.id.btn_cancel);

        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel =  new NotificationChannel(CHANNEL_ID, "channel-name", NotificationManager.IMPORTANCE_HIGH);
            mNotificationManager.createNotificationChannel(channel);
        }



        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                judul = etJudul.getText().toString();
                deskripsi = etDes.getText().toString();
                isi = etIsi.getText().toString();

                if(judul.trim().equals("")){
                    btnSave.setEnabled(false);
                }
                else if(deskripsi.trim().equals("")){
                    btnSave.setEnabled(false);
                }
                else if(isi.trim().equals("")){
                    btnSave.setEnabled(false);
                }
                else{
                    btnSave.setEnabled(true);
                    createNote();
                    showNotification();
                }

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CreateActivity.this, MainActivity.class));
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

    public void showNotification(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);
        builder.setContentTitle("Note baru ditambahkan!");
        builder.setContentText("Klik untuk melihat");
        builder.setSmallIcon(R.drawable.ic_notification);
        builder.setPriority(NotificationCompat.PRIORITY_HIGH);

        Bitmap mascotBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.logo);
        builder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(mascotBitmap).
                setBigContentTitle("This notification has been update"));

        Intent contentIntent = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent pendingContentIntent = PendingIntent.getActivity(getApplicationContext(), NOTIF_ID, contentIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingContentIntent);

        Notification notification = builder.build();
        mNotificationManager.notify(NOTIF_ID, notification);
    }


}