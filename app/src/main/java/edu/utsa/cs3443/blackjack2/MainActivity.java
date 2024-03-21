package edu.utsa.cs3443.blackjack2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import edu.utsa.cs3443.blackjack2.model.Player;

public class MainActivity extends AppCompatActivity {
    private ArrayList<Player> players;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button enterButton = findViewById(R.id.enterButton);
        EditText editText = findViewById(R.id.editText);

        players = new ArrayList<>();
        Log.d("PLAYERS INITIALIZED", "players created and initialized" + players.toString());

        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editText.getText().toString().equals("")) {
                    Toast.makeText(MainActivity.this, "Please enter a name", Toast.LENGTH_SHORT).show();
                } else {
                    String name = editText.getText().toString();
                    Log.d("EDIT TEXT WORKED", "passed");
                    Player player = new Player(name, 1000);

                    if (players.isEmpty()) {
                        players.add(player);
                        Toast.makeText(MainActivity.this, "Welcome " + player.getName(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(MainActivity.this, "+ $1000", Toast.LENGTH_SHORT).show();
                    } else {
                        for (int i = 0; i < players.size(); i++) {
                            if (players.get(i).getName().equals(player.getName())) {
                                Toast.makeText(MainActivity.this, "Welcome Back " + player.getName(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    Intent intent = new Intent(MainActivity.this, TableActivity.class);
                    intent.putExtra("players", players);
                    Log.d("PUT PLAYERS", "passed: " + intent.getExtras().toString());
                    intent.putExtra("name", player.getName());
                    Log.d("PUT PLAYER", "passed: " + player.getName());
                    startActivity(intent);
                    Log.d("ACTIVITY STARTED", "started TableActivity.class");

                    editText.setText("");
                }
            }
        });
    }
}