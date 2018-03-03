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

import kr.co.aperturedev.petcommunity.R;
import kr.co.aperturedev.petcommunity.view.activitys.pages.PageActivity;
import kr.co.aperturedev.petcommunity.view.activitys.pages.PageSuper;

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
        EditTextTracker textTracker = new EditTextTracker();

        this.selectType.setOnClickListener(btnClickHandler);
        this.nextButton.setOnClickListener(btnClickHandler);
        this.genContainer.setOnCheckedChangeListener(radioClickHandler);

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
                    Log.d("PC", "남자 선택");
                    break;
                case R.id.registpet1_gen_woman:
                    Log.d("PC", "여자 선택");
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

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }
}
