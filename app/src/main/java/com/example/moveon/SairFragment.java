package com.example.moveon;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;

/**
 * A simple {@link Fragment} subclass.
 */
public class SairFragment extends DialogFragment  {
private GoogleSignInClient googleSignInClient;

    public SairFragment() {
        // Required empty public constructor
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout to use as dialog or embedded fragment
        exibirDialog();
        return inflater.inflate(R.layout.fragment_sair, container, false);
    }


    public void exibirDialog(){
        AlertDialog.Builder sairDialog = new AlertDialog.Builder(getContext());
        sairDialog.setTitle("Sair");
        sairDialog.setMessage("Tem certeza que deseja sair?");
        sairDialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getContext(), "Clicou em sim", Toast.LENGTH_SHORT).show();


            }
        });

        sairDialog.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getContext(), "Clicou em não", Toast.LENGTH_SHORT).show();
            }
        });
        sairDialog.show();
    }

}
