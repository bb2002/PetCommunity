package kr.co.aperturedev.petcommunity;

import android.content.Intent;
import android.net.Uri;
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
        viewFlipper.setFlipInterval(3000);
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
    public void banner1Click(View v) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("www.goldntree.co.kr"));
        startActivity(intent);
    }
    public void banner2Click(View v) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.goldntree.co.kr/board/product/list.html?board_no=6"));
        startActivity(intent);
    }
    public void banner3Click(View v) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.goldntree.co.kr/board/free/list.html?board_no=1"));
        startActivity(intent);
    }
}
