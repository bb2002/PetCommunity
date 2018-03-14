package kr.co.aperturedev.petcommunity.view.activitys.pages.registpet;

import android.content.Context;
import android.content.DialogInterface;
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
import kr.co.aperturedev.petcommunity.modules.http.bcr.BCRRequest;
import kr.co.aperturedev.petcommunity.modules.http.bcr.BCRResponse;
import kr.co.aperturedev.petcommunity.modules.http.bcr.ResponseCode;
import kr.co.aperturedev.petcommunity.view.activitys.pages.PageActivity;
import kr.co.aperturedev.petcommunity.view.activitys.pages.PageSuper;
import kr.co.aperturedev.petcommunity.view.dialogs.window.main.DialogManager;
import kr.co.aperturedev.petcommunity.view.dialogs.window.main.clicklistener.OnYesClickListener;

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

            /*
                03 13 2018
                강아지 정보를 저장 요청 합니다.
             */
            BCRRequest request = new BCRRequest();
            ArrayList<Object> args = getControl().args;

            request.addArgs("admin-uuid", "testuuidtestuuid");
            request.addArgs("admin-tel", telNumber.getText().toString());
            request.addArgs("pet-type", args.get(0));
            request.addArgs("pet-name", args.get(1));
            request.addArgs("pet-age", args.get(2));
            request.addArgs("pet-gender", args.get(3));
            request.addArgs("pet-image", args.get(4));

            RequestHttpTask task = new RequestHttpTask(RequestURLs.REGIST_MATCH_PET, request, getContext(), new OnHttpRequestHandler());
            task.execute();
        }
    }

    class OnHttpRequestHandler implements RequestHttpListener {
        @Override
        public void onResponse(int responseCode, BCRResponse response) {
            if(responseCode == ResponseCode.OK) {
                // 처리 성공
                Toast.makeText(getContext(), "등록되었습니다.", Toast.LENGTH_SHORT).show();
            } else {
                // 처리 실패
                DialogManager dm = new DialogManager(getContext());
                dm.setTitle("Fatal error");
                dm.setDescription(getContext().getString(R.string.default_http_error) + "\n" + response.getResponseMsg());
                dm.setOnYesButtonClickListener(new OnYesClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog) {
                        dialog.dismiss();
                    }
                }, "OK");
                dm.show();
            }
        }
    }
}
