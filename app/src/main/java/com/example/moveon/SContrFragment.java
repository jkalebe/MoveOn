package com.example.moveon;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.moveon.adapter.Adapter;
import com.example.moveon.api.DadosService;
import com.example.moveon.api.RetroClient;
import com.example.moveon.model.MoveOn;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 */
public class SContrFragment extends Fragment implements RetroClient {

    private RecyclerView recyclerView;
    private Retrofit retrofit;
    private List<MoveOn> lista = new ArrayList<>();
    private List<MoveOn> listaMove;
    private Adapter adapter;
    private Handler handler = new Handler();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        Log.d("Adapterciclo", "onCreate");
        onRetrofit();


        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View layoutView = inflater.inflate(R.layout.fragment_scontr, container, false);

        Log.d("Adapterciclo", "onCreateView");

        recyclerView = layoutView.findViewById(R.id.recyclerView);

        gerarList();
        //configurar adapter
        //adapter = new Adapter(lista);

        //configurar RecyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(this.getContext(), LinearLayoutManager.VERTICAL));
        //recyclerView.setAdapter(adapter);

        return layoutView;
    }


    @Override
    public void onRetrofit() {
        retrofit = new Retrofit.Builder()
                .baseUrl("http://10.219.7.135:3333")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }


    @Nullable
    @Override
    public Context getContext() {
        return super.getContext();
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("Adapterciclo", "onStart");

    }


    @Override
    public void onResume() {


        String idgoogle = getArguments().getString("IDT");
        DadosService service = retrofit.create(DadosService.class);
        Call<List<MoveOn>> call = service.recuperarDadosUsr(idgoogle);

        call.enqueue(new Callback<List<MoveOn>>() {
            @Override
            public void onResponse(Call<List<MoveOn>> call, Response<List<MoveOn>> response) {
                lista.clear();
                lista = response.body();

                MyRunnable myRunnable = new MyRunnable(lista);
                new Thread(myRunnable).start();

                for (int i = 0; i < lista.size(); i++) {
                    MoveOn moveOn = lista.get(i);
                    Log.d("movelist", moveOn.getTitulo());
                }
            }

            @Override
            public void onFailure(Call<List<MoveOn>> call, Throwable t) {

            }
        });
        super.onResume();
    }

    public void gerarList() {

        //MoveOn moveOn = new MoveOn("TesteProjeto", "5", "2016.08.19T", "teste");
        MoveOn moveOn = new MoveOn("MoveOn", "5", "2019/08/30T", "imageView");
        lista.add(moveOn);

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("Adapterciclo", "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("Adapterciclo", "onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("Adapterciclo", "onDestroyView'''''");
    }


    class MyRunnable implements Runnable {


        private List<MoveOn> list;

        public MyRunnable(List<MoveOn> list) {
            this.list = list;
        }

        @Override
        public void run() {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    //adapter.notifyDataSetChanged();
                    adapter = new Adapter(list);
                    recyclerView.setAdapter(adapter);
                }
            });
        }
    }
}


