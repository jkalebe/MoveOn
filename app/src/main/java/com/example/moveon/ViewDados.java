package com.example.moveon;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.moveon.api.DadosService;
import com.example.moveon.api.RetroClient;
import com.example.moveon.model.MoveOn;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ViewDados extends AppCompatActivity implements RetroClient {

    private TextView comentario;
    private TextView tipoAcess;
    private TextView titulo;
    private RatingBar ratingBar;
    private ImageView imageView;
    private Retrofit retrofit;
    private List<MoveOn> listMoveon = new ArrayList<>();
    private MoveOn moveOn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_dados);

        String latitude = getIntent().getStringExtra("latitude");
        Log.d("latitudeview", latitude);

        onRetrofit();

        comentario = findViewById(R.id.textComentario);
        titulo = findViewById(R.id.textViewTitulo);
        tipoAcess = findViewById(R.id.textTipoAcess);
        ratingBar = findViewById(R.id.ratingBarClassific);
        imageView = findViewById(R.id.imagePhoto);

        configRequisicao(latitude);

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void configRequisicao(String latitude) {

        DadosService dadosService = retrofit.create(DadosService.class);
        Call<List<MoveOn>> call = dadosService.recuperarDadosCompleto(latitude);
        call.enqueue(new Callback<List<MoveOn>>() {
            @Override
            public void onResponse(Call<List<MoveOn>> call, Response<List<MoveOn>> response) {

                listMoveon = response.body();
                moveOn = listMoveon.get(0);
                Log.d("body", moveOn.getTitulo() + "\n" + moveOn.getComentario() + "\n" + moveOn.getTipoacess() + "\n" + moveOn.getClassific());

                titulo.setText(moveOn.getTitulo());
                //getActionBar().setTitle(moveOn.getTitulo());
                comentario.setText(moveOn.getComentario());
                tipoAcess.setText(moveOn.getTipoacess());
                ratingBar.setRating(Float.parseFloat(moveOn.getClassific()));
                Picasso.get().load(URL + "/files/" + moveOn.getImage()).centerInside().fit().into(imageView);


            }

            @Override
            public void onFailure(Call<List<MoveOn>> call, Throwable t) {

            }
        });
    }

    public void clickVoltar(View view) {
        finish();
    }


    @Override
    public void onRetrofit() {
        retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}