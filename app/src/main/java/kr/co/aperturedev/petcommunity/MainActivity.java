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
    String titles1="";
    int num;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main2);

        /*
        메인스레드에서 네트워크 부분은 처리 하면 안됩니다.

        try {
            board1 = Jsoup.connect("http://www.goldntree.co.kr/board/free/list.html?board_no=1").get();
            element1 = board1.select("td.subject");
        } catch (IOException e) {
            e.printStackTrace();
        }
        titles1="";
        num=0;
        for(Element element:element1) {
            titles1 += num+element.text()+"\n";
            num++;
            if(num==10)
                break;
        }
        board1View = (TextView)findViewById(R.id.mainNumber);
        board1View.setText(titles1);
        //뷰플리퍼실행
        viewFlipper = (ViewFlipper)findViewById(R.id.bannerView);
        viewFlipper.setFlipInterval(3000);
        viewFlipper.startFlipping(); //뷰플리퍼3초간격슬라이드
        */
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
