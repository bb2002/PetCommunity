package kr.co.aperturedev.petcommunity.view.fragments.pages.registpet;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import kr.co.aperturedev.petcommunity.R;
import kr.co.aperturedev.petcommunity.modules.constant.QueryIDs;
import kr.co.aperturedev.petcommunity.view.activitys.services.UploadImageActivity;
import kr.co.aperturedev.petcommunity.view.fragments.PageActivity;
import kr.co.aperturedev.petcommunity.view.fragments.PageSuper;

/**
 * Created by 5252b on 2018-03-06.
 */

public class RegistPetPage2 extends PageSuper {
    View v = null;

    ImageButton upload = null;      // 업로드 이미지 버튼
    Button nextButton = null;       // 다음 버튼
    TextView statusView = null;     // 스텟 뷰

    String uploadedUrl = null;      // 파일 업로드 후 URL

    public RegistPetPage2(final Context context, PageActivity control) {
        super(context, control);
        setView(R.layout.page_registpet_2);

        this.v = getView();
        this.upload = this.v.findViewById(R.id.upload_image_button);
        this.nextButton = this.v.findViewById(R.id.registpet2_next);
        this.statusView = this.v.findViewById(R.id.upload_prep_msg);

        OnButtonClickHandler handler = new OnButtonClickHandler();
        this.nextButton.setOnClickListener(handler);
        this.upload.setOnClickListener(handler);
    }

    class OnButtonClickHandler implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.upload_image_button:
                    Intent intent = new Intent(getContext(), UploadImageActivity.class);
                    getControl().startActivityForResult(intent, QueryIDs.UPLOAD_IMAGE_RESULT);
                    break;
                case R.id.registpet2_next:
                    getControl().switchPage(new RegistPetPage3(getContext(), getControl()));
                    getControl().args.add(uploadedUrl);
                    break;  // 다음 페이지로 넘어갑니다.
            }
        }
    }

    /*
        이미지 업로드 완료.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != Activity.RESULT_OK) return;

        switch (requestCode) {
            case QueryIDs.UPLOAD_IMAGE_RESULT:
                this.uploadedUrl = data.getStringExtra("file-name");
                this.nextButton.setVisibility(VISIBLE);
                this.statusView.setText(R.string.registpet2_uploaded);
                break;
            default:
                return;
        }
    }
}
