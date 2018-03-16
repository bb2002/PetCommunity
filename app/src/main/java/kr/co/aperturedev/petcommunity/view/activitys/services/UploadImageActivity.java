package kr.co.aperturedev.petcommunity.view.activitys.services;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import kr.co.aperturedev.petcommunity.R;
import kr.co.aperturedev.petcommunity.modules.constant.QueryIDs;
import kr.co.aperturedev.petcommunity.modules.imager.ImageDownloadListener;
import kr.co.aperturedev.petcommunity.modules.imager.ImageDownloadTask;
import kr.co.aperturedev.petcommunity.modules.imager.ImageUploadListener;
import kr.co.aperturedev.petcommunity.modules.imager.ImageUploadTask;
import kr.co.aperturedev.petcommunity.modules.lib.PermissionHelper;
import kr.co.aperturedev.petcommunity.view.dialogs.progress.ProgressManager;
import kr.co.aperturedev.petcommunity.view.dialogs.window.main.DialogManager;
import kr.co.aperturedev.petcommunity.view.dialogs.window.main.clicklistener.OnYesClickListener;

/**
 * Created by 5252b on 2018-03-16.
 */

public class UploadImageActivity extends AppCompatActivity {
    ProgressManager pm = null;  // 업로드 중일때 표시한다.
    DialogManager dm = null;    // 오류 발생시 사용한다.
    UploadImageActivity me = null;  // 자기 자신

    ImageView imagePreview = null;
    Button cancelButton = null;
    Button uploadButton = null;
    TextView statusView = null;

    String fileName = null;     // 업로드한 이미지

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_image);
        getSupportActionBar().hide();

        this.me = this;
        this.pm = new ProgressManager(this);
        this.dm = new DialogManager(this);
        this.dm.setTitle("Fatal error");

        // 객체 찾기
        this.imagePreview = findViewById(R.id.upload_image_preview);
        this.cancelButton = findViewById(R.id.cancel_button);
        this.uploadButton = findViewById(R.id.upload_button);
        this.statusView = findViewById(R.id.upload_status_view);

        // 이벤트 핸들링
        OnButtonClickHandler handler = new OnButtonClickHandler();
        this.cancelButton.setOnClickListener(handler);
        this.uploadButton.setOnClickListener(handler);
        this.dm.setOnYesButtonClickListener(new OnYesClickListener() {
            @Override
            public void onClick(DialogInterface dialog) {
                dialog.dismiss();

                // 닫는다.
                setResult(RESULT_CANCELED);
                finish();
            }
        }, "OK");

        // 권한 요청
        if(!PermissionHelper.checkPermission(me, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            // 권한이 없다면 요청한다.
            PermissionHelper.requestPermission(me, Manifest.permission.READ_EXTERNAL_STORAGE);
            return;
        }
    }

    /*
        업로드한 이미지를 리턴합니다.
     */
    @Override
    public void onBackPressed() {
        if(fileName == null) {
            Toast.makeText(getApplicationContext(), "이미지를 업로드 해 주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent();
        intent.putExtra("file-name", fileName);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == QueryIDs.REQUEST_PERMISSION_RESULT) {
            // 퍼미션 요청에 대한 결과

            if(!PermissionHelper.checkPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // 권한을 거부했다.
                dm.setDescription("Permission deny!");
                dm.show();
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == QueryIDs.SELECT_PICTURE_BROWSER && resultCode == RESULT_OK) {
            // 이미지가 선택되었다면 업로드 테스크를 구동한다.
            try {
                Bitmap image = MediaStore.Images.Media.getBitmap(me.getContentResolver(), data.getData());

                ImageUploadTask task = new ImageUploadTask(image, new ImageCallbackHandler());
                task.execute();

                // 업로드가 시작됨.
                this.uploadButton.setText(R.string.upload_now_loading);
                this.uploadButton.setEnabled(false);
                this.statusView.setVisibility(View.VISIBLE);
                this.statusView.setText(R.string.upload_now_loading_kr);
            } catch(IOException iex) {
                dm.setDescription("IOException\n" + iex.getMessage());
                dm.show();
                iex.printStackTrace();
            }
        }
    }

    class OnButtonClickHandler implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.cancel_button:
                    setResult(RESULT_CANCELED);
                    finish();
                    break;
                case R.id.upload_button:
                    clickUploadButton();
                    break;
            }
        }

        private void clickUploadButton() {
            if(!PermissionHelper.checkPermission(me, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // 권한이 없다면 요청한다.
                PermissionHelper.requestPermission(me, Manifest.permission.READ_EXTERNAL_STORAGE);
                return;
            }

            // 앨범에서 이미지를 선택합니다.
            Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            Intent intent = new Intent(Intent.ACTION_PICK, uri);
            me.startActivityForResult(intent, QueryIDs.SELECT_PICTURE_BROWSER);
        }
    }

    class ImageCallbackHandler implements ImageDownloadListener, ImageUploadListener {
        /*
            이미지가 업로드 되었습니다.
         */
        @Override
        public void onUploaded(String link) {
            // 업로드 되었다.
            // 프리뷰를 위해 이미지를 로드한다.
            ImageDownloadTask downTask = new ImageDownloadTask(link, this, me);
            downTask.execute();

            statusView.setText(R.string.upload_check_uploaded);
            fileName = link;

            // 업로드 완료
            Snackbar.make(getWindow().getDecorView().getRootView(), R.string.upload_success_msg, 3000).setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.setVisibility(View.INVISIBLE);
                }
            }).show();
        }

        /*
            이미지 업로드 실패
         */
        @Override
        public void onUploadFailed(String reason) {
            dm.setDescription("Can not upload image!\n" + reason);
            dm.show();

            // 오류 발생시 재시도 가능하게 함
            uploadButton.setText(R.string.upload_retry);
            uploadButton.setEnabled(true);
            statusView.setText(reason);
        }

        /*
            업로드한 이미지 로드 완료
         */
        @Override
        public void onLoaded(Bitmap image) {
            imagePreview.setImageBitmap(image);

            // 다시 업로드 및 스테이터스 초기화
            statusView.setVisibility(View.INVISIBLE);
            uploadButton.setText(R.string.upload_retry);
            uploadButton.setEnabled(true);
        }
    }
}
