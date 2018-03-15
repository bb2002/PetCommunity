package kr.co.aperturedev.petcommunity.modules.uploader;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import java.io.ByteArrayOutputStream;
import java.net.SocketException;

import okhttp3.Call;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Bitmap -> Upload target
 * Integer -> Persent
 * String -> Upload link
 */

public class ImageUploadTask extends AsyncTask<Void, Integer, Object[]> {
    Bitmap uploadImage = null;
    ImageUploadListener listener = null;

    int fileSize = 0;   // 업로드할 파일의 크기

    public ImageUploadTask(Bitmap image, ImageUploadListener listener) {
        this.uploadImage = image;
        this.listener = listener;
    }

    @Override
    protected void onPostExecute(Object[] result) {
        super.onPostExecute(result);

        boolean isUploaded = (boolean) result[0];   // 업로드 결과
        String reasonOrLink = (String) result[1];   // isUploaed ? 'FILE LINK' : 'REASON';

        if (listener != null) {
            if (isUploaded) {
                listener.onUploaded(reasonOrLink);
            } else {
                listener.onUploadFailed(reasonOrLink);
            }
        }
    }

    /*
        values 으로 업로드된 파일의 크기를 가져옵니다.
     */
    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);

        if (listener != null) {
            listener.onPacketSended((int) (((double) values[0] / (double) fileSize) * 100));
        }
    }

    @Override
    protected Object[] doInBackground(Void... voids) {
        /*
        Bitmap image 를 byte[] 으로 바꾼다.
         */
        onProgressUpdate(0);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        uploadImage.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        onProgressUpdate(10);
        byte[] byteArray = stream.toByteArray();

        onProgressUpdate(30);
        try {
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("imagefile", "petimage.jpg", RequestBody.create(MultipartBody.FORM, byteArray))
                    .build();
            onProgressUpdate(50);

            Request request = new Request.Builder()
                    .url(UploadHostConst.UPLOADER_URL)
                    .post(requestBody)
                    .build();
            onProgressUpdate(60);

            OkHttpClient client = new OkHttpClient();
            Call call = client.newCall(request);
            Response response = call.execute();
            onProgressUpdate(80);

            //System.out.println("Response: " + s);
            String uploadedName = response.body().string();

            if (uploadedName.contains("FILE UPLOAD FAILED")) {
                throw new Exception("Try Later");
            }
            // 성공했습니다.
            onProgressUpdate(100);
            return new Object[]{true, uploadedName};
        } catch (SocketException sex) {
            // 타임아웃등 연결 실패
            sex.printStackTrace();
            return new Object[]{false, sex.getMessage()};
        } catch (Exception ex) {
            // 기타 오류
            ex.printStackTrace();
            return new Object[]{false, ex.getMessage()};
        }
    }
}
