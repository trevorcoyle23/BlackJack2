package edu.utsa.cs3443.blackjack2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {
    private EditText bjTargetEditText, standEditText, nameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        bjTargetEditText = findViewById(R.id.bjTargetEditText);
        standEditText = findViewById(R.id.standEditText);
        nameEditText = findViewById(R.id.nameEditText);

        Button enterButton = findViewById(R.id.enterButton);
        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (nameEditText.getText().toString().equals("")) {
                    Toast.makeText(SettingsActivity.this, "Please enter a name", Toast.LENGTH_SHORT).show();
                } else {
                    String name = nameEditText.getText().toString();
                    Intent intent = new Intent(SettingsActivity.this, TableActivity.class);
                    intent.putExtra("name", name);
                    if (bjTargetEditText.getText().toString().equals("")) {
                        intent.putExtra("blackjack", 21);
                    } else {
                        intent.putExtra("blackjack", Integer.parseInt(bjTargetEditText.getText().toString()));
                    }
                    if (standEditText.getText().toString().equals("")) {
                        intent.putExtra("stand", 17);
                    } else {
                        intent.putExtra("stand", Integer.parseInt(standEditText.getText().toString()));
                    }
                    Toast.makeText(SettingsActivity.this, "Welcome " + name, Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                    nameEditText.setText("");
                    bjTargetEditText.setText("");
                    standEditText.setText("");
                }
            }
        });
    }
}