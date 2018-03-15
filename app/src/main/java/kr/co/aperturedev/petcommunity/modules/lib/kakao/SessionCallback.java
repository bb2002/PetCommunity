package kr.co.aperturedev.petcommunity.modules.lib.kakao;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.kakao.auth.ISessionCallback;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;

import org.json.JSONException;
import org.json.JSONObject;

import kr.co.aperturedev.petcommunity.MainActivity;
import kr.co.aperturedev.petcommunity.R;
import kr.co.aperturedev.petcommunity.modules.http.RequestHttpListener;
import kr.co.aperturedev.petcommunity.modules.http.RequestHttpTask;
import kr.co.aperturedev.petcommunity.modules.http.RequestURLs;
import kr.co.aperturedev.petcommunity.modules.http.bcr.BCRRequest;
import kr.co.aperturedev.petcommunity.modules.http.bcr.BCRResponse;
import kr.co.aperturedev.petcommunity.view.activitys.UserRegisterActivity;
import kr.co.aperturedev.petcommunity.view.dialogs.progress.ProgressManager;
import kr.co.aperturedev.petcommunity.view.dialogs.window.main.DialogManager;
import kr.co.aperturedev.petcommunity.view.dialogs.window.main.clicklistener.OnYesClickListener;

/**
 * Created by 5252b on 2018-03-09.
 */

public class SessionCallback implements ISessionCallback, RequestHttpListener {
    Activity context = null;     // context
    UserProfile profile = null; // 카카오 로그인 성공시 받는 데이터
    SessionCallback callback = null;
    DialogManager dm = null;
    ProgressManager pm = null;

    public SessionCallback(Activity context) {
        this.context = context;
        this.callback = this;

        this.dm = new DialogManager(context);
        dm.setDescription("Fatal error");
        dm.setOnYesButtonClickListener(new OnYesClickListener() {
            @Override
            public void onClick(DialogInterface dialog) {
                dialog.dismiss();
            }
        }, "OK");
        this.pm = new ProgressManager(context);
    }

    @Override
    public void onSessionOpened() {
        UserManagement.requestMe(new MeResponseCallback() {
            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                Toast.makeText(context, "Kakao login failed.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNotSignedUp() {

            }

            @Override
            public void onSuccess(UserProfile result) {
                profile = result;   // 전역으로 올려준다.

                BCRRequest request = new BCRRequest();
                request.addArgs("target-uuid", result.getId());
                RequestHttpTask task = new RequestHttpTask(RequestURLs.IS_REGIST_SERVICE, request, context, callback);
                task.execute();

                pm.setMessage(context.getString(R.string.default_process));
                pm.enable();
            }
        });
    }

    @Override
    public void onSessionOpenFailed(KakaoException exception) {
    }

    @Override
    public void onResponse(int responseCode, BCRResponse response) {
        pm.disable();

        if(responseCode == 200) {
            // 처리 성공
            try {
                JSONObject respObj = response.getData();
                Intent intent = null;
                if (respObj.getBoolean("is-registed")) {
                    // 회원가입 됨.
                    intent = new Intent(context, MainActivity.class);
                    context.startActivity(intent);
                } else {
                    // 회원가입 안됨.
                    if(profile == null) {
                        Toast.makeText(context, "Fatal error", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    intent = new Intent(context, UserRegisterActivity.class);
                    intent.putExtra("user-nickname", profile.getNickname());
                    intent.putExtra("user-profile", profile.getProfileImagePath());
                    intent.putExtra("user-id", profile.getId());
                    context.startActivity(intent);  // 회원가입 화면을 띄운다.
                }

                context.finish();

            } catch(JSONException jex) {
                jex.printStackTrace();
                dm.setDescription("Client error.\n" + jex.getMessage());
            }
        } else {
            // 처리 실패
            dm.setDescription("Failed.\n" + response.getResponseMsg());
            dm.show();
        }
    }
}
