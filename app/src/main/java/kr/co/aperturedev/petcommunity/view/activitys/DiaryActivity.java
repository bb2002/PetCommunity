package kr.co.aperturedev.petcommunity.view.activitys;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

import kr.co.aperturedev.petcommunity.R;

public class DiaryActivity extends AppCompatActivity {
    WebView webview;
    TextView diaryBoard1View;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);
        diaryBoard1View=(TextView)findViewById(R.id.diaryBoard1View);
    }

    private class DiaryBoard1View extends AsyncTask<Void,Void,Void>{
        String string1="";
        private Document documents;
        private Elements elements;

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            diaryBoard1View.setText(elements.text());
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try{
                documents = (Document) Jsoup.connect("http://www.goldntree.co.kr/board/product/list.html?board_no=6").get();
                elements = documents.select("div#contents");
            } catch(IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}


