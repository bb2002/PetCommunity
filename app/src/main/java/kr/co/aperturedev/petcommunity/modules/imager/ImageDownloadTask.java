package kr.co.aperturedev.petcommunity.modules.imager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by 5252b on 2018-03-16.
 * 업로드 한 이미지를 다운로드 해 옵니다.
 */

public class ImageDownloadTask extends AsyncTask<Void, Void, Bitmap> {
    String imageSrc = null;
    ImageDownloadListener listener = null;
    Context context = null;

    public ImageDownloadTask(String imageSrc, ImageDownloadListener listener, Context context) {
        this.imageSrc = imageSrc;
        this.listener = listener;
        this.context = context;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);

        if(listener != null) {
            listener.onLoaded(bitmap);
        }
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

    @Override
    protected Bitmap doInBackground(Void... voids) {
        Bitmap bitmap = null;

        try {
            URL url = new URL(UploadHostConst.CDN_URL + imageSrc);

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
