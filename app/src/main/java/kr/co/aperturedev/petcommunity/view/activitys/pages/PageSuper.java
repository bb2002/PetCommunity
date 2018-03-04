package kr.co.aperturedev.petcommunity.view.activitys.pages;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by 5252b on 2018-03-02.
 */

public class PageSuper extends RelativeLayout {
    PageActivity control = null;    // 컨트롤러
    View view = null;               // 현재 보이는 뷰

    public PageSuper(Context context, PageActivity control) {
        super(context);
        this.control = control;
    }

    /*
        03 02 2018
        이 Layout 에 View 를 설정한다.
     */
    public void setView(int layout) {
        LayoutInflater inflater = (LayoutInflater) control.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(layout, this, false);
        this.view = v;

        addView(v);
    }

    public PageActivity getControl() {
        return control;
    }

    public View getView() {
        return view;
    }

    /*
            03 02 2018
            생명주기 메서드
            재정의가 필수가 아닙니다.
         */
    public void onShow(){};     // 이 화면이 보여질 때
    public void onStop(){};     // 액티비티가 멈출 때
    public void onResume(){};   // 액티비티가 띄워질때
}
