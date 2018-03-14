package kr.co.aperturedev.petcommunity.modules.http.bcr;

import java.util.ArrayList;

/**
 * Created by 5252b on 2018-03-13.
 */

public class BCRRequest {
    private ArrayList<String> keys = new ArrayList<>();
    private ArrayList<Object> values = new ArrayList<>();

    /*
    새로운 인자값을 넣습니다.
     */
    public void addArgs(String key, Object value) {
        this.keys.add(key);
        this.values.add(value);
    }

    public String getScript() {
        if(keys.size() == 0) {
            return "";
        }

        if(keys.size() == 1) {
            return keys.get(0) + "=" + values.get(0);
        }

        String args = "";
        for(int i = 0; i < keys.size(); i ++) {
            if(i > 0) {
                args += "&";
            }

            args += keys.get(i);
            args += "=";
            args += values.get(i);
        }

        return args;
    }
}
