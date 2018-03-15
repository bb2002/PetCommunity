package kr.co.aperturedev.petcommunity;

import android.content.DialogInterface;
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

import kr.co.aperturedev.petcommunity.modules.http.RequestHttpListener;
import kr.co.aperturedev.petcommunity.modules.http.RequestHttpTask;
import kr.co.aperturedev.petcommunity.modules.http.RequestURLs;
import kr.co.aperturedev.petcommunity.modules.http.bcr.BCRRequest;
import kr.co.aperturedev.petcommunity.modules.http.bcr.BCRResponse;
import kr.co.aperturedev.petcommunity.view.activitys.DiaryActivity;
import kr.co.aperturedev.petcommunity.view.activitys.GetInformationActivity;
import kr.co.aperturedev.petcommunity.view.activitys.MatchingActivity;
import kr.co.aperturedev.petcommunity.view.activitys.SocialLoginAcvtivity;
import kr.co.aperturedev.petcommunity.view.activitys.UserRegisterActivity;
import kr.co.aperturedev.petcommunity.view.dialogs.progress.ProgressManager;
import kr.co.aperturedev.petcommunity.view.dialogs.window.main.DialogManager;
import kr.co.aperturedev.petcommunity.view.dialogs.window.main.clicklistener.OnYesClickListener;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity implements RequestHttpListener {
    Button matchButton = null;
    MainActivity me = null;

    DialogManager dm = null;    // 다이얼로그 매니저
    ProgressManager pm = null;  // 프로그레스 매니저

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main2);

        this.me = this;
        this.matchButton = findViewById(R.id.dogMatching);
        this.matchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 매칭버튼누를때
                Intent intent = new Intent(getApplicationContext(), MatchingActivity.class);
                startActivity(intent);
            }
        });

        /*
            자동 로그인 한다.
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
                String pin = prep.getString("user-pin", null);
                if(pin == null) {
                    // 빈 깡통 계정.
                    // 회원가입 화면으로 이동한다.
                    Intent intent = new Intent(me, UserRegisterActivity.class);
                    startActivity(intent);  // 회원가입 화면을 띄운다.

                    finish();
                } else {
                    // 해당 계정이 로그인 되어 있는지 확인

                    BCRRequest req = new BCRRequest();
                    req.addArgs("user-pin", pin);
                    RequestHttpTask loginTask = new RequestHttpTask(RequestURLs.LOGIN_SERVICE, req, getApplicationContext(), me);
                    loginTask.execute();
                }
            }
        });

        this.dm = new DialogManager(this);
        this.dm.setTitle("Fatal error");
        this.dm.setOnYesButtonClickListener(new OnYesClickListener() {
            @Override
            public void onClick(DialogInterface dialog) {
                dialog.dismiss();
                finish();
            }
        }, "CLOSE");
    }

    @Override
    public void onResponse(int responseCode, BCRResponse response) {
        // 자동 로그인에 대한 결과
        if(responseCode == 200) {
            JSONObject obj = response.getData();

            try {
                boolean isLogin = obj.getBoolean("is-login");

                if(isLogin) {
                    // 자동 로그인 성공

                } else {
                    // 자동 로그인 오류
                    dm.setDescription("Auth error. Please login.");
                    dm.show();

                    SharedPreferences.Editor editor = getSharedPreferences("PetCommunity", MODE_PRIVATE).edit();
                    editor.putString("user-pin", null);
                    editor.commit();    // PIN 초기화
                }
            } catch(JSONException jex) {
                jex.printStackTrace();
                dm.setDescription("Client error\n" + jex.getMessage());
                dm.show();
            }
        } else {
            // 로그인 실패
            dm.setDescription("자동 로그인에 실패했습니다.\n" + response);
            dm.show();
        }
    }
}


