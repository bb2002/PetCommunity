package kr.co.aperturedev.petcommunity.modules.imager;

/**
 * Created by 5252b on 2018-03-06.
 */

public interface ImageUploadListener {
    void onUploaded(String link);       // 업로드 성공
    void onUploadFailed(String reason); // 업로드 실패
}
