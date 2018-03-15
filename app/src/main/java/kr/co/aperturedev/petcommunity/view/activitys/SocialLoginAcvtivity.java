package kr.co.aperturedev.petcommunity.view.activitys;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;

import kr.co.aperturedev.petcommunity.R;
import kr.co.aperturedev.petcommunity.modules.lib.kakao.SessionCallback;

/**
 * Created by 5252b on 2018-02-28.
 */

public class SocialLoginAcvtivity extends AppCompatActivity {
    SessionCallback callback = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sociallogin);
        getSupportActionBar().hide();

        callback = new SessionCallback(this);
        Session.getCurrentSession().addCallback(callback);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
