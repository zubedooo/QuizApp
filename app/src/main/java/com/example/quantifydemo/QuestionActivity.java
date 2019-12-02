package com.example.quantifydemo;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import android.view.Menu;
import android.widget.TextView;
import java.util.concurrent.TimeUnit;
import android.util.Log;

public class QuestionActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    int time_play=Common.TOTAL_TIME;
    boolean isAnswerModeView=false;
    AnswerSheetAdapter answerSheetAdapter;
    TextView txt_answers,txt_timer;
    RecyclerView answer_sheet_view;
    ViewPager viewPager;
    TabLayout tabLayout;

    @Override
    protected void onDestroy() {
        if(Common.countDownTimer != null){
            Common.countDownTimer.cancel();
        }
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("test","test");
        setContentView(R.layout.activity_question);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(Common.selectedTest.getSubject());
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        Common.questionList=Common.selectedTest.getQuestions();
        afterTakeQuestion();

    }

    private void afterTakeQuestion(){
        Common.answerSheetList.clear();
        Common.fragmentList.clear();
        for(int i=0;i<Common.questionList.size();i++){
            Common.answerSheetList.add(new CurrentQuestion(i,Common.ANSWER_TYPE.NO_ANSWER));
        }

        answer_sheet_view=findViewById(R.id.grid_answer);
        answer_sheet_view.setHasFixedSize(true);
        answer_sheet_view.setLayoutManager(new GridLayoutManager(this,Common.answerSheetList.size()/2));
        answerSheetAdapter=new AnswerSheetAdapter(this,Common.answerSheetList);
        answer_sheet_view.setAdapter(answerSheetAdapter);

        viewPager=findViewById(R.id.viewpager);
        tabLayout=findViewById(R.id.sliding_tabs);

        genFragmentList();
        QuestionFragmentAdapter questionFragmentAdapter=new QuestionFragmentAdapter(getSupportFragmentManager(),this,Common.fragmentList);
        viewPager.setAdapter(questionFragmentAdapter);
        tabLayout.setupWithViewPager(viewPager);

        txt_answers=findViewById(R.id.txt_question_right);
        txt_answers.setVisibility(View.VISIBLE);
        txt_timer=findViewById(R.id.txt_timer);
        txt_timer.setVisibility(View.VISIBLE);

        txt_answers.setText(new StringBuilder(String.format("%d/%d",Common.right_answer_count,Common.questionList.size())));
        countTimer();

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            int SCROLLING_RIGHT=0;
            int SCROLLING_LEFT=1;
            int SCROLLING_UNDETERMINED=2;
            int currentScrollDirection=2;

            private void setScrollingDirection(float positionOffset){

                if(1-positionOffset>=0.5){
                    this.currentScrollDirection=SCROLLING_RIGHT;
                }
                else if(1-positionOffset<=0.5){
                    this.currentScrollDirection=SCROLLING_LEFT;
                }

            }

            private boolean isScrollDirectionUndetermined(){
                return currentScrollDirection==SCROLLING_UNDETERMINED;
            }

            private boolean isScrollDirectionRight(){
                return currentScrollDirection==SCROLLING_RIGHT;
            }

            private boolean isScrollDirectionLeft(){
                return currentScrollDirection==SCROLLING_LEFT;
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if(isScrollDirectionUndetermined()){
                    setScrollingDirection(positionOffset);
                }
            }

            @Override
            public void onPageSelected(int position) {

                QuestionFragment questionFragment;
                int place=0;
                if(position>0){
                    if(isScrollDirectionRight()){
                        questionFragment=Common.fragmentList.get(position-1);
                        place=position-1;
                    }
                    else if(isScrollDirectionLeft()){
                        questionFragment=Common.fragmentList.get(position+1);
                        place=position+1;
                    }
                    else {
                        questionFragment=Common.fragmentList.get(position);
                    }
                }
                else {
                    questionFragment=Common.fragmentList.get(0);
                    place=0;
                }

                answerSheetAdapter.notifyDataSetChanged();

                countCorrectAnswer();

                txt_answers.setText(new StringBuilder(String.format("%d",(Common.right_answer_count+Common.wrong_answer_count)))
                .append("/")
                .append(String.format("%d",Common.questionList.size())));
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if(state==ViewPager.SCROLL_STATE_IDLE) {
                    this.currentScrollDirection = SCROLLING_UNDETERMINED;
                }
            }
        });
    }

    private void countCorrectAnswer(){
        Common.right_answer_count=Common.wrong_answer_count=0;
        for (CurrentQuestion item:Common.answerSheetList){
            if(item.getType()==Common.ANSWER_TYPE.RIGHT_ANSWER){
                Common.right_answer_count++;
            }
            else if(item.getType()==Common.ANSWER_TYPE.WRONG_ANSWER){
                Common.wrong_answer_count++;
            }

        }
    }

    private void genFragmentList() {
        for(int i=0;i<Common.questionList.size();i++){
            Bundle bundle=new Bundle();
            bundle.putInt("index",i);
            QuestionFragment fragment=new QuestionFragment();
            fragment.setArguments(bundle);
            Common.fragmentList.add(fragment);
        }
    }

    private void countTimer() {
        if(Common.countDownTimer!=null) {
            Common.countDownTimer.cancel();
        }
        Common.countDownTimer = new CountDownTimer(Common.TOTAL_TIME, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                txt_timer.setText(String.format("%02d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
                time_play -= 1000;
            }

            @Override
            public void onFinish() {
                finish_game();
            }
        }.start();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.question, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_finish_test) {
            if(!isAnswerModeView){
                new AlertDialog.Builder(this)
                        .setMessage("Are you sure you want to finish the test?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                finish_game();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .create()
                        .show();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void finish_game(){

        startActivity(new Intent(this,TestResult.class));
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_tools) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
