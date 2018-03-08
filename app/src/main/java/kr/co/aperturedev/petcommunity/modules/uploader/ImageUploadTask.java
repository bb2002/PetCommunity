package kr.co.aperturedev.petcommunity.modules.uploader;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;

/**
 * Bitmap -> Upload target
 * Integer -> Persent
 * String -> Upload link
 */

public class ImageUploadTask extends AsyncTask<Void, Integer, Object[]> {
    Bitmap uploadImage = null;
    ImageUploadListener listener = null;

    int fileSize = 0;   // 업로드할 파일의 크기

    public ImageUploadTask(Bitmap image, ImageUploadListener listener) {
        this.uploadImage = image;
        this.listener = listener;
    }

    @Override
    protected void onPostExecute(Object[] result) {
        super.onPostExecute(result);

        boolean isUploaded = (boolean) result[0];   // 업로드 결과
        String reasonOrLink = (String) result[1];   // isUploaed ? 'FILE LINK' : 'REASON';

        if(listener != null) {
            if(isUploaded) {
                listener.onUploaded(reasonOrLink);
            } else {
                listener.onUploadFailed(reasonOrLink);
            }
        }
    }

    /*
        values 으로 업로드된 파일의 크기를 가져옵니다.
     */
    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);

        if(listener != null) {
            listener.onPacketSended((int) (((double) values[0] / (double) fileSize) * 100));
        }
    }

    @Override
    protected Object[] doInBackground(Void... voids) {
        /*
        Bitmap image 를 byte[] 으로 바꾼다.
         */
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        uploadImage.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        fileSize = byteArray.length;

        // 서버에 연결한다.
        Socket dataSocket = new Socket();   // 파일 전송 소켓
        Socket infoSocket = new Socket();   // 정보 교환 소켓

        // Socket pipe 를 만든다.
        DataOutputStream infoDos = null;	// Client -> Server [info]
        DataInputStream infoDis = null;		// Server -> Client [info]
        DataInputStream dataDis = null;		// Server -> Client [data]
        DataOutputStream dataDos = null;	// Client -> Server [data]

        try {
            SocketAddress dataSocketAddr = new InetSocketAddress(UploadHostConst.SERVER_IP, UploadHostConst.DATA_PORT);
            SocketAddress infoSocketAddr = new InetSocketAddress(UploadHostConst.SERVER_IP, UploadHostConst.INFO_PORT);

            // 타임 아웃 설정
            dataSocket.setSoTimeout(UploadHostConst.TIME_OUT);
            infoSocket.setSoTimeout(UploadHostConst.TIME_OUT);

            // 서버와 연결한다.
            dataSocket.connect(dataSocketAddr, UploadHostConst.TIME_OUT);
            infoSocket.connect(infoSocketAddr, UploadHostConst.TIME_OUT);

            infoDos = new DataOutputStream(infoSocket.getOutputStream());
            infoDis = new DataInputStream(infoSocket.getInputStream());
            dataDis = new DataInputStream(dataSocket.getInputStream());
            dataDos = new DataOutputStream(dataSocket.getOutputStream());

            /*
             * 단일 파일 업로드
             *
             * 1. 업로드 할 파일의 크기를 보낸다.
             * 2. 업로드 가능 여부를 받아온다. false -> reason
             * 3. 파일 데이터를 보낸다.
             * 4. URL 값을 받는다.
             * 5. 스레드 종료.
             */
            infoDos.writeInt(fileSize); // 파일 크기 통보
            boolean canUpload = infoDis.readBoolean();
            if(!canUpload) {
                // 업로드 불가, 사유값 읽기
                String reason = infoDis.readUTF();

                return new Object[]{ false, reason };
            }

            // 업로드 가능, 파일을 업로드 한다.
            ByteArrayInputStream imageStream = new ByteArrayInputStream(byteArray);
            byte[] buff = new byte[UploadHostConst.PACKET_SIZE];
            int readCount = 0;
            int loaded = 0;

            while((readCount = imageStream.read(buff)) != -1) {
                dataDos.write(buff, 0, readCount);
                boolean send = dataDis.readInt() != 0;

                if (!send) {
                    Log.d("PC", "Packet send failed.");
                }

                loaded += readCount;
                onProgressUpdate(loaded);
            }

            dataDos.flush();
            dataDis.close();
            dataDos.close();

            // 파일 업로드 완료.
            String uploadedName = infoDis.readUTF();

            // 성공했습니다.
            return new Object[]{ true, uploadedName };
        } catch(SocketException sex) {
            // 타임아웃등 연결 실패
            sex.printStackTrace();
            return new Object[]{ false, sex.getMessage() };
        } catch(Exception ex) {
            // 기타 오류
            ex.printStackTrace();
            return new Object[]{ false, ex.getMessage() };
        }
    }
}
