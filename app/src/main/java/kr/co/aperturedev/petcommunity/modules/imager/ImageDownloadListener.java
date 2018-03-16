package kr.co.aperturedev.petcommunity.modules.imager;

import android.graphics.Bitmap;

/**
 * Created by 5252b on 2018-03-16.
 */

public interface ImageDownloadListener {
    void onLoaded(Bitmap image);
}
