package com.example.blockutd.blockutd;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;


public class EtherHandler extends AppCompatActivity {

    private boolean blocked;
    private double ether;
    private boolean etherOrNot;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ether = 1.5;
        etherOrNot = true;
        setContentView(R.layout.handler_ether);
        TextView valEther = (TextView) findViewById(R.id.etherQuantity);
        valEther.setText("1.5");
        blocked = true;
        TextView tot = (TextView) findViewById(R.id.totalTime);
        tot.setText("Total time: " + MainActivity.getTotalMinutes() + " minutes");
        TextView totOwed = (TextView) findViewById(R.id.totalOwed);
        double ethowed = ((double)(MainActivity.getTotalMinutes()))/10000;
        totOwed.setText("You owe " + ethowed + " ether");


    }

    private void showStats() {

    }

    private void sendMoney() {
        //update ether
        TextView tot = (TextView) findViewById(R.id.totalTime);
        tot.setText("Total time: " + 0 + " minutes");
        TextView totOwed = (TextView) findViewById(R.id.totalOwed);
        double a = ((double)(MainActivity.getTotalMinutes()))/10000;
        totOwed.setText("You owe " + 0.0 + " ether");
        ether -= a;
        TextView val = (TextView) findViewById(R.id.etherQuantity);
        ImageView ethLogo = (ImageView) findViewById(R.id.ethereumButton);
        etherOrNot = true;
        ethLogo.setVisibility(View.VISIBLE);
        val.setText("" + ether);

        //and then actually send the money

    }

    public void convertEther(View v) {
        TextView val = (TextView) findViewById(R.id.etherQuantity);
        ImageView ethLogo = (ImageView) findViewById(R.id.ethereumButton);
        if(etherOrNot) {
            etherOrNot = false;
            ethLogo.setVisibility(View.INVISIBLE);
            int a = (int)(ether*828.247);
            val.setText("$"+a);
        }
        else {
            etherOrNot = true;
            ethLogo.setVisibility(View.VISIBLE);
            val.setText("" + ether);
        }
    }

    public void apps_blocked(View v) {
        if(blocked) {
            blocked = false;
            showStats();
            sendMoney();
        }
        else {
            blocked = true;
            MainActivity.resetBeginTime();
        }
    }
}
