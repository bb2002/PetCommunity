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
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
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
import kr.co.aperturedev.petcommunity.view.dialogs.progress.ProgressManager;
import kr.co.aperturedev.petcommunity.view.dialogs.window.custom.SelectPetTypeDialog;

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

    ArrayList<PetTypeObject> items = new ArrayList<>();


    // 선택된 데이터들
    PetTypeObject petType = null;

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
                    SelectPetTypeDialog dialog = new SelectPetTypeDialog(getControl(), items, new DialogCloseListener());
                    dialog.show();
                    break;
                case R.id.registpet1_next:
                    if(isNotEmpty()) {
                        getControl().switchPage(new RegistPetPage2(getContext(), getControl()));
                    } else {
                        Toast.makeText(getContext(), "모두 입력해 주세요.", Toast.LENGTH_SHORT).show();
                    }
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

    class DialogCloseListener implements SelectPetTypeDialog.ICustomDialogEventListener {
        @Override
        public void onClose(PetTypeObject type) {
            if(type != null) {
                petType = type; // 팻을 선택했습니다. 완료됨.
                selectType.setText(petType.getTypeName());
            }
        }
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

    private boolean isNotEmpty() {
        if(petType == null) return false;
        if(dogName.length() == 0) return false;
        if(dogAge.length() == 0) return false;

        return true;
    }

    class RequestPetTypeListener implements RequestHttpListener {
        @Override
        public void onResponse(ResponseObject result) {
            pm.disable();

            // 요청 오류
            if(result.getRespCode() != ResponseObject.ResponseCodes.SUCCESS) {
                // 오류
                Toast.makeText(getContext(), "Request failed.", Toast.LENGTH_SHORT).show();
                return;
            }

            JSONObject resObj = result.getRespObject();

            try {
                JSONArray petNames = resObj.getJSONArray("pet-names");
                JSONArray petUUIDs = resObj.getJSONArray("pet-uuids");

                for(int i = 0; i < petNames.length(); i ++) {
                    String name = petNames.getString(i);
                    int uuid = petUUIDs.getInt(i);

                    items.add(new PetTypeObject(name, uuid));
                }
            } catch(JSONException ex) {
                ex.printStackTrace();
                Toast.makeText(getContext(), "Internal server error", Toast.LENGTH_SHORT).show();
            } catch(Exception ex2) {
                ex2.printStackTrace();
                Toast.makeText(getContext(), "Fatal error", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
