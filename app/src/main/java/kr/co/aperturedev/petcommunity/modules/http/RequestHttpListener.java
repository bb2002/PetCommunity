package kr.co.aperturedev.petcommunity.modules.http;

import org.json.JSONObject;

import kr.co.aperturedev.petcommunity.modules.http.bcr.BCRResponse;

/**
 * Created by 5252b on 2018-03-02.
 * 결과값리스너
 */

public interface RequestHttpListener {
    void onResponse(int responseCode, BCRResponse response);
}
