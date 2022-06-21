package com.example.notdeapp.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notdeapp.API.APIRequestData;
import com.example.notdeapp.API.RetroServer;
import com.example.notdeapp.Activity.CreateActivity;
import com.example.notdeapp.Activity.MainActivity;
import com.example.notdeapp.Activity.UpdateActivity;
import com.example.notdeapp.Model.DataModel;
import com.example.notdeapp.Model.ResponseModel;
import com.example.notdeapp.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterData extends RecyclerView.Adapter<AdapterData.AdapterHolder>{
    private Context context;
    private List<DataModel> listNote;
    private List<DataModel> listNoteId;
    private int idNote;


    public AdapterData(Context context, List<DataModel> listNote) {
        this.context = context;
        this.listNote = listNote;
    }

    @NonNull
    @Override
    public AdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_item,parent,false);
        AdapterHolder holder = new AdapterHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterHolder holder, int position) {
        DataModel dm = listNote.get(position);

        holder.tvId.setText(String.valueOf(dm.getId()));
        holder.tvJudul.setText(dm.getJudul());
        holder.tvDes.setText(dm.getDeskripsi());
        holder.tvDate.setText(dm.getDate());


    }

    @Override
    public int getItemCount() {
        return listNote.size();
    }


    public class AdapterHolder extends RecyclerView.ViewHolder {
        TextView tvId, tvJudul, tvDes, tvDate;
        ImageView ivIcon;

        public AdapterHolder(@NonNull View itemView) {
            super(itemView);

            tvId = itemView.findViewById(R.id.tv_id);
            tvJudul = itemView.findViewById(R.id.tv_judul);
            tvDes = itemView.findViewById(R.id.tv_des);
            tvDate = itemView.findViewById(R.id.tv_date);
            ivIcon = itemView.findViewById(R.id.iv_icon);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    idNote = Integer.parseInt(tvId.getText().toString());
                    getNote();
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    AlertDialog.Builder dialogPesan = new AlertDialog.Builder(context);
                    dialogPesan.setMessage("Anda yakin ingin menghapus note ini?");
                    dialogPesan.setTitle("Perhatian");
                    dialogPesan.setIcon(R.mipmap.ic_launcher_round);
                    dialogPesan.setCancelable(true);

                    idNote = Integer.parseInt(tvId.getText().toString());

                    dialogPesan.setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            deleteNote();
                            dialogInterface.dismiss();
                            Handler hand = new Handler();
                            hand.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    ((MainActivity) context).getDataNote();
                                }
                            }, 1000);
                        }
                    });

                    dialogPesan.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ((MainActivity) context).getDataNote();
                        }
                    });

                    dialogPesan.show();
                    return false;
                }
            });
        }

        private void deleteNote(){
            APIRequestData ardData = RetroServer.retrofitConnection().create(APIRequestData.class);
            Call<ResponseModel> hapusData = ardData.deleteData(idNote);

            hapusData.enqueue(new Callback<ResponseModel>() {
                @Override
                public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                    int status = response.body().getStatus();
                    String message = response.body().getMessage();

                    Toast.makeText(context, "Status : "+status+" | Message : "+message, Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onFailure(Call<ResponseModel> call, Throwable t) {
                    Toast.makeText(context, "Gagal Menghubungi Server : " + t.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });
        }
        
        private void getNote(){
            APIRequestData ardData = RetroServer.retrofitConnection().create(APIRequestData.class);
            Call<ResponseModel> ambilData = ardData.getDataId(idNote);

            ambilData.enqueue(new Callback<ResponseModel>() {
                @Override
                public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                    int status = response.body().getStatus();
                    String message = response.body().getMessage();
                    listNoteId = response.body().getData();

                    int varId = listNoteId.get(0).getId();
                    String varJudul = listNoteId.get(0).getJudul();
                    String varDes = listNoteId.get(0).getDeskripsi();
                    String varIsi = listNoteId.get(0).getIsi();



                    Intent goSend = new Intent(context, UpdateActivity.class);
                    goSend.putExtra("xId", varId);
                    goSend.putExtra("xJudul", varJudul);
                    goSend.putExtra("xDes", varDes);
                    goSend.putExtra("xIsi", varIsi);
                    context.startActivity(goSend);

                }

                @Override
                public void onFailure(Call<ResponseModel> call, Throwable t) {
                    Toast.makeText(context, "Gagal Menghubungi Server : " + t.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });

        }

    }
}
