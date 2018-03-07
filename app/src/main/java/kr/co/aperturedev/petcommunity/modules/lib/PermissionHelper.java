package kr.co.aperturedev.petcommunity.modules.lib;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.accessibility.AccessibilityManager;
import android.widget.Toast;

import java.util.List;

import kr.co.aperturedev.petcommunity.modules.constant.QueryIDs;


/**
 * Created by 5252b on 2018-02-06.
 */

public class PermissionHelper {
    public static OnPermissionResultHandler handler = null;

    public static boolean checkPermission(Context context, String permission) {
        int result = ContextCompat.checkSelfPermission(context, permission);

        if(result == PackageManager.PERMISSION_DENIED) {
            return false;
        } else {
            return true;
        }
    }

    public static void requestPermission(Activity activity, String permission) {
        ActivityCompat.requestPermissions(activity, new String[]{ permission }, QueryIDs.REQUEST_PERMISSION_RESULT);
    }

    public interface OnPermissionResultHandler {
       void onResult(boolean allow, String code);
    }
}
