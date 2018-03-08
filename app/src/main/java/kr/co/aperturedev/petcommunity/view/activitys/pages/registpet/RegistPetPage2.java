package kr.co.aperturedev.petcommunity.view.activitys.pages.registpet;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import kr.co.aperturedev.petcommunity.R;
import kr.co.aperturedev.petcommunity.modules.constant.QueryIDs;
import kr.co.aperturedev.petcommunity.modules.lib.PermissionHelper;
import kr.co.aperturedev.petcommunity.modules.uploader.ImageUploadListener;
import kr.co.aperturedev.petcommunity.modules.uploader.ImageUploadTask;
import kr.co.aperturedev.petcommunity.modules.uploader.UploadHostConst;
import kr.co.aperturedev.petcommunity.view.activitys.pages.PageActivity;
import kr.co.aperturedev.petcommunity.view.activitys.pages.PageSuper;

/**
 * Created by 5252b on 2018-03-06.
 */

public class RegistPetPage2 extends PageSuper {
    View v = null;

    ImageButton upload = null;      // 업로드 이미지 버튼
    ProgressBar uploadBar = null;   // 업로드 이미지 바
    TextView uploadMsg = null;      // 업로드 준비 바
    ImageView uploadView = null;    // 업로드 후 이미지 뷰
    Button nextButton = null;       // 다음 버튼

    String uploadedUrl = null;      // 파일 업로드 후 URL

    public RegistPetPage2(final Context context, PageActivity control) {
        super(context, control);
        setView(R.layout.page_registpet_2);

        this.v = getView();
        this.upload = this.v.findViewById(R.id.upload_image_button);
        this.uploadBar = this.v.findViewById(R.id.upload_progress);
        this.uploadMsg = this.v.findViewById(R.id.upload_prep_msg);
        this.uploadView = this.v.findViewById(R.id.upload_image_view);
        this.nextButton = this.v.findViewById(R.id.registpet2_next);

        OnButtonClickHandler handler = new OnButtonClickHandler();
        this.nextButton.setOnClickListener(handler);
        this.upload.setOnClickListener(handler);
    }

    /*
        파일 업로더 핸들러
     */
    class FileUploadHandler implements ImageUploadListener {
        @Override
        public void onUploaded(String link) {
            LoadImageTask task = new LoadImageTask();
            task.execute(link);

            uploadBar.setVisibility(INVISIBLE);
            uploadMsg.setVisibility(INVISIBLE);
            upload.setVisibility(INVISIBLE);
            nextButton.setVisibility(VISIBLE);
            uploadView.setVisibility(VISIBLE);

            // 업로드 된 이미지의 위치를 저장한다.
            uploadedUrl = link;
        }

        @Override
        public void onUploadFailed(String reason) {
            Toast.makeText(getContext(), "업로드에 실패했습니다.", Toast.LENGTH_SHORT).show();

            uploadBar.setVisibility(INVISIBLE);
            uploadMsg.setVisibility(VISIBLE);
            upload.setVisibility(VISIBLE);
        }

        @Override
        public void onPacketSended(int percent) {
            uploadBar.setProgress(percent);
        }
    }

    /*
        이미지 로드 테스크
     */
    class LoadImageTask extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected void onPostExecute(Bitmap image) {
            super.onPostExecute(image);

            if(image == null) {
                Toast.makeText(getContext(), "Image load failed.", Toast.LENGTH_SHORT).show();
                return;
            }

            uploadView.setImageBitmap(image);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            String targetUrl = strings[0];
            Bitmap bitmap = null;

            try {
                URL url = new URL(UploadHostConst.CDN_URL + targetUrl);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true);  // 서버 응답 수신
                conn.connect();

                InputStream is = conn.getInputStream();
                bitmap = BitmapFactory.decodeStream(is);
            } catch(IOException ex) {
                ex.printStackTrace();
            } catch(Exception ex) {
                ex.printStackTrace();
            }

            return bitmap;
        }
    }

    class OnButtonClickHandler implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.upload_image_button:
                    // 권한 확인
                    if(!PermissionHelper.checkPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        // 권한이 없다면 요청한다.
                        PermissionHelper.requestPermission(getControl(), Manifest.permission.READ_EXTERNAL_STORAGE);
                        return;
                    }

                    // 앨범에서 이미지를 선택합니다.
                    Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    Intent intent = new Intent(Intent.ACTION_PICK, uri);
                    getControl().startActivityForResult(intent, QueryIDs.SELECT_PICTURE_BROWSER);
                    break;
                case R.id.registpet2_next:
                    getControl().switchPage(new RegistPetPage3(getContext(), getControl()));
                    getControl().args.add(uploadedUrl);
                    break;  // 다음 페이지로 넘어갑니다.
            }
        }
    }

    /*
        엘범에서 이미지를 선택함.
     */
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

            this.upload.setVisibility(INVISIBLE);
            this.uploadBar.setVisibility(VISIBLE);
            this.uploadMsg.setVisibility(INVISIBLE);
        } catch(IOException iex) {
            Toast.makeText(getContext(), "IOException! " + iex.getMessage(), Toast.LENGTH_SHORT).show();
        } catch(Exception ex) {
            ex.printStackTrace();
            Toast.makeText(getContext(), "Failed. " + ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
