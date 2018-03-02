package kr.co.aperturedev.petcommunity;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import kr.co.aperturedev.petcommunity.view.activitys.DiaryActivity;
import kr.co.aperturedev.petcommunity.view.activitys.GetInformationActivity;
import kr.co.aperturedev.petcommunity.view.activitys.MatchingActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    ViewFlipper viewFlipper;
    WebView webView;
    TextView board1View;
    Document board1;
    Elements element1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main2);
        try {
            board1 = Jsoup.connect("http://www.goldntree.co.kr/board/free/list.html?board_no=1").get();
            element1 = board1.select("span.subject");
        } catch (IOException e) {
            e.printStackTrace();
        }

        board1View = (TextView)findViewById(R.id.mainNumber);
        board1View.setText(element1.text());
        //뷰플리퍼실행
        viewFlipper = (ViewFlipper)findViewById(R.id.bannerView);
        viewFlipper.setFlipInterval(3000);
        viewFlipper.startFlipping(); //뷰플리퍼3초간격슬라이드

    }

    public void goMatching(View v) {
        //매칭버튼누를때
        Intent intent = new Intent(this, MatchingActivity.class);
        startActivity(intent);
        finish();//매칭으로이동
    }
    public void goDiary(View v) {
        //다이어리버튼누를때
        Intent intent = new Intent(this, DiaryActivity.class);
        startActivity(intent);
        finish();//다이어리로이동
    }
    public void goRecommend(View v) {
        //추천버튼누를때
        Intent intent = new Intent(this, GetInformationActivity.class);
        startActivity(intent);
        finish();//추천으로이동
    }
    public void banner1Click(View v) {
        //첫배너클릭
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.goldntree.co.kr"));
        startActivity(intent);//웹띄우기
    }
    public void banner2Click(View v) {
        //둘째배너클릭
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.goldntree.co.kr/board/product/list.html?board_no=6"));
        startActivity(intent);//웹띄우기
    }
    public void banner3Click(View v) {
        //세번째배너클릭
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.goldntree.co.kr/board/free/list.html?board_no=1"));
        startActivity(intent);//웹띄우기
    }
}
