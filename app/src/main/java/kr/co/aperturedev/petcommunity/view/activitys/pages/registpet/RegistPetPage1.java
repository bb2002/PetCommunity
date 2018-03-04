package kr.co.aperturedev.petcommunity.view.activitys.pages.registpet;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;

import kr.co.aperturedev.petcommunity.R;
import kr.co.aperturedev.petcommunity.modules.http.RequestHttpListener;
import kr.co.aperturedev.petcommunity.modules.http.RequestHttpTask;
import kr.co.aperturedev.petcommunity.modules.http.RequestURLs;
import kr.co.aperturedev.petcommunity.modules.http.ResponseObject;
import kr.co.aperturedev.petcommunity.view.activitys.pages.PageActivity;
import kr.co.aperturedev.petcommunity.view.activitys.pages.PageSuper;
import kr.co.aperturedev.petcommunity.view.dialogs.progress.ProgressManager;

/**
 * Created by 5252b on 2018-03-02.
 */

public class RegistPetPage1 extends PageSuper {
    View v = null;

    EditText selectType = null;
    EditText dogName = null;
    EditText dogAge = null;
    RadioGroup genContainer = null;
    Button nextButton = null;

    public RegistPetPage1(Context context, PageActivity control) {
        super(context, control);
        setView(R.layout.page_registpet_1);

        // 현제 뷰를 가져온다.
        this.v = getView();

        this.selectType = this.v.findViewById(R.id.registpet1_select_type);
        this.dogName = this.v.findViewById(R.id.registpet1_name);
        this.dogAge = this.v.findViewById(R.id.registpet1_age);
        this.genContainer = this.v.findViewById(R.id.registpet1_gen_container);
        this.nextButton = this.v.findViewById(R.id.registpet1_next);

        // 핸들링
        OnButtonClickListener btnClickHandler = new OnButtonClickListener();
        OnRadioSelectChangeListener radioClickHandler = new OnRadioSelectChangeListener();
        EditTextTracker tracker = new EditTextTracker();

        this.selectType.setOnClickListener(btnClickHandler);
        this.nextButton.setOnClickListener(btnClickHandler);
        this.genContainer.setOnCheckedChangeListener(radioClickHandler);
        this.dogName.addTextChangedListener(tracker);
        this.dogAge.addTextChangedListener(tracker);
    }

    class OnButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.registpet1_select_type:
                    break;
                case R.id.registpet1_next:
                    break;
            }
        }
    }

    class OnRadioSelectChangeListener implements RadioGroup.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch(checkedId) {
                case R.id.registpet1_gen_man:
                    break;
                case R.id.registpet1_gen_woman:
                    break;
            }
        }
    }

    class EditTextTracker implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            // 입력값이 전혀 없을 경우
            if(s.length() == 0) {
                setEnableNextButton(false);
                return;
            }
        }

        @Override
        public void afterTextChanged(Editable s) {}
    }

    private void setEnableNextButton(boolean b) {
        this.nextButton.setEnabled(b);
    }


    /*
        03 04 2018
        Http 요청 처리 관련
     */
    ProgressManager pm = null;
    @Override
    public void onShow() {
        // 사용자에게 화면이 보여집니다.

        // 프로그레스 바를 띄워 서버에서 다운로드 하게 합니다.
        this.pm = new ProgressManager(getContext());
        this.pm.setMessage(getControl().getString(R.string.default_process));
        this.pm.enable();

        RequestHttpTask task = new RequestHttpTask(RequestURLs.GET_PET_TYPES, new ArrayList<Object>(), getContext(), new RequestPetTypeListener());
        task.execute();
    }

    class RequestPetTypeListener implements RequestHttpListener {
        @Override
        public void onResponse(ResponseObject result) {
            pm.disable();

            Log.d("PC", result.getRespObject().toString());
        }
    }
}
