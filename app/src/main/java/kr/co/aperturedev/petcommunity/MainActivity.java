package kr.co.aperturedev.petcommunity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ViewFlipper;

import kr.co.aperturedev.petcommunity.view.activitys.DiaryActivity;
import kr.co.aperturedev.petcommunity.view.activitys.GetInformationActivity;
import kr.co.aperturedev.petcommunity.view.activitys.MatchingActivity;

public class MainActivity extends AppCompatActivity {
    ViewFlipper viewFlipper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main2);
        viewFlipper = (ViewFlipper)findViewById(R.id.bannerView);
        viewFlipper.setFlipInterval(1500);
        viewFlipper.startFlipping();
    }

    public void goMatching(View v) {
        Intent intent = new Intent(this, MatchingActivity.class);
        startActivity(intent);
    }
    public void goDiary(View v) {
        Intent intent = new Intent(this, DiaryActivity.class);
        startActivity(intent);
    }
    public void goRecommend(View v) {
        Intent intent = new Intent(this, GetInformationActivity.class);
        startActivity(intent);
    }
}
