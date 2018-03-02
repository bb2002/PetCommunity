package kr.co.aperturedev.petcommunity.modules.http;

import org.json.JSONObject;

/**
 * Created by 5252b on 2018-03-02.
 * 결과값리스너
 */

public interface RequestHttpListener {
    void onResponse(ResponseObject result);
}
