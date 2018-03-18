package kr.co.aperturedev.petcommunity.view.fragments.pages.registpet;

import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import kr.co.aperturedev.petcommunity.modules.http.bcr.BCRResponse;
import kr.co.aperturedev.petcommunity.modules.http.bcr.ResponseCode;
import kr.co.aperturedev.petcommunity.modules.obj.PetTypeObject;
import kr.co.aperturedev.petcommunity.view.fragments.PageActivity;
import kr.co.aperturedev.petcommunity.view.fragments.PageSuper;
import kr.co.aperturedev.petcommunity.view.dialogs.progress.ProgressManager;
import kr.co.aperturedev.petcommunity.view.dialogs.window.custom.SelectPetTypeDialog;
import kr.co.aperturedev.petcommunity.view.dialogs.window.main.DialogManager;
import kr.co.aperturedev.petcommunity.view.dialogs.window.main.clicklistener.OnYesClickListener;

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
    boolean isMan = true;   // 기본값은 남자 이다.

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

                        // 입력값 저장
                        getControl().args.add(petType.getTypeUUID());
                        getControl().args.add(dogName.getText().toString());
                        getControl().args.add(Integer.parseInt(dogAge.getText().toString()));
                        getControl().args.add(isMan);
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
                    isMan = true;
                    break;
                case R.id.registpet1_gen_woman:
                    isMan = false;
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

        RequestHttpTask task = new RequestHttpTask(RequestURLs.GET_PET_TYPES, null, getContext(), new RequestPetTypeListener());
        task.execute();
    }

    private boolean isNotEmpty() {
        if(petType == null) return false;
        if(dogName.length() == 0) return false;
        if(dogAge.length() == 0) return false;

        return true;
    }

    class RequestPetTypeListener implements RequestHttpListener {
        DialogManager dm = null;
        public RequestPetTypeListener() {
            this.dm = new DialogManager(getContext());
            this.dm.setTitle("Fatal error");
            this.dm.setOnYesButtonClickListener(new OnYesClickListener() {
                @Override
                public void onClick(DialogInterface dialog) {
                    dialog.dismiss();
                }
            }, "OK");
        }

        @Override
        public void onResponse(int responseCode, BCRResponse response) {
            pm.disable();   // Progess bar close

            if(responseCode == ResponseCode.OK) {
                // 처리 성공
                JSONObject data = response.getData();

                try {
                    JSONArray petNames = data.getJSONArray("pet-names");
                    JSONArray petUUIDs = data.getJSONArray("pet-uuids");

                    for(int i = 0; i < petNames.length(); i ++) {
                        items.add(new PetTypeObject(petNames.getString(i), petUUIDs.getInt(i)));
                    }
                } catch(JSONException jex) {
                    this.dm.setDescription(getContext().getString(R.string.default_exec_error) + "\n" + jex.getMessage());
                    this.dm.show();
                }


            } else {
                // 처리 오류
                this.dm.setDescription(getContext().getString(R.string.default_http_error) + "\n" + response.getResponseMsg());
                this.dm.show();
            }
        }
    }
}
