package edu.utsa.cs3443.blackjack2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button enterButton = findViewById(R.id.enterButton);
        EditText editText = findViewById(R.id.editText);

        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editText.getText().toString().equals("")) {
                    Toast.makeText(MainActivity.this, "Please enter a name", Toast.LENGTH_SHORT).show();
                } else {
                    String name = editText.getText().toString();
                    Intent intent = new Intent(MainActivity.this, TableActivity.class);
                    intent.putExtra("name", name);
                    Toast.makeText(MainActivity.this, "Welcome " + name, Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                    editText.setText("");
                }
            }
        });
    }
}