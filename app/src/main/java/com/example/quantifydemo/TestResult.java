package com.example.quantifydemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.util.Log;
import android.view.View;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class TestResult extends AppCompatActivity {

    private TextView marksObtained;
    private int testScore;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_result);

        marksObtained=findViewById(R.id.marks_obtained);

        testScore=0;

        for (CurrentQuestion item:Common.answerSheetList){
            if(item.getType()==Common.ANSWER_TYPE.RIGHT_ANSWER){
                testScore++;
            }
        }

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        String id=currentUser.getUid();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference userReference=database.getReference("Results").child(id).child(Common.selectedTest.getSubject());
        userReference.setValue(testScore+"");

        marksObtained.setText(new StringBuilder(String.format("%d",testScore))
                .append("/")
                .append(String.format("%d",Common.questionList.size())));

        Log.i("correct answers",Common.right_answer_count+"");
    }

    public void goHome(View view){
        startActivity(new Intent(this,HomeActivity.class));
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this,HomeActivity.class));
    }
}
