package kr.co.aperturedev.petcommunity.modules.uploader;

/**
 * Created by 5252b on 2018-03-06.
 */

public interface UploadHostConst {
    String SERVER_IP = "211.239.124.246";        // Server IP
    int DATA_PORT = 21014;	// 파일 전송 포트
    int INFO_PORT = 21015;	// 정보 포트

    int TIME_OUT = 5000;    // 타임아웃
    int PACKET_SIZE = 1024; // 패킷 크기
}
