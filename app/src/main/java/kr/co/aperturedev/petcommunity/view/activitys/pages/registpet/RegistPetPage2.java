package kr.co.aperturedev.petcommunity.view.activitys.pages.registpet;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.IOException;

import kr.co.aperturedev.petcommunity.R;
import kr.co.aperturedev.petcommunity.modules.constant.QueryIDs;
import kr.co.aperturedev.petcommunity.modules.lib.PermissionHelper;
import kr.co.aperturedev.petcommunity.modules.uploader.ImageUploadListener;
import kr.co.aperturedev.petcommunity.modules.uploader.ImageUploadTask;
import kr.co.aperturedev.petcommunity.view.activitys.pages.PageActivity;
import kr.co.aperturedev.petcommunity.view.activitys.pages.PageSuper;

/**
 * Created by 5252b on 2018-03-06.
 */

public class RegistPetPage2 extends PageSuper {
    View v = null;

    ImageButton upload = null;

    public RegistPetPage2(final Context context, PageActivity control) {
        super(context, control);
        setView(R.layout.page_registpet_2);

        this.v = getView();
        this.upload = this.v.findViewById(R.id.upload_image_button);

        this.upload.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 권한 확인
                if(!PermissionHelper.checkPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    // 권한이 없다면 요청한다.
                    PermissionHelper.requestPermission(getControl(), Manifest.permission.READ_EXTERNAL_STORAGE);
                    return;
                }

                // 이미지를 직접 가져옵니다.
                Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                Intent intent = new Intent(Intent.ACTION_PICK, uri);
                getControl().startActivityForResult(intent, QueryIDs.SELECT_PICTURE_BROWSER);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap image = null;

        if(data == null) return;

        try {
            switch (requestCode) {
                case QueryIDs.SELECT_PICTURE_BROWSER:
                    image = MediaStore.Images.Media.getBitmap(getControl().getContentResolver(), data.getData());
                    break;
                default:
                    return;
            }

            // 이미지를 업로드 합시다.
            ImageUploadTask uploadTask = new ImageUploadTask(image, new FileUploadHandler());
            uploadTask.execute();

            this.upload.setEnabled(false);
        } catch(IOException iex) {
            Toast.makeText(getContext(), "IOException! " + iex.getMessage(), Toast.LENGTH_SHORT).show();
        } catch(Exception ex) {
            ex.printStackTrace();
            Toast.makeText(getContext(), "Failed. " + ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
