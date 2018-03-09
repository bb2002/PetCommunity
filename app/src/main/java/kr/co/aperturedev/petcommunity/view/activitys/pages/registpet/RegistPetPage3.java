package kr.co.aperturedev.petcommunity.view.activitys.pages.registpet;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;

import kr.co.aperturedev.petcommunity.R;
import kr.co.aperturedev.petcommunity.modules.http.RequestHttpListener;
import kr.co.aperturedev.petcommunity.modules.http.RequestHttpTask;
import kr.co.aperturedev.petcommunity.modules.http.RequestURLs;
import kr.co.aperturedev.petcommunity.modules.http.ResponseObject;
import kr.co.aperturedev.petcommunity.modules.obj.PetTypeObject;
import kr.co.aperturedev.petcommunity.view.activitys.pages.PageActivity;
import kr.co.aperturedev.petcommunity.view.activitys.pages.PageSuper;

/**
 * Created by 5252b on 2018-03-08.
 */

public class RegistPetPage3 extends PageSuper {
    Button nextButton = null;   // 다음 버튼
    TextView telNumber = null;  // 전화번호 입력 창

    public RegistPetPage3(Context context, PageActivity control) {
        super(context, control);
        setView(R.layout.page_registpet_3);

        View v = getView();

        this.nextButton = v.findViewById(R.id.registpet3_next);
        this.telNumber = v.findViewById(R.id.registpet3_tel_input);

        this.nextButton.setOnClickListener(new OnButtonClickHandler());
    }

    class OnButtonClickHandler implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if(telNumber.getText().length() == 0) {
                Toast.makeText(getContext(), "전화번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                return;
            }

            getControl().args.add(telNumber.getText().toString());

            RequestHttpTask task = new RequestHttpTask(RequestURLs.REGIST_MATCH_PET, getControl().args, getContext(), new OnHttpRequestHandler());
            task.execute();
        }
    }

    class OnHttpRequestHandler implements RequestHttpListener {
        @Override
        public void onResponse(ResponseObject result) {
            JSONObject obj = result.getRespObject();

            // 처리 결과
            try {
                if (obj.getInt("response-code") == 200) {
                    Toast.makeText(getContext(), "매칭이 등록되었습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Http query error", Toast.LENGTH_SHORT).show();
                    return;
                }
            } catch(Exception ex) {
                Toast.makeText(getContext(), "Exception. " + ex.getMessage(), Toast.LENGTH_SHORT).show();
                return;
            }

            getControl().finish();
        }
    }
}
