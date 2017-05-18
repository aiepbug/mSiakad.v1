package com.example.aiepbug.belajarjson6;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

import static com.example.aiepbug.belajarjson6.R.id.img_foto;

public class BerandaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beranda);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        String nim = intent.getStringExtra("nim");
        getSupportActionBar().setTitle("SIAKAD - IAIN Palu");

        getBiodata(nim);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Permohonan modifikasi KRS", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void getBiodata(String nim) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(konstanta.URL_SIAKAD+"getMahasiswa/biodata/"+nim+"/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitObjectAPI service = retrofit.create(RetrofitObjectAPI.class);

        //panggil interface
        Call<ModelMhs> call = service.getKoneksi();
        call.enqueue(new Callback<ModelMhs>() {
            @Override
            public void onResponse(Response<ModelMhs> response, Retrofit retrofit) {
                String hasil = response.body().getHasil().toString();
                try {
                    if(hasil.equals("sukses")) {

                        Date tanggal = new SimpleDateFormat("dd/MM/yyyy").parse(response.body().getTgl_lahir());

                        TextView nim = (TextView) findViewById(R.id.txt_nim);
                        nim.setText(": "+response.body().getNim());
                        TextView nama = (TextView) findViewById(R.id.txt_nama);
                        nama.setText(": "+response.body().getNama());
                        TextView jender= (TextView) findViewById(R.id.txt_jender);
                        jender.setText(": "+response.body().getJender());
                        ImageView foto = (ImageView) findViewById(img_foto);
                        Picasso.with(BerandaActivity.this)
                                .load(konstanta.URL_FOTO+response.body().getAngkatan()+"/"+response.body().getNim()+".jpg")
                                .into(foto);
                        TextView prodi= (TextView) findViewById(R.id.txt_prodi);
                        prodi.setText(": "+response.body().getNama_prodi().toUpperCase());
                        TextView tempat_lahir= (TextView) findViewById(R.id.txt_tempat_lahir);
                        tempat_lahir.setText(": "+response.body().getTempat_lahir().toUpperCase());
                        TextView tgl_lahir= (TextView) findViewById(R.id.txt_tgl_lahir);
                        tgl_lahir.setText(": "+tanggal);
                        TextView alamat= (TextView) findViewById(R.id.txt_alamat);
                        alamat.setText(": "+response.body().getAlamat_lokal());
                        TextView hp= (TextView) findViewById(R.id.txt_hp);
                        hp.setText(": "+response.body().getHp());
                        TextView email= (TextView) findViewById(R.id.txt_email);
                        email.setText(": "+response.body().getEmail());
                    }
                    else if(hasil.equals("gagal")) {
                        Toast.makeText(BerandaActivity.this, "Salah NIM atau sandi", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(BerandaActivity.this, "Nothing happen", Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception e) {
                    Log.d("onResponse", "There is an error");
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("onFailure", t.toString());
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_beranda, menu);
        return true;
    }
}
