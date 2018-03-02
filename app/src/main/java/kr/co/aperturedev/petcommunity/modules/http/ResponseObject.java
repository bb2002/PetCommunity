package kr.co.aperturedev.petcommunity.modules.http;

import org.json.JSONObject;

/**
 * Created by 5252b on 2018-03-02.
 * Http 요청 결과를 저장하는 개체
 */

public class ResponseObject {
    // 요청에 대한 결과값
    enum ResponseCodes {
        SUCCESS,        // 성공
        NO_INTERNET,    // 인터넷 없슴
        EXCEPTION       // 예외 처리됨.
    }

    JSONObject respObject = null;   // 결과값
    ResponseCodes respCode = null;  // 처리 결과

    public JSONObject getRespObject() {
        return respObject;
    }

    public void setRespObject(JSONObject respObject) {
        this.respObject = respObject;
    }

    public ResponseCodes getRespCode() {
        return respCode;
    }

    public void setRespCode(ResponseCodes respCode) {
        this.respCode = respCode;
    }
}
