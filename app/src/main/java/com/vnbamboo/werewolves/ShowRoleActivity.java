package com.vnbamboo.werewolves;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class ShowRoleActivity extends AppCompatActivity {

    @Override
    protected void onCreate( @Nullable Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_role);
        getSupportActionBar().hide();

        final byte roleOfThisPlayer = getIntent().getByteExtra("Position", (byte) 0);
        final byte nextRole = (byte) (roleOfThisPlayer + 1);
        final byte cardOrder[] = getIntent().getByteArrayExtra("Order list");

        //View thisView = this; // getLayoutInflater().inflate(R.layout.activity_next_player, null);
        final ImageView card = (ImageView) findViewById(R.id.imgCard);
        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                card.setImageResource(getResources().getIdentifier("com.vnbamboo.werewolves:drawable/" + MenuActivity.PATH_IMG_ROLE[cardOrder[roleOfThisPlayer]], null, null));
            }
        });

        Button btnNextPlayer = (Button) findViewById(R.id.btnNextPlayer);
        btnNextPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                if(nextRole == cardOrder.length)
                    finish();
                else
                    nextCard(cardOrder, nextRole);
            }
        });
    }
    public void nextCard(byte[] cardOrder, byte nextRole){
        Intent intent = new Intent(this, NextPlayerActivity.class);
        intent.putExtra("Order list", cardOrder);
        intent.putExtra("Position", nextRole);
        super.startActivity(intent);
    }
}
