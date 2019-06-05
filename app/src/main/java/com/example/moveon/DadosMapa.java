package com.example.moveon;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;

public class DadosMapa extends AppCompatActivity {

    private static final int IMAGE_GALLERY_REQUEST = 1;



    private static String tituloResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //String comentarioResult;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dados_mapa);

    }

    public void clickRatingBar(View view){
        RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
    }

    public void selectImage(View view){

    }

    public void clickOk(View view){
        //EditText Titulo = findViewById(R.id.editTitulo);
        //EditText Comentario = findViewById(R.id.editComentario);

        //Bundle params = new Bundle();
        //params.putString("titulo", Titulo.getText().toString());
        //params.putString("comentario",  Comentario);
        finish();
    }

    public void clickCancelar(View view){
        finish();
    }

}
