package com.example.moveon;


import android.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

import android.graphics.Canvas;
import android.graphics.Color;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.moveon.api.DadosService;
import com.example.moveon.api.RetroClient;
import com.example.moveon.model.MoveOn;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;

import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;

import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.annotation.OnSymbolClickListener;
import com.mapbox.mapboxsdk.plugins.annotation.Symbol;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import java.util.ArrayList;
import java.util.List;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


import static com.mapbox.mapboxsdk.Mapbox.getApplicationContext;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapasFragment extends Fragment implements RetroClient {

    private MapView mapView;
    private MapboxMap mapboxMap;
    private SymbolManager symbolManager;
    private static final int REQUEST_CODE_AUTOCOMPLETE = 1;
    private static final String ICON_ACESS = "id-icon";
    private String geojsonSourceLayerId = "geojsonSourceLayerId";
    private List<MoveOn> listMoveon = new ArrayList<>();
    Intent intentPutExtra;
    private Retrofit retrofit;
    private float sizeIcon = 1.0f;
    private Handler handler = new Handler();


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d("ciclo", "Fragment: onAttach() criado");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("ciclo", "Fragment: onCreate() criado");

        onRetrofit();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_mapas, container, false);

        Log.d("ciclo", "Fragment: onCreateView() criado");

        mapView = rootView.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        //estilo do mapa é estanciado
        mapView.getMapAsync(map->{
            mapboxMap = map;
            mapboxMap.setStyle(getStyleBuilder("mapbox://styles/jeremiaskalebe/cjx2f4wx702v91cqo728d5xo0"), style -> {

                mapboxMap.addOnMapLongClickListener(this::addSymbol);
                initSearchFab(rootView);

                symbolManager = new SymbolManager(mapView, mapboxMap, style);
                symbolManager.setIconAllowOverlap(true);
                symbolManager.setTextAllowOverlap(true);


                symbolManager.addClickListener(MapasFragment.this::clickViewDados);

            });

        });


        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("ciclo", "Fragment: onActivityCreated() criado");

    }

    /*
        symbolManager.addClickListener(new OnSymbolClickListener() {
            @Override
            public void onAnnotationClick(Symbol symbol) {


                Toast.makeText(getApplicationContext(), symbol.getTextAnchor()+symbol.getId(), Toast.LENGTH_LONG).show();
                clickViewDados(symbol.getLatLng());
            }
        });
    */
    @Override
    public void onStart() {
        super.onStart();
        Log.d("ciclo", "Fragment: onStart() criado");
        mapView.onStart();

    }


    @Override
    public void onResume() {
        super.onResume();
        Log.d("ciclo", "Fragment: onResume() criado");
        mapView.onResume();

        final List<MoveOn>[] moveOns = new List[]{new ArrayList<>()};

        DadosService service = retrofit.create(DadosService.class);
        Call<List<MoveOn>> call = service.recuperarDados();
        call.enqueue(new Callback<List<MoveOn>>() {
            @Override
            public void onResponse(Call<List<MoveOn>> call, Response<List<MoveOn>> response) {
                moveOns[0] = response.body();

                for (int i = 0; i < moveOns[0].size(); i++) {
                    MoveOn moveOn = moveOns[0].get(i);
                    MyClass myClass = new MyClass(moveOn);
                    new Thread(myClass).start();
                }
            }

            @Override
            public void onFailure(Call<List<MoveOn>> call, Throwable t) {

            }
        });

    }


    @Override
    public void onPause() {
        Log.d("ciclo", "Fragment: onPause(): criado");
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        Log.d("ciclo", "Fragment: onStop() criado");
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("ciclo", "Fragment: onDestroyView() criado");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("ciclo", "Fragment: onDestroy() criado");
        mapView.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d("ciclo", "Fragment: onDetach() criado");
    }


    private void clickViewDados(Symbol ponto) {

        intentPutExtra = new Intent(getActivity(), ViewDados.class);
        intentPutExtra.putExtra("latitude", String.valueOf(ponto.getLatLng().getLatitude()));

        Log.d("latitude", String.valueOf(ponto.getLatLng().getLatitude()));


        getActivity().startActivity(intentPutExtra);

    }

    private boolean addSymbol(LatLng point) {
        if (symbolManager == null) {
            return false;
        }

        //chama a tela activity_dados_mapa

        //Variaveis das coordenadas
        String latitudeString = String.valueOf(point.getLatitude());
        String longitudeString = String.valueOf(point.getLongitude());

        //envia as coordenadas
        Intent It = new Intent(getActivity(), DadosMapa.class);
        It.putExtra("Latitude", latitudeString);
        It.putExtra("Longitude", longitudeString);
        It.putExtra("idGoogle", getArguments().getString("IDT"));
        //chama a tela de dados
        getActivity().startActivity(It);

        return true;
    }

    private void initSearchFab(View view) {
        view.findViewById(R.id.fab_location_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new PlaceAutocomplete.IntentBuilder()
                        .accessToken(Mapbox.getAccessToken())
                        .placeOptions(PlaceOptions.builder()
                                .backgroundColor(Color.parseColor("#EEEEEE"))
                                .limit(10)
                                .language("PORTUGUESE")
                                .build(PlaceOptions.MODE_CARDS))
                        .build(getActivity());
                startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_AUTOCOMPLETE) {
            CarmenFeature selectedCarmenFeature = PlaceAutocomplete.getPlace(data);

            if (mapboxMap != null) {
                Style style = mapboxMap.getStyle();
                if (style != null) {
                    GeoJsonSource source = style.getSourceAs(geojsonSourceLayerId);
                    if (source != null) {
                        source.setGeoJson(FeatureCollection.fromFeatures(
                                new Feature[] {Feature.fromJson(selectedCarmenFeature.toJson())}));
                    }

// Move map camera to the selected location
                    mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                            new CameraPosition.Builder()
                                    .target(new LatLng(selectedCarmenFeature.center().latitude(), selectedCarmenFeature.center().longitude()))
                                    .zoom(18)
                                    .build()), 4000);
                }
            }

            Toast.makeText(getActivity(), selectedCarmenFeature.text()+" "+selectedCarmenFeature.geometry() + selectedCarmenFeature.address(), Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onLowMemory () {
        super.onLowMemory();
        mapView.onLowMemory();
        Log.d("ciclo", "Fragment: onLowMemory() criado");
    }


    @Override
    public void onSaveInstanceState (Bundle outState){
        super.onSaveInstanceState(outState);
        Log.d("ciclo", "Fragment: onSaveInstanceState() criado");
        mapView.onSaveInstanceState(outState);
    }

    private Style.Builder getStyleBuilder(@NonNull String styleUrl){
        return new Style.Builder().fromUrl(styleUrl)
                .withImage(ICON_ACESS, generateBitmap(R.drawable.ic_accessible_black_24dp));
    }

    private Bitmap generateBitmap(@DrawableRes int drawableRes) {
        Drawable drawable = getResources().getDrawable(drawableRes);
        return getBitmapFromDrawable(drawable);
    }

    static Bitmap getBitmapFromDrawable(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        } else {
            // width and height are equal for all assets since they are ovals.
            Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        }
    }

    @Override
    public void onRetrofit() {
        retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.7:3333")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    class MyClass implements Runnable {

        private MoveOn moveOn;

        public MyClass(MoveOn moveOn) {
            this.moveOn = moveOn;
        }

        @Override
        public void run() {
            handler.post(new Runnable() {
                @Override
                public void run() {

                    symbolManager.create(new SymbolOptions()
                            .withLatLng(new LatLng(moveOn.getLatitude(), moveOn.getLongitude()))
                            .withTextAnchor("Ponto")
                            .withIconImage(ICON_ACESS)
                            .withIconSize(sizeIcon)
                            .withDraggable(false)
                    );

                }
            });
        }
    }

    class MySymbolClass implements Runnable {

        @Override
        public void run() {

            handler.post(new Runnable() {
                @Override
                public void run() {
                    symbolManager.addClickListener(MapasFragment.this::clickViewDados);
                }
            });
        }
    }

}