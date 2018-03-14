package kr.co.aperturedev.petcommunity.modules.http.bcr;

/**
 * Created by 5252b on 2018-03-13.
 */

public interface ResponseCode {
    int OK = 200;   // 성공
    int INTERNAL_SERVER_ERROR = 500;    // 내부 서버 오류
    int CLIENT_REQUEST_ERROR = 404;     // 클라이언트 요청 오류
    int CLIENT_NOT_AUTH = 403;          // 클라이언트 인증 안됨

    int CLIENT_NO_ETHERNET = 100;       // 네트워크 연결 없슴
}
