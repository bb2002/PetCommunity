package kr.co.aperturedev.petcommunity.modules.http;

/**
 * Created by 5252b on 2018-03-02.
 */

public interface RequestURLs {
    String DEFAULT_URL = "http://aperturedev.co.kr:21002/petcommunity/";

    /*
        매칭 서비스 관련
     */
    String GET_PET_TYPES = DEFAULT_URL + "services/get-pettypes.php";   // 강아지 종을 가져온다.
    String REGIST_MATCH_PET = DEFAULT_URL + "matching/regist-new-pet.php";  // 팻 등록 실시
    String MATCHING_OTHER_PET = DEFAULT_URL + "matching/matching.php";                // 매칭할 팻을 하나 가져옵니다.

    /*
        사용자 정보 관련
     */
    String GET_USER_INFO = DEFAULT_URL + "users/get-info.php";      // UUID -> 사용자 정보
    String REGIST_SERVICE = DEFAULT_URL + "users/signup.php";           // 회원가입을 실시한다.
    String IS_REGIST_SERVICE = DEFAULT_URL + "/users/is-registed.php";  // 회원가입 되어 있는지 확인한다.
    String LOGIN_SERVICE = DEFAULT_URL + "users/signin.php";            // 로그인 처리한다.




}
