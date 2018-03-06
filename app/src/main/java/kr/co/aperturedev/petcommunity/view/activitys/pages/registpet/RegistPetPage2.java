package kr.co.aperturedev.petcommunity.view.activitys.pages.registpet;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import kr.co.aperturedev.petcommunity.R;
import kr.co.aperturedev.petcommunity.modules.uploader.ImageUploadListener;
import kr.co.aperturedev.petcommunity.modules.uploader.ImageUploadTask;
import kr.co.aperturedev.petcommunity.view.activitys.pages.PageActivity;
import kr.co.aperturedev.petcommunity.view.activitys.pages.PageSuper;

/**
 * Created by 5252b on 2018-03-06.
 */

public class RegistPetPage2 extends PageSuper {
    View v = null;

    Button upload = null;

    public RegistPetPage2(Context context, PageActivity control) {
        super(context, control);
        setView(R.layout.page_registpet_2);

        this.v = getView();
        this.upload = this.v.findViewById(R.id.upload_image_button);

        this.upload.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap testImage = BitmapFactory.decodeResource(getControl().getResources(), R.drawable.test_2);

                Toast.makeText(getContext(), "파일 업로드를 시작합니다.", Toast.LENGTH_SHORT).show();

                ImageUploadTask task = new ImageUploadTask(testImage, new FileUploadHandler());
                task.execute();
            }
        });
    }

    class FileUploadHandler implements ImageUploadListener {
        @Override
        public void onUploaded(String link) {
            Log.d("PC", "업로드 성공 : " + link);
        }

        @Override
        public void onUploadFailed(String reason) {
            Log.d("PC", "업로드 실패 : " + reason);
        }

        @Override
        public void onPacketSended(int percent) {
            Log.d("PC", "업로드 중 : " +  percent);
        }
    }

}
