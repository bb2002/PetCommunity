package kr.co.aperturedev.petcommunity.view.activitys;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

import kr.co.aperturedev.petcommunity.R;
import kr.co.aperturedev.petcommunity.view.activitys.pages.PageActivity;
import kr.co.aperturedev.petcommunity.view.activitys.pages.PageSuper;
import kr.co.aperturedev.petcommunity.view.activitys.pages.registpet.RegistPetPage1;

/**
 * Copyright(c) 2018 Saint software All rights reserved.
 * 애견을 등록하는 페이지
 */

public class RegistPetActivity extends PageActivity {
    FrameLayout contentView = null;     // 컨텐츠 뷰
    Animation anime = null;             // 화면 전환시 사용될 애니
    PageSuper nowPage = null;           // 현재 페이지

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist_matching);
        getSupportActionBar().hide();

        // 개체 찾기
        this.contentView = findViewById(R.id.registmatch_container);
        this.anime = AnimationUtils.loadAnimation(this, R.anim.view_move_right);

        // 페이지 열기
        switchPage(new RegistPetPage1(this, this));
    }

    @Override
    public void switchPage(PageSuper page) {
        contentView.removeAllViews();

        contentView.addView(page);
        this.nowPage = page;
        contentView.startAnimation(this.anime);

        page.onShow();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(this.nowPage != null) {
            this.nowPage.onResume();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        if(this.nowPage != null) {
            this.nowPage.onStop();
        }
    }
}
