package kr.co.aperturedev.petcommunity.modules.http;

/**
 * Created by 5252b on 2018-03-02.
 */

public interface RequestURLs {
    String DEFAULT_URL = "http://aperturedev.co.kr:21002/petcommunity/";

    /*
        사용자 정보 관련
     */
    String GET_USER_INFO = DEFAULT_URL + "users/get-userinfo";      // UUID -> 사용자 정보
    String GET_PET_TYPES = DEFAULT_URL + "services/get-pettypes.php";   // 강아지 종을 가져온다.
    String REGIST_MATCH_PET = DEFAULT_URL + "matching/regist-new-pet.php";  // 팻 등록 실시
}
