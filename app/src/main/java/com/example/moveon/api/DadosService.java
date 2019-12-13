package com.example.moveon.api;

import android.net.Uri;

import com.example.moveon.model.MoveOn;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface DadosService {
    @Multipart
    @POST("/posts")
    Call<ResponseBody> uploadMoveon(
            @Part("tipoacess") RequestBody tipoacess,
            @Part("titulo") RequestBody titulo,
            @Part("classific") RequestBody classific,
            @Part("comentario") RequestBody comentario,
            @Part("latitude") RequestBody latitude,
            @Part("longitude") RequestBody longitude,
            @Part("idgoogle") RequestBody idgoogle,
            @Part MultipartBody.Part photo
    );

    @GET("/posts")
    Call<List<MoveOn>> recuperarDados();

    @GET("/posts/{latitude}")
    Call<List<MoveOn>> recuperarDadosCompleto(@Path("latitude") String latitude);

    @GET("/posts/usr/{idgoogle}")
    Call<List<MoveOn>> recuperarDadosUsr(@Path("idgoogle") String idgoogle);

    @DELETE("/posts/usr/{idgoogle}/{id}")
    Call<Void> apagarDados(@Path("idgoogle") String idgoogle, @Path("id") String id);

}
