package kr.co.aperturedev.petcommunity.modules.http.bcr;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * header = null / 500, Internal server error.
 * data = null / ???, No arguments.
 */

public class BCRResponse {
    private JSONObject header = null;
    private JSONObject data = null;

    public BCRResponse(String respScript) {
        try {
            JSONObject obj = new JSONObject(respScript);
            this.header = obj.getJSONObject("header");
            this.data = obj.getJSONObject("data");
        } catch(JSONException jex) {
            jex.printStackTrace();
        }
    }

    public BCRResponse(int code, String msg) {
        try {
            this.header = new JSONObject();
            this.header.put("response-code", code);
            this.header.put("response-msg", msg);
        } catch(JSONException jex) {
            jex.printStackTrace();

            this.header = null;
            this.data = null;
        }
    }

    /*
        서버 응답 결과를 가져온다.
     */
    public int getResponseCode() {
        if(header == null) return ResponseCode.INTERNAL_SERVER_ERROR;

        try {
            return header.getInt("response-code");
        } catch(JSONException jex) {
            return ResponseCode.CLIENT_REQUEST_ERROR;
        }
    }

    /*
        서버 메세지를 가져온다.
     */
    public String getResponseMsg() {
        if(header == null) return null;

        try {
            return header.getString("response-msg");
        } catch(JSONException jex) {
            return null;
        }
    }

    public JSONObject getData() {
        return this.data;
    }
}
