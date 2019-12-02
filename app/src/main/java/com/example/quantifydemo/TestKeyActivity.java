package com.example.quantifydemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class TestKeyActivity extends AppCompatActivity {

    private EditText takeKey;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_key);
        TextView textView=findViewById(R.id.key_test_name);
        textView.setText(Common.selectedTest.getSubject());
        takeKey=findViewById(R.id.take_key);
    }

    public void startQuestions(View view){
        String password=takeKey.getText().toString();
        if(password.equals(Common.selectedTest.getKey())) {
            startActivity(new Intent(getApplicationContext(), QuestionActivity.class));
        }
        else{
            Toast.makeText(this,"Incorrect Key",Toast.LENGTH_SHORT).show();
        }
    }

    public void goBackHome(View view){
        finish();
        startActivity(new Intent(getApplicationContext(),HomeActivity.class));
    }
}
