package kr.co.aperturedev.petcommunity.modules.lib.kakao;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.kakao.auth.ISessionCallback;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;

/**
 * Created by 5252b on 2018-03-09.
 */

public class SessionCallback implements ISessionCallback {
    Context context = null;

    public SessionCallback(Context context) {
        this.context = context;
    }

    @Override
    public void onSessionOpened() {
        UserManagement.requestMe(new MeResponseCallback() {
            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                Log.d("PC", "Kakao Login failed. " + errorResult.getErrorMessage());
            }

            @Override
            public void onNotSignedUp() {

            }

            @Override
            public void onSuccess(UserProfile result) {
                Log.d("PC", "== Kakao Login Success !! ==");
                Log.d("PC", "Nickname : " + result.getNickname());
                Log.d("PC", "Profile : " + result.getProfileImagePath());
                Log.d("PC", "UUID : " + result.getId());

                Toast.makeText(context, result.getNickname() + " 님, 환영합니다!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onSessionOpenFailed(KakaoException exception) {
        Log.d("PC", "Kakao Session Failed. ");
    }
}
