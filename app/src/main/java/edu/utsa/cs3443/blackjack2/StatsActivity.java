package edu.utsa.cs3443.blackjack2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;

public class StatsActivity extends AppCompatActivity {

    private int numBlackJacks, numGames, numWins, numLosses, numDraws, topWin, worstLoss;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        initializeStats();
        TextView blackJacks, gamesPlayed, playerWins, dealerWins, pushes, highestPot, worstLossText;
        blackJacks = findViewById(R.id.blackJacks);
        gamesPlayed = findViewById(R.id.gamesPlayed);
        playerWins = findViewById(R.id.playerWins);
        dealerWins = findViewById(R.id.dealerWins);
        pushes = findViewById(R.id.pushes);
        highestPot = findViewById(R.id.highestPot);
        worstLossText = findViewById(R.id.worstLoss);

        blackJacks.setText("" + numBlackJacks);
        gamesPlayed.setText("" + numGames);
        playerWins.setText("" + numWins);
        dealerWins.setText("" + numLosses);
        pushes.setText("" + numDraws);
        highestPot.setText("" + topWin);
        worstLossText.setText("" + worstLoss);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void initializeStats() {
        try {
            // try reading from file
            InputStream in = StatsActivity.this.openFileInput("stats.csv");
            loadStats(in); // call loadStats() with the input stream if successful
        } catch (FileNotFoundException e1) {
            try {
                // try creating file
                OutputStream out = StatsActivity.this.openFileOutput("stats.csv", Context.MODE_PRIVATE);
            } catch (FileNotFoundException e2) {
                e2.printStackTrace();
            }
        }
    }

    public void loadStats(InputStream in) {
        if (in != null) {
            Scanner scan = new Scanner(in);
            if (scan.hasNextLine()) {
                String[] tokens = scan.nextLine().split(",");
                numBlackJacks = Integer.parseInt(tokens[0]);
                numGames = Integer.parseInt(tokens[1]);
                numWins = Integer.parseInt(tokens[2]);
                numLosses = Integer.parseInt(tokens[3]);
                numDraws = Integer.parseInt(tokens[4]);
                topWin = Integer.parseInt(tokens[5]);
                worstLoss = Integer.parseInt(tokens[6]);
            }
        } else {
            numBlackJacks = 0;
            numGames = 0;
            numWins = 0;
            numLosses = 0;
            numDraws = 0;
            topWin = 0;
            worstLoss = 0;
        }
    }
}