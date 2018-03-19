package kr.co.aperturedev.petcommunity.view.fragments.pages.matching;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import kr.co.aperturedev.petcommunity.R;
import kr.co.aperturedev.petcommunity.modules.imager.ImageDownloadListener;
import kr.co.aperturedev.petcommunity.modules.imager.ImageDownloadTask;
import kr.co.aperturedev.petcommunity.view.fragments.PageActivity;
import kr.co.aperturedev.petcommunity.view.fragments.PageSuper;

/**
 * Created by 5252b on 2018-03-18.
 */

public class MatchedFragment extends PageSuper {
    TextView petName = null;        // 강아지 이름 뷰
    TextView petAge = null;         // 강아지 나이
    Button cancelButton = null;     // 취소 버튼
    Button applyButton = null;      // 승인 버튼
    ImageView petPreview = null;    // 강아지 프리뷰

    JSONObject petObj = null;

    public MatchedFragment(Context context, PageActivity control, String petObject) throws JSONException {
        super(context, control);
        setView(R.layout.page_matched_show);

        // 개체 찾기
        this.petName = findViewById(R.id.matched_pet_name);
        this.petAge = findViewById(R.id.matched_pet_age);
        this.cancelButton = findViewById(R.id.matched_cancel);
        this.applyButton = findViewById(R.id.matched_apply);
        this.petPreview = findViewById(R.id.matched_pet_preview);

        this.petObj = new JSONObject(petObject);

        // 데이터 뷰
        try {
            this.petName.setText(petObj.getString("pet-name"));
            this.petAge.setText(petObj.getString("pet-age"));
        } catch(JSONException jex) {
            Toast.makeText(getContext(), "Fatal error.", Toast.LENGTH_SHORT).show();
            jex.printStackTrace();
            return;
        }

        // 이미지 다운로드
        ImageDownloadTask downTask = new ImageDownloadTask(this.petObj.getString("pet-image"), new ImageDownloadListener() {
            @Override
            public void onLoaded(Bitmap image) {
                petPreview.setImageBitmap(image);
            }
        }, getContext());
        downTask.execute();
    }
}
