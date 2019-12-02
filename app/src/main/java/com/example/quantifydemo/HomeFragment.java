package com.example.quantifydemo;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class HomeFragment extends Fragment {

    private ListView homeListView;
    private HomeFragmentAdapter homeFragmentAdapter;
    private ArrayList<Tests> testList=new ArrayList<>();
    private ProgressBar homeFragmentProgressBar;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private StudentData studentData;
    private DatabaseReference myRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container, false);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        String id=currentUser.getUid();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference userReference=database.getReference("Students").child(id);
        myRef = database.getReference("Tests");
        homeListView=view.findViewById(R.id.home_test_list);
        homeFragmentProgressBar=view.findViewById(R.id.home_fragment_progress_bar);

        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                studentData=dataSnapshot.getValue(StudentData.class);
                findTests();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        homeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Common.selectedTest=testList.get(position);
                startActivity(new Intent(getActivity(),TestKeyActivity.class));
            }
        });

        return view;
    }

    private void findTests() {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                testList.clear();

                for(DataSnapshot alltests:dataSnapshot.getChildren()){
                    Tests temp=alltests.getValue(Tests.class);
                    if(temp.getSection().equals(studentData.getSection()) && temp.getSemester().equals(studentData.getSemester()))
                        testList.add(temp);
                }

                homeFragmentAdapter=new HomeFragmentAdapter(getActivity(),R.layout.home_cards,testList);
                homeListView.setAdapter(homeFragmentAdapter);
                homeFragmentProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(getActivity(),"Error retrieving tests",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
