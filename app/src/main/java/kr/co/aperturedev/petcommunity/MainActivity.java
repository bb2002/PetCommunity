package kr.co.aperturedev.petcommunity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;

import kr.co.aperturedev.petcommunity.view.activitys.DiaryActivity;
import kr.co.aperturedev.petcommunity.view.activitys.GetInformationActivity;
import kr.co.aperturedev.petcommunity.view.activitys.MatchingActivity;
import kr.co.aperturedev.petcommunity.view.activitys.SocialLoginAcvtivity;
import kr.co.aperturedev.petcommunity.view.activitys.UserRegisterActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {
    ViewFlipper viewFlipper;
    WebView webView;
    TextView board1View;
    Document board1;
    Elements element1;
    String titles1="";
    int num;

    Button matchButton = null;
    MainActivity me = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main2);
        //뷰플리퍼실행
        viewFlipper = (ViewFlipper)findViewById(R.id.bannerView);
        viewFlipper.setFlipInterval(3000);
        viewFlipper.startFlipping(); //뷰플리퍼3초간격슬라이드

        this.me = this;
        this.matchButton = findViewById(R.id.dogMatching);
        this.matchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 매칭버튼누를때
                Intent intent = new Intent(getApplicationContext(), MatchingActivity.class);
                startActivity(intent);

//                UserManagement.requestLogout(new LogoutResponseCallback() {
//                    @Override
//                    public void onCompleteLogout() {
//
//                    }
//                });
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        /*
            로그인 되어 있는지 확인한다.
         */
        UserManagement.requestMe(new MeResponseCallback() {
            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                /*
                    세션이 없다. 소셜로그인 화면으로 이동
                 */
                Intent intent = new Intent(getApplicationContext(), SocialLoginAcvtivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onNotSignedUp() {
                Log.d("PC", "NO");
            }

            @Override
            public void onSuccess(UserProfile profile) {
                /*
                    로그인 성공!
                    이 계정이 로그인 되어 있는지 확인한다.
                 */
                SharedPreferences prep = getSharedPreferences("PetCommunity", MODE_PRIVATE);
                boolean isRegist = prep.getBoolean("is-registed", false);
                if(!isRegist) {
                    // 빈 깡통 계정.
                    // 회원가입 화면으로 이동한다.
                    Intent intent = new Intent(me, UserRegisterActivity.class);
                    intent.putExtra("user-nickname", profile.getNickname());
                    intent.putExtra("user-profile", profile.getProfileImagePath());
                    intent.putExtra("user-id", profile.getId());
                    startActivity(intent);  // 회원가입 화면을 띄운다.

                    finish();
                }
            }
        });
    }

    protected void goDiary(View v) {
        //다이어리버튼누를때
        Intent intent = new Intent(this, DiaryActivity.class);
        startActivity(intent);
        finish();//다이어리로이동
    }
    protected void goRecommend(View v) {
        //추천버튼누를때
        Intent intent = new Intent(this, GetInformationActivity.class);
        startActivity(intent);
        finish();//추천으로이동
    }
    protected void banner1Click(View v) {
        //첫배너클릭
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.goldntree.co.kr"));
        startActivity(intent);//웹띄우기
    }
    protected void banner2Click(View v) {
        //둘째배너클릭
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.goldntree.co.kr/board/product/list.html?board_no=6"));
        startActivity(intent);//웹띄우기
    }
    protected void banner3Click(View v) {
        //세번째배너클릭
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.goldntree.co.kr/board/free/list.html?board_no=1"));
        startActivity(intent);//웹띄우기
    }
}
