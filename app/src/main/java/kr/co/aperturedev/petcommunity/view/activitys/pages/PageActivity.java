package kr.co.aperturedev.petcommunity.view.activitys.pages;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by 5252b on 2018-03-02.
 */

public abstract class PageActivity extends AppCompatActivity {
    public abstract void switchPage(PageSuper page);
}
