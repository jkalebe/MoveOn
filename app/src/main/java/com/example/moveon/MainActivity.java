package com.example.moveon;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    //private Button btnPesquisar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void botaoComecar(View view) {
        Intent it = new Intent(this, mapBoxImplement.class);
        startActivity(it);
    }

    public void botaoSobre(View view){
        Intent it = new Intent(this, SobreActivity.class);
        startActivity(it);
    }

    public void botaoAjuda(View view){
        //Intent intent = new Intent(this, IntroActivity.class);
        //startActivity(intent);
        Intent it = new Intent(this, AjudaActivity.class);
        startActivity(it);
    }
}
