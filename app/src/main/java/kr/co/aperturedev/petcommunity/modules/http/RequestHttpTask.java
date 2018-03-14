package kr.co.aperturedev.petcommunity.modules.http;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import kr.co.aperturedev.petcommunity.modules.http.bcr.BCRRequest;
import kr.co.aperturedev.petcommunity.modules.http.bcr.BCRResponse;
import kr.co.aperturedev.petcommunity.modules.http.bcr.ResponseCode;

/**
 * Created by 5252b on 2018-03-02.
 * Http 요청 처리 라이브러리
 */

public class RequestHttpTask extends AsyncTask<String, Void, BCRResponse> {
    String targetUrl = null;            // 타겟 URL
    Context context = null;             // 컨텍스트
    RequestHttpListener listener = null;    // 리스너
    BCRRequest request = null;

    public RequestHttpTask(String targetUrl, BCRRequest request, Context context, RequestHttpListener listener) {
        this.targetUrl = targetUrl;
        this.context = context;
        this.listener = listener;
        this.request = (request == null ? new BCRRequest() : request);
    }

    @Override
    protected void onPostExecute(BCRResponse respObj) {
        super.onPostExecute(respObj);

        // 처리 완료, 결과값을 보낸다.
        if(this.listener != null) {
            this.listener.onResponse(respObj.getResponseCode(), respObj);
        }
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected BCRResponse doInBackground(String... strings) {
        BCRResponse respObj = null;

        // 네트워크 연결을 확인한다.
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(cm.getActiveNetworkInfo() == null) {
            // 네트워크가 없을 경우
            respObj = new BCRResponse(ResponseCode.CLIENT_NO_ETHERNET, "This device is not connect internet.");
            return respObj;
        }

        try {
            // 요청을 보냅니다.
            URL url = new URL(targetUrl);
            HttpURLConnection http = (HttpURLConnection) url.openConnection();

            http.setConnectTimeout(5000);
            http.setDefaultUseCaches(false);
            http.setDoInput(true);                         // 서버에서 읽기 모드 지정
            http.setDoOutput(true);                       // 서버로 쓰기 모드 지정
            http.setRequestMethod("POST");         // 전송 방식은 POST

            http.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
            StringBuffer buffer = new StringBuffer();
            buffer.append(request.getScript());

            OutputStreamWriter outStream = new OutputStreamWriter(http.getOutputStream(), "UTF-8");
            PrintWriter writer = new PrintWriter(outStream);
            writer.write(buffer.toString());
            writer.flush();

            InputStreamReader tmp = new InputStreamReader(http.getInputStream(), "UTF-8");
            BufferedReader reader = new BufferedReader(tmp);
            StringBuilder builder = new StringBuilder();
            String str;
            while ((str = reader.readLine()) != null) {
                builder.append(str);
            }

            /*
                03 02 디버깅 전용 코드
             */
             Log.d("PC", builder.toString());

            respObj = new BCRResponse(builder.toString());
            return respObj;
        } catch(Exception ex) {
            // 예외 발생시
            respObj = new BCRResponse(ResponseCode.CLIENT_REQUEST_ERROR, "Request error.\n" + ex.getMessage());
            return respObj;
        }
    }
}
