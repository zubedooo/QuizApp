package com.example.quantifydemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText emailId,userPassword,reUserPassword,userUSN,userSemester,userSection,userDepartment,userName;
    private DatabaseReference myRef;
    private StudentData studentData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Students");

        mAuth = FirebaseAuth.getInstance();
        emailId=findViewById(R.id.signUpEmail);
        userPassword=findViewById(R.id.signUpPassword);
        reUserPassword=findViewById(R.id.reSignUpPassword);
        userUSN=findViewById(R.id.takeUSN);
        userSemester=findViewById(R.id.takeSemester);
        userSection=findViewById(R.id.takeSection);
        userDepartment=findViewById(R.id.takeDepartment);
        userName=findViewById(R.id.takeUserName);
    }

    public boolean checkIfNull(EditText editText){
        if(editText.getText().toString().equals(""))
            return false;
        return true;
    }

    public boolean passwordCheck(){
        if(checkIfNull(emailId) && checkIfNull(userPassword) && checkIfNull(reUserPassword) &&
        checkIfNull(userUSN) && checkIfNull(userSemester) && checkIfNull(userSection) &&
        checkIfNull(userDepartment) && checkIfNull(userName)){

            if(!userPassword.getText().toString().equals(reUserPassword.getText().toString()))
                return false;

            studentData = new StudentData(userName.getText().toString(),
                    userSection.getText().toString(),
                    userUSN.getText().toString(),
                    userDepartment.getText().toString(),
                    userSemester.getText().toString());
            return true;
        }
        else
            return false;
    }

    public void signUp(View view){

        if(passwordCheck()) {
            mAuth.createUserWithEmailAndPassword(emailId.getText().toString(), userPassword.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Toast.makeText(SignUpActivity.this, "Authentication success.",
                                        Toast.LENGTH_SHORT).show();
                                FirebaseUser user = mAuth.getCurrentUser();
                                String currentUserID=FirebaseAuth.getInstance().getCurrentUser().getUid();
                                myRef.child(currentUserID).setValue(studentData);
                                finish();
                                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                            } else {
                                // If sign in fails, display a message to the user.

                                Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                //updateUI(null);
                            }

                            // ...
                        }
                    });
        }else{
            Toast.makeText(SignUpActivity.this,"Check credentials",Toast.LENGTH_LONG).show();
        }
    }
}
