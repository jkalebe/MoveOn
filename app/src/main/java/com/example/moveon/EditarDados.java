package com.example.moveon;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;

import com.example.moveon.api.DadosService;
import com.example.moveon.api.RetroClient;
import com.example.moveon.model.MoveOn;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EditarDados extends AppCompatActivity implements RetroClient {

    private ImageButton imageButton;
    private EditText editTextTitulo;
    private EditText editTextComentario;
    private CheckBox checkBoxRampa;
    private CheckBox checkBoxBanheiro;
    private CheckBox checkBoxEstacionamento;
    private RatingBar ratingBar;
    private Button buttonOk;
    private Button buttonCancelar;
    private ImageView imageSelected;
    private Button editImage;

    private static final int PERMISSION_STORAGE = 2;
    private static final int PICK_IMAGE = 100;

    private List<MoveOn> listMoveon = new ArrayList<>();

    private String latitude;

    Retrofit retrofit;
    private MoveOn moveOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_dados);

        onRetrofit();

        latitude = getIntent().getStringExtra("latitude");

        imageButton = findViewById(R.id.cameraButton);
        editTextTitulo = findViewById(R.id.textTitulo);
        editTextComentario = findViewById(R.id.editComentario);
        checkBoxBanheiro = findViewById(R.id.checkBoxBanheiro);
        checkBoxEstacionamento = findViewById(R.id.checkBoxEstacionamento);
        checkBoxRampa = findViewById(R.id.checkBoxRampa);
        ratingBar = findViewById(R.id.classificStar);
        buttonOk = findViewById(R.id.clickOk);
        buttonCancelar = findViewById(R.id.clickCancelar);
        imageSelected = findViewById(R.id.photo_selected);
        editImage = findViewById(R.id.button_edit);

        carregarDadosEdit();

        buttonCancelar.setOnClickListener(view -> {
            finish();
        });

        buttonOk.setOnClickListener(view -> {
            finish();
        });

        editImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(EditarDados.this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(EditarDados.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(EditarDados.this,
                            new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            PERMISSION_STORAGE);

                } else {
                    openImage();
                    //Toast.makeText(MainActivity.this, "Image Selected", Toast.LENGTH_SHORT).show();
//
                }

            }

        });

    }

    private void carregarDadosEdit() {
        DadosService service = retrofit.create(DadosService.class);
        Call<List<MoveOn>> call = service.recuperarDadosCompleto(latitude);

        call.enqueue(new Callback<List<MoveOn>>() {
            @Override
            public void onResponse(Call<List<MoveOn>> call, Response<List<MoveOn>> response) {
                listMoveon = response.body();
                moveOn = listMoveon.get(0);
                editTextTitulo.setText(moveOn.getTitulo());
                editTextComentario.setText(moveOn.getComentario());
                ratingBar.setRating(Float.parseFloat(moveOn.getClassific()));
                String[] tipo = moveOn.getTipoacess().split(" ");

                if (tipo.length == 1) {
                    if (tipo[0].equals("Estacionamento"))
                        checkBoxEstacionamento.setChecked(true);
                    else if (tipo[0].equals("Rampa"))
                        checkBoxRampa.setChecked(true);
                    else if (tipo[0].equals("Banheiro"))
                        checkBoxBanheiro.setChecked(true);
                } else if (tipo.length == 2) {
                    if (tipo[0].equals("Estacionamento") || tipo[1].equals("Estacionamento") || tipo[2].equals("Estacionamento"))
                        checkBoxEstacionamento.setChecked(true);
                    if (tipo[0].equals("Rampa") || tipo[1].equals("Rampa") || tipo[2].equals("Rampa"))
                        checkBoxRampa.setChecked(true);
                    if (tipo[0].equals("Banheiro") || tipo[1].equals("Banheiro") || tipo[2].equals("Banheiro"))
                        checkBoxBanheiro.setChecked(true);
                } else if (tipo.length == 3) {
                    if (tipo[0].equals("Estacionamento") || tipo[1].equals("Estacionamento") || tipo[2].equals("Estacionamento"))
                        checkBoxEstacionamento.setChecked(true);
                    if (tipo[0].equals("Rampa") || tipo[1].equals("Rampa") || tipo[2].equals("Rampa"))
                        checkBoxRampa.setChecked(true);
                    if (tipo[0].equals("Banheiro") || tipo[1].equals("Banheiro") || tipo[2].equals("Banheiro"))
                        checkBoxBanheiro.setChecked(true);
                }


                Picasso.get().load(URL + "/files/" + moveOn.getImage()).centerInside().fit().into(imageSelected);

            }

            @Override
            public void onFailure(Call<List<MoveOn>> call, Throwable t) {

            }
        });
    }


    @Override
    public void onRetrofit() {
        retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private void openImage() {
        Intent intent = new Intent(Intent.ACTION_VIEW,
                android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        //intent.setType("image/*");
        //intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            Uri selectImageUri = data.getData();
            //selectImagePath = getRealPathFromURI(selectImageUri);


            imageSelected.setImageURI(selectImageUri);
            //decodeImage(selectImagePath);
        }
    }

    private String getRealPathFromURI(Uri selectImageUri) {
        Cursor cursor = getContentResolver().query(selectImageUri, null, null, null, null);
        if (cursor == null) {
            return selectImageUri.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }

}
