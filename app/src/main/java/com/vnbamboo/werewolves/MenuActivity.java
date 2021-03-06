package com.vnbamboo.werewolves;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MenuActivity extends AppCompatActivity {

    List<Card> cards = new ArrayList<>();
    byte numPlayer;
    public static final String[] PATH_IMG_ROLE = {
            "badong",
            "baove",
            "conlai",
            "cupid",
            "danlang",
            "gau",
            "gialang",
            "kisi",
            "nguyetnu",
            "phuthuy",
            "phuthuycam",
            "soibang",
            "soiden",
            "soitrang",
            "thosan",
            "tientri",
            "tihi"};
    public static final String[] FULL_ROLE_NAME = {
            "Bà đồng",
            "Bảo vệ",
            "Con lai",
            "Cupid",
            "Dân làng",
            "Thần gấu",
            "Già làng",
            "Kị sĩ",
            "Nguyệt nữ",
            "Phù thủy",
            "Phù thủy câm",
            "Sói băng",
            "Sói đen",
            "Sói trắng",
            "Thợ săn",
            "Tiên tri",
            "Ti hí"};
    @Override
    protected void onCreate( @Nullable Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_pick_number);
//        getSupportActionBar().hide();

        generateCard();

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rclViewItem);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        final Intent intent = getIntent();
        numPlayer = intent.getByteExtra("numPlayer", (byte) 0);

        recyclerView.setAdapter(new RecyclerViewAdapter(this, cards, numPlayer ));

        final Context thisContext = this;
        Button btnConfirm = (Button) findViewById(R.id.btnConfirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ) {

                if (RecyclerViewAdapter.total == numPlayer) {
                    //update dataset
                    cards = RecyclerViewAdapter.cards;

                    //create view link to confirm dialog
                    View confirmDialog = getLayoutInflater().inflate(R.layout.confirm_dialog, null);

                    //link 2 column
                    TextView txtRoleColumn = confirmDialog.findViewById(R.id.txtRoleColumn);
                    TextView txtNumColumn = confirmDialog.findViewById(R.id.txtNumColumn);

                    //built alert dialog
                    final AlertDialog.Builder alertDialog = new AlertDialog.Builder(thisContext);
                    alertDialog.setTitle("Xác nhận các vai trò đã chọn");

                    //convert data to string
                    String roleColumn = new String();
                    String numColumn = new String();
                    for (int id = 0; id < cards.size(); id++) {
                        roleColumn += cards.get(id).getName() + "\n";
                        numColumn += Integer.toString(cards.get(id).getNumOrder()) + "\n";
                    }

                    //set compoment
                    txtNumColumn.setText(numColumn);
                    txtRoleColumn.setText(roleColumn);
                    alertDialog.setView(confirmDialog);

                    alertDialog.setPositiveButton("Tiếp tục", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick( DialogInterface dialog, int which ) {
                            startPlayGame();
                        }
                    });
                    alertDialog.setNegativeButton("Chọn lại", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick( DialogInterface dialog, int which ) {
                            dialog.cancel();
                        }

                    });
                    alertDialog.create().show();
                } else {
                    final Toast toast = Toast.makeText(thisContext, "Còn người chơi chưa có vai trò!", Toast.LENGTH_SHORT);
                    toast.show();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            toast.cancel();
                        }
                    }, 750);
                }
            }
        });

    }
    public void generateCard(){
        for(byte i = 0; i < FULL_ROLE_NAME.length; i++){
            Card temp = new Card(i, FULL_ROLE_NAME[i], PATH_IMG_ROLE[i]);
            cards.add(temp);
        }
    }
    public byte randomNumber(byte b){
        return (byte) ((Math.random() * 100) % b);
    }
    private byte[] generateCardOrder(){
        List<Byte> listId = new ArrayList<>();
        List<Boolean> picked = new ArrayList<>();

        for (Card temp:cards) {
            if(temp.getNumOrder() > 0) {
                for (byte i = 0; i < temp.getNumOrder(); i++) {
                    listId.add(temp.getId());
                    picked.add(false);
                }
            }
        }
        byte cardOrder[] = new byte[listId.size()];
        byte iCardOrder = 0, card = 0;
        while (iCardOrder < listId.size()){
            do {
                card = randomNumber((byte) listId.size());
            } while (picked.get(card));
            picked.set(card, true);
            cardOrder[iCardOrder++] = (listId.get(card));
        }
        return cardOrder;
    }
    public void startPlayGame(){
        Intent intent = new Intent(this, NextPlayerActivity.class);
        intent.putExtra("Order list", generateCardOrder());
        this.startActivity(intent);
    }

}
