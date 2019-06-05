package com.example.moveon;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class SobreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sobre);
    }

    public void btn_Voltar(View view){
        finish();
    }
}
