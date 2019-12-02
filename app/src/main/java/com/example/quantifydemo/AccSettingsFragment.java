package com.example.quantifydemo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class AccSettingsFragment extends Fragment implements View.OnClickListener{

    private FirebaseAuth mAuth;
    private StudentData studentData;
    private EditText putUserName,putUSN,putSection,putSemester,putDepartment;
    private ImageView allowPutUsn,allowPutUserName,allowPutSection,allowPutSemester,allowPutDepartment;
    private Button updateInfoButton,resetButtonFirebase;
    private DatabaseReference myRef;
    private TextView changePassword,cancelPasswordChange;
    private RelativeLayout relativeLayout;
    private EditText existingPassword,newPasswordEntry,reNewPasswordEntry;
    private FirebaseUser user;
    private String newPass;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_acc_settings,container, false);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Students");

        relativeLayout=view.findViewById(R.id.resetPasswordLayout);

        changePassword=view.findViewById(R.id.changePasswordButton);
        changePassword.setOnClickListener(this);

        cancelPasswordChange=view.findViewById(R.id.cancelChangePassword);
        cancelPasswordChange.setOnClickListener(this);

        updateInfoButton=view.findViewById(R.id.updateInfoButtons);
        updateInfoButton.setOnClickListener(this);

        resetButtonFirebase=view.findViewById(R.id.resetPasswordFirebaseButton);
        resetButtonFirebase.setOnClickListener(this);

        putUSN=view.findViewById(R.id.putUSN);
        putUserName=view.findViewById(R.id.putUserName);
        putDepartment=view.findViewById(R.id.putDepartment);
        putSemester=view.findViewById(R.id.putSemester);
        putSection=view.findViewById(R.id.putSection);

        allowPutUsn=view.findViewById(R.id.allowPutUSN);
        allowPutUsn.setOnClickListener(this);
        allowPutUserName=view.findViewById(R.id.allowPutUserName);
        allowPutUserName.setOnClickListener(this);
        allowPutSemester=view.findViewById(R.id.allowPutSemester);
        allowPutSemester.setOnClickListener(this);
        allowPutSection=view.findViewById(R.id.allowPutSection);
        allowPutSection.setOnClickListener(this);
        allowPutDepartment=view.findViewById(R.id.allowPutDepartment);
        allowPutDepartment.setOnClickListener(this);
        existingPassword=view.findViewById(R.id.existingPassword);
        existingPassword.setOnClickListener(this);
        newPasswordEntry=view.findViewById(R.id.newPasswordEntry);
        newPasswordEntry.setOnClickListener(this);
        reNewPasswordEntry=view.findViewById(R.id.reExistingPasswordEntry);
        reNewPasswordEntry.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot userids:dataSnapshot.getChildren()){
                    if(userids.getKey().equals(mAuth.getUid().toString())){
                        studentData=userids.getValue(StudentData.class);
                        Log.w(TAG,studentData.getUserName());
                        putUSN.setText(studentData.getUSN());
                        putUserName.setText(studentData.getUserName());
                        putDepartment.setText(studentData.getDepartment());
                        putSemester.setText(studentData.getSemester());
                        putSection.setText(studentData.getSection());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        return view;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.allowPutUSN:
                putUSN.setEnabled(true);
                updateInfoButton.setVisibility(View.VISIBLE);
                break;
            case R.id.allowPutUserName:
                putUserName.setEnabled(true);
                updateInfoButton.setVisibility(View.VISIBLE);
                break;
            case R.id.allowPutDepartment:
                putDepartment.setEnabled(true);
                updateInfoButton.setVisibility(View.VISIBLE);
                break;
            case R.id.allowPutSection:
                putSection.setEnabled(true);
                updateInfoButton.setVisibility(View.VISIBLE);
                break;
            case R.id.allowPutSemester:
                putSemester.setEnabled(true);
                updateInfoButton.setVisibility(View.VISIBLE);
                break;
            case R.id.updateInfoButtons:
                updateInfo();
                break;
            case R.id.changePasswordButton:
                relativeLayout.setVisibility(View.VISIBLE);
                break;
            case R.id.cancelChangePassword:
                relativeLayout.setVisibility(View.GONE);
                break;
            case R.id.resetPasswordFirebaseButton:
                passwordReset();
                break;
        }
    }

    public void updateInfo(){
        studentData.setUSN(putUSN.getText().toString());
        studentData.setUserName((putUserName.getText().toString()));
        studentData.setDepartment(putDepartment.getText().toString());
        studentData.setSection(putSection.getText().toString());
        studentData.setSemester(putSemester.getText().toString());
        String currentUserID=FirebaseAuth.getInstance().getCurrentUser().getUid();
        myRef.child(currentUserID).setValue(studentData);
        Toast.makeText(getActivity(),"Update successful",Toast.LENGTH_LONG).show();
    }

    public void passwordReset(){
        user=FirebaseAuth.getInstance().getCurrentUser();
        String existingPass=existingPassword.getText().toString();
        newPass=newPasswordEntry.getText().toString();
        String reNewPass=reNewPasswordEntry.getText().toString();
        if(!newPass.equals("") && newPass.equals(reNewPass)) {
            AuthCredential credential = EmailAuthProvider
                    .getCredential(user.getEmail(), existingPass);

            user.reauthenticate(credential)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                user.updatePassword(newPass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.d(TAG, "Password updated");
                                            Toast.makeText(getActivity(),"Password chaged",Toast.LENGTH_SHORT).show();
                                            relativeLayout.setVisibility(View.GONE);
                                        } else {
                                            Toast.makeText(getActivity(),"Password could not be changed. Try again later.",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            } else {
                                Toast.makeText(getActivity(),"Incorrect password entered.",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}
