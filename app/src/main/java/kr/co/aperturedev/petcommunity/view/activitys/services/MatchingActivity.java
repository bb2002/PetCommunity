package kr.co.aperturedev.petcommunity.view.activitys.services;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import kr.co.aperturedev.petcommunity.R;
import kr.co.aperturedev.petcommunity.view.dialogs.progress.ProgressManager;

/**
 * Copyright (c) 2018 Saint software All rights reserved.
 * 애견 매칭 메인 화면
 */

public class MatchingActivity extends AppCompatActivity {
    TextView registedCount = null;  // 현재 매칭 가능한 팻 들
    Button startMatching = null;    // 매칭 시작 버튼
    Button registMyPet = null;      // 내 팻 등록 버튼

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_macthing);
        getSupportActionBar().hide();

        // 개체 참조
        registedCount = findViewById(R.id.matching_registed_count);
        startMatching = findViewById(R.id.matching_match_pet);
        registMyPet = findViewById(R.id.matching_regist_pet);

        // 이벤트 핸들링
        ButtonClickHandler handler = new ButtonClickHandler(this);
        startMatching.setOnClickListener(handler);
        registMyPet.setOnClickListener(handler);
    }

    /*
        버튼 핸들러
     */
    class ButtonClickHandler implements View.OnClickListener {
        ProgressManager progressMgr = null;

        public ButtonClickHandler(Context context) {
            this.progressMgr = new ProgressManager(context);
        }

        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.matching_match_pet:
                    // 애견 매칭을 선택
                    this.progressMgr.setMessage(getString(R.string.default_now_matching));
                    this.progressMgr.enable();

                    // 매칭 테스크 구동

                    break;
                case R.id.matching_regist_pet:
                    // 내 팻 등록 버튼
                    Intent intent = new Intent(getApplicationContext(), RegistPetActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    }
}
