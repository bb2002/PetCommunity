package kr.co.aperturedev.petcommunity.view.fragments.pages.matching;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import kr.co.aperturedev.petcommunity.R;
import kr.co.aperturedev.petcommunity.view.fragments.PageActivity;
import kr.co.aperturedev.petcommunity.view.fragments.PageSuper;

/**
 * Created by 5252b on 2018-03-18.
 */

public class MatchedFragment extends PageSuper {
    JSONObject petObj = null;

    public MatchedFragment(Context context, PageActivity control, String petObject) throws JSONException {
        super(context, control);
        setView(R.layout.page_matched_show);

        this.petObj = new JSONObject(petObject);

        Log.d("PC", petObject);
    }
}
