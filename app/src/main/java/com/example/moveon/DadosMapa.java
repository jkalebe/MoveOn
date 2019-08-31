package com.example.moveon;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
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
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.moveon.api.DadosService;
import com.example.moveon.api.RetroClient;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DadosMapa extends AppCompatActivity implements RetroClient {

    private Retrofit retrofit;

    private static final int PERMISSION_STORAGE = 2;
    private static final int PICK_IMAGE = 100;

    private String selectImagePath;

    private ImageButton imageButton;
    private EditText editTextTitulo;
    private EditText editTextComentario;
    private CheckBox checkBoxRampa;
    private CheckBox checkBoxBanheiro;
    private CheckBox checkBoxEstacionamento;
    private RatingBar ratingBar;
    private Button buttonOk;


    private String checkString;
    private final int[] avaliacao = new int[1];


    private static String tituloResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dados_mapa);

        checkString = "";

        imageButton = findViewById(R.id.cameraButton);
        editTextTitulo = findViewById(R.id.textTitulo);
        editTextComentario = findViewById(R.id.editComentario);
        checkBoxBanheiro = findViewById(R.id.checkBoxBanheiro);
        checkBoxEstacionamento = findViewById(R.id.checkBoxEstacionamento);
        checkBoxRampa = findViewById(R.id.checkBoxRampa);
        ratingBar = findViewById(R.id.classificStar);
        buttonOk = findViewById(R.id.clickOk);

        buttonOk.setEnabled(false);

        onRetrofit();

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(DadosMapa.this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(DadosMapa.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(DadosMapa.this,
                            new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            PERMISSION_STORAGE);

                } else {
                    openImage();
                    //Toast.makeText(MainActivity.this, "Image Selected", Toast.LENGTH_SHORT).show();
//
                }

            }

        });

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                avaliacao[0] = (int) v;
                Log.d("RatingBar: ", String.valueOf(avaliacao[0]));
            }
        });

        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadFile();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            Uri selectImageUri = data.getData();
            selectImagePath = getRealPathFromURI(selectImageUri);
            buttonOk.setEnabled(true);
            //decodeImage(selectImagePath);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openImage();
                }

                return;
            }
        }
    }

    private void openImage() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        //intent.setType("image/*");
        //intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE);
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

    private void uploadFile() {
        File file = new File(selectImagePath);

        String rampa = "Rampa ";
        String estacionamento = "Estacionamento ";
        String banheiro = "Banheiro ";

        if (checkBoxRampa.isChecked()) {
            checkString = checkString + rampa;
        }

        if (checkBoxEstacionamento.isChecked()) {
            checkString = checkString + estacionamento;
        }

        if (checkBoxBanheiro.isChecked()) {
            checkString = checkString + banheiro;
        }

        String latitudeString;
        String longitudeString;
        String idGoogle;

        latitudeString = getIntent().getStringExtra("Latitude");
        longitudeString = getIntent().getStringExtra("Longitude");
        idGoogle = getIntent().getStringExtra("idGoogle");

        RequestBody tituloBody = RequestBody.create(MultipartBody.FORM, editTextTitulo.getText().toString());
        RequestBody comentarioBody = RequestBody.create(MultipartBody.FORM, editTextComentario.getText().toString());
        RequestBody classificBody = RequestBody.create(MultipartBody.FORM, String.valueOf(avaliacao[0]));
        RequestBody tipoAcessBody = RequestBody.create(MultipartBody.FORM, checkString);
        RequestBody latitudeBody = RequestBody.create(MultipartBody.FORM, latitudeString);
        RequestBody longitudeBody = RequestBody.create(MultipartBody.FORM, longitudeString);
        RequestBody idgoogle = RequestBody.create(MultipartBody.FORM, idGoogle);

        RequestBody imageBody = RequestBody.create(MediaType.parse("image"), file);
        MultipartBody.Part fileUpload = MultipartBody.Part.createFormData("image", file.getName(), imageBody);

        DadosService service = retrofit.create(DadosService.class);
        Call<ResponseBody> call = service.uploadMoveon(tipoAcessBody, tituloBody, classificBody, comentarioBody, latitudeBody, longitudeBody, idgoogle, fileUpload);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                Toast.makeText(DadosMapa.this, "MaisSucesso", Toast.LENGTH_SHORT).show();
                Log.d("Resultado1:  ", "Tudo Certo");
                if (response.isSuccessful()) {
                    //Toast.makeText(MainActivity.this, "Sucesso", Toast.LENGTH_SHORT).show();
                    Log.d("Resultado:  ", "Tudo Certo");
                    try {
                        Log.d("titulo", response.body().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
        finish();
    }

    public void clickCancelar(View view){
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