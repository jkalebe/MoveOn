package com.example.moveon;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class AjudaActivity extends AppCompatActivity {
    private ViewPager screenPager;
    IntroViewPagerAdapter introViewPagerAdapter;
    TabLayout tabIndicator;
    Button btnNext;
    int position = 0;
    Button btnGetStarted;
    Animation btnAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //make the view
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //when this activity is about to be launch we need to check is its openend before or not

        if (restorePrefData()) {

            Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(mainActivity);
            finish();

        }

        setContentView(R.layout.activity_ajuda);

        //hide the action bar
        //getSupportActionBar().hide();

        //iniciar visualização
        btnNext = findViewById(R.id.btn_next);
        btnGetStarted = findViewById(R.id.btn_get_started);
        tabIndicator = findViewById(R.id.tab_indicator);
        btnAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.button_animation);

        // fill list screen
        List<ScreenItem> mList = new ArrayList<>();
        mList.add(new ScreenItem("Como funciona?", "Não sei se você conhece, mas existe na literatura um famoso e inteligente detetive " +
                "chamado Sherlock Holmes. Sherlock era apaixonado pela cidade onde vivia na Inglaterra: Londres. Tão apaixonado que sempre estava " +
                "catalogando a sua cidade, sabendo até mesmo sobre suas particularidades estruturais, informações que ele ia enriquecendo ao longo de " +
                "suas diversas aventuras investigativas. Ele também contava com um aliado para documentar de forma escrita suas façanhas, seu amigo, " +
                "Dr. Watson.", R.drawable.imagedetetive));

        mList.add(new ScreenItem("", "O seu papel aqui é ser tanto o Sherlock quanto o Watson, mas da acessibilidade! Aqui você vai agir " +
                "“investigando” se existe acessibilidade para cadeirantes em espaços públicos e privados da sua cidade, em seguida documentando as " +
                "informações recolhidas.", R.drawable.imagetela2));

        mList.add(new ScreenItem("", "Ao registrar um local você vai seguir alguns passos:\n" +
                "Passo 1- Abrir o mapa.\n" +
                "Passo 2- Digitar no campo de busca o endereço do local.\nPasso 3- Clicar no ponto desejado no mapa, adicionar as informações e deixar um comentário pessoal.\n" +
                "Passo 4- Você vê aquela camerazinha ali? Se quiser acrescentar fotos do local você não só pode como deve. \n" +
                "Passo 5- Avalie o local! Quando mais cadeirinhas selecionar mais acessível o local é.", R.drawable.ic_accessible_black_24dp));

        mList.add(new ScreenItem("","Quer deixar o papel de Sherlock e Watson de lado por um momento para ir relaxar em algum lugar da cidade, mas gostaria de saber antes se tem acessibilidade lá? Elementar, meu caro amigo (a).  É só ir no campo de busca e pesquisar o nome do local para saber tudinho sobre ele.\n" +
                "Muita gente vê, mas não observa. Seja diferente. Bem vindo!", R.drawable.logomoveon21));


        screenPager = findViewById(R.id.screen_viewpager);
        introViewPagerAdapter = new IntroViewPagerAdapter(this, mList);
        screenPager.setAdapter(introViewPagerAdapter);

        //setup tablayout

        tabIndicator.setupWithViewPager(screenPager);

        // next button click
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                position = screenPager.getCurrentItem();
                if(position < mList.size()) {

                    position++;
                    screenPager.setCurrentItem(position);
                }

                if(position == mList.size() -1) {

                    //Todo: show the GETSTARTED button and hide the indicator and the next button

                    loadLastScreen();
                }
            }
        });

        //tabLayout add change listener
        tabIndicator.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if(tab.getPosition() == mList.size()-1){

                    loadLastScreen();

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        //Get Started button click listener
         btnGetStarted.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

                 //open main activity

                 Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
                 startActivity(mainActivity);
                 //also we need  to save  a boolean  value  to storage  ao next  time  when  the user  run the app
                 //we could know that he is already checked the intro screen activity
                 //i'm going to use shared preferences to that process

                 savePrefsData();
                 finish();
             }
         });


    }

    private boolean restorePrefData() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs", MODE_PRIVATE);
        Boolean isIntroActivityOpnendBefore = pref.getBoolean("isIntroOpnend", false);
        return  isIntroActivityOpnendBefore;

    }

    private void savePrefsData() {

            SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean("isIntroOpnend", true);
            editor.commit();
    }

    //Todo: show the GETSTARTED button and hide the indicator and the next button
    private void loadLastScreen() {
        btnNext.setVisibility(View.INVISIBLE);
        btnGetStarted.setVisibility(View.VISIBLE);
        tabIndicator.setVisibility(View.INVISIBLE);

        //TODO: ADD an animation the getstarted
        //lets create the button animation
        btnGetStarted.setAnimation(btnAnim);

    }
}
