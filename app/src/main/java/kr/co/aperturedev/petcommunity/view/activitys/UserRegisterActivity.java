package kr.co.aperturedev.petcommunity.view.activitys;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;

import org.json.JSONException;
import org.json.JSONObject;

import kr.co.aperturedev.petcommunity.MainActivity;
import kr.co.aperturedev.petcommunity.R;
import kr.co.aperturedev.petcommunity.modules.http.RequestHttpListener;
import kr.co.aperturedev.petcommunity.modules.http.RequestHttpTask;
import kr.co.aperturedev.petcommunity.modules.http.RequestURLs;
import kr.co.aperturedev.petcommunity.modules.http.bcr.BCRRequest;
import kr.co.aperturedev.petcommunity.modules.http.bcr.BCRResponse;
import kr.co.aperturedev.petcommunity.view.dialogs.progress.ProgressManager;
import kr.co.aperturedev.petcommunity.view.dialogs.window.main.DialogManager;
import kr.co.aperturedev.petcommunity.view.dialogs.window.main.clicklistener.OnYesClickListener;

/**
 * Created by 5252b on 2018-03-14.
 */

public class UserRegisterActivity extends AppCompatActivity {
    EditText nicknameEdit = null;
    Button uploadProfile = null;
    Button nextButton = null;
    EditText nameEdit = null;
    RadioGroup gender = null;
    RadioButton[] genders = null;

    String kakaoUUID = null;    // 카카오톡 uuid
    String profileImage = null; // 프로필사진 링크 (기본 카카오 로그인 값)

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist_user);
        getSupportActionBar().hide();

        // 객체 찾기
        this.nicknameEdit = findViewById(R.id.form_nickname);
        this.uploadProfile = findViewById(R.id.form_profile_upload);
        this.nameEdit = findViewById(R.id.form_name);
        this.gender = findViewById(R.id.form_gender);
        this.genders = new RadioButton[] {
                findViewById(R.id.form_user_man),
                findViewById(R.id.form_user_woman)
        };
        this.nextButton = findViewById(R.id.user_regist_ok);

        // 리스너 정의
        EditTextWatcher textWatcher = new EditTextWatcher();
        this.nicknameEdit.addTextChangedListener(textWatcher);
        this.nameEdit.addTextChangedListener(textWatcher);
        ButtonClickHandler buttonClick = new ButtonClickHandler(this);
        this.nextButton.setOnClickListener(buttonClick);
        this.uploadProfile.setOnClickListener(buttonClick);

        // 기본값 정의
        genders[0].setChecked(true);
        UserManagement.requestMe(new MeResponseCallback() {
            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                /*
                    세션이 없다. 소셜로그인 화면으로 이동
                 */
                DialogManager dm = new DialogManager(getApplicationContext());
                dm.setTitle("Kakao Login Error");
                dm.setDescription(getString(R.string.register_kakaologin_failed));
                dm.setOnYesButtonClickListener(new OnYesClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog) {
                        dialog.dismiss();

                        finish();
                    }
                }, "OK");
                dm.show();
            }

            @Override
            public void onNotSignedUp() {
            }

            @Override
            public void onSuccess(UserProfile profile) {
                /*
                    로그인 데이터를 가져온다.
                 */
                nicknameEdit.setText(profile.getNickname());
                kakaoUUID = profile.getId() + "";
                profileImage = profile.getProfileImagePath();

                checkNextButton();
            }
        });
    }

    /*
        텍스트 에디터
     */
    class EditTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            checkNextButton();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    /*
        버튼 클릭 리스너
     */
    class ButtonClickHandler implements View.OnClickListener, RequestHttpListener {
        ProgressManager pm = null;
        DialogManager dm = null;
        public ButtonClickHandler(Context context) {
            this.pm = new ProgressManager(context);
            this.dm = new DialogManager(context);
            this.dm.setTitle("Fatal error");
            this.dm.setOnYesButtonClickListener(new OnYesClickListener() {
                @Override
                public void onClick(DialogInterface dialog) {
                    dialog.dismiss();
                }
            }, "OK");
        }

        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.form_profile_upload:
                    break;
                case R.id.user_regist_ok:
                    // 데이터를 다시 확인합니다
                    if(nicknameEdit.getText().length() == 0) return;
                    if(nameEdit.getText().length() == 0) return;
                    if(profileImage == null) return;

                    boolean isGenderSelected = genders[0].isChecked() || genders[1].isChecked();
                    if(!isGenderSelected) return;

                    BCRRequest request = new BCRRequest();
                    request.addArgs("user-uuid", kakaoUUID);
                    request.addArgs("user-profile", profileImage);
                    request.addArgs("user-name", nameEdit.getText().toString());
                    request.addArgs("user-nickname", nicknameEdit.getText().toString());
                    request.addArgs("user-gender", genders[0].isChecked() ? "man" : "woman");

                    // 회원가입 테스크 가동
                    RequestHttpTask task = new RequestHttpTask(RequestURLs.REGIST_SERVICE, request, getApplicationContext(), this);
                    task.execute();

                    this.pm.setMessage(getString(R.string.default_process));
                    this.pm.enable();
                    break;
            }
        }

        @Override
        public void onResponse(int responseCode, BCRResponse response) {
            this.pm.disable();

            if(responseCode == 200) {
                Toast.makeText(getApplicationContext(), "가입되었습니다.", Toast.LENGTH_SHORT).show();

                // 가입 후 뒷처리 한다.
                SharedPreferences prep = getSharedPreferences("PetCommunity", MODE_PRIVATE);
                SharedPreferences.Editor editor = prep.edit();

                JSONObject resp = response.getData();
                try {
                    editor.putString("user-pin", resp.getString("user-pin"));
                    editor.commit();
                } catch(JSONException jex) {
                    Toast.makeText(getApplicationContext(), "Fatal error", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                // 오류
                this.dm.setDescription("Register failed.\n" + response.getResponseMsg());
                this.dm.show();
            }
        }
    }

    /*
        '다음' 버튼을 열지 말지 생각합니다.
     */
    private void checkNextButton() {
        if(nicknameEdit.getText().length() == 0) {
            nextButton.setVisibility(View.INVISIBLE);
            return;
        }

        if(nameEdit.getText().length() == 0) {
            nextButton.setVisibility(View.INVISIBLE);
            return;
        }

        if(profileImage == null) {
            nextButton.setVisibility(View.INVISIBLE);
            return;
        }

        nextButton.setVisibility(View.VISIBLE);
    }
}
