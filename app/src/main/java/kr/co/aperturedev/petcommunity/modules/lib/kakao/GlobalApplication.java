package kr.co.aperturedev.petcommunity.modules.lib.kakao;

import android.app.Activity;
import android.app.Application;

import com.kakao.auth.KakaoSDK;

/**
 * Created by 5252b on 2018-03-09.
 */

public class GlobalApplication extends Application {
    private static volatile GlobalApplication obj = null;
    private static volatile Activity currectActivity = null;

    @Override
    public void onCreate() {
        super.onCreate();
        obj = this;
        KakaoSDK.init(new KakaoSDKAdapter());
    }

    public static GlobalApplication getGlobalAppContext() {
        return obj;
    }

    public static Activity getCurrectActivity() {
        return currectActivity;
    }

    public static void setCurrectActivity(Activity activity) {
        GlobalApplication.currectActivity = currectActivity;
    }
}
