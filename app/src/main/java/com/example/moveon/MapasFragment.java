package com.example.moveon;


import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapFragment;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.MapboxMapOptions;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.annotation.OnSymbolClickListener;
import com.mapbox.mapboxsdk.plugins.annotation.Symbol;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import static com.example.moveon.mapBoxImplement.getBitmapFromDrawable;
import static com.mapbox.mapboxsdk.Mapbox.getApplicationContext;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapasFragment extends Fragment{

    private MapView mapView;
    private MapboxMap mapboxMap;
    private SymbolManager symbolManager;
    private static final int REQUEST_CODE_AUTOCOMPLETE = 1;
    private static final String ICON_ACESS = "id-icon";
    private static int quantIcon=0;
    private String geojsonSourceLayerId = "geojsonSourceLayerId";

    public MapasFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        final View rootView = inflater.inflate(R.layout.fragment_mapas, container, false);
        Intent it = new Intent(getActivity(), ViewDados.class);

        mapView = rootView.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(map->{
            mapboxMap = map;

            mapboxMap.addOnMapLongClickListener(this::addSymbol);

            mapboxMap.setStyle(getStyleBuilder(Style.MAPBOX_STREETS), style -> {

                initSearchFab(rootView);
                symbolManager = new SymbolManager(mapView, mapboxMap, style);
                symbolManager.setIconAllowOverlap(true);
                symbolManager.setTextAllowOverlap(true);

                symbolManager.addClickListener(new OnSymbolClickListener() {
                    @Override
                    public void onAnnotationClick(Symbol symbol) {


                        Toast.makeText(getApplicationContext(), symbol.getTextAnchor()+symbol.getId(), Toast.LENGTH_LONG).show();
                        getActivity().startActivity(it);
                    }
                });


            });



        });
        return rootView;
    }




    private boolean addSymbol(LatLng point) {
        if (symbolManager == null) {
            return false;
        }
        //chama a tela activity_dados_mapa

        Intent It = new Intent(getActivity(), DadosMapa.class);
        getActivity().startActivity(It);

        //incrementa o contador de icone
        quantIcon++;

        //cria icone no mapa
        symbolManager.create(new SymbolOptions()
                .withLatLng(point)
                .withTextAnchor("titulo")
                .withZIndex(quantIcon) //criar id auto incremetal ........
                .withIconImage(ICON_ACESS)
                .withIconSize(1.3f)
                .withDraggable(false)
        );
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

    private void setUpSource(@NonNull Style loadedMapStyle) {
        loadedMapStyle.addSource(new GeoJsonSource(geojsonSourceLayerId));
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
    public void onStart () {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume () {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause () {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop () {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onLowMemory () {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onDestroy () {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState (Bundle outState){
        super.onSaveInstanceState(outState);
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
}
