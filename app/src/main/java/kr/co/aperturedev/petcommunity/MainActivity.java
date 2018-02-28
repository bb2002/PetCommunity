package kr.co.aperturedev.petcommunity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import kr.co.aperturedev.petcommunity.view.activitys.SocialLoginAcvtivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 소셜 로그인 화면을 연다.
        Intent openSocialLogin = new Intent(this, SocialLoginAcvtivity.class);
        startActivity(openSocialLogin);
    }
}
