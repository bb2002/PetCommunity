package kr.co.aperturedev.petcommunity.view.fragments.pages.matching;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import kr.co.aperturedev.petcommunity.R;
import kr.co.aperturedev.petcommunity.modules.http.RequestHttpListener;
import kr.co.aperturedev.petcommunity.modules.http.RequestHttpTask;
import kr.co.aperturedev.petcommunity.modules.http.RequestURLs;
import kr.co.aperturedev.petcommunity.modules.http.bcr.BCRRequest;
import kr.co.aperturedev.petcommunity.modules.http.bcr.BCRResponse;
import kr.co.aperturedev.petcommunity.modules.http.bcr.ResponseCode;
import kr.co.aperturedev.petcommunity.modules.obj.PetTypeObject;
import kr.co.aperturedev.petcommunity.view.dialogs.progress.ProgressManager;
import kr.co.aperturedev.petcommunity.view.dialogs.window.main.DialogManager;
import kr.co.aperturedev.petcommunity.view.dialogs.window.main.clicklistener.OnYesClickListener;
import kr.co.aperturedev.petcommunity.view.fragments.PageActivity;
import kr.co.aperturedev.petcommunity.view.fragments.PageSuper;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by 5252b on 2018-03-18.
 */

public class SettingFilterFragment extends PageSuper {
    View v = null;

    DialogManager dm = null;    // 오류 발생 시
    ProgressManager pm = null;  // 프로그래스 바

    Spinner petTypeSelector = null; // 강아지 타입
    Button startMatching = null;    // 매칭 시작 버튼
    ArrayList<PetTypeObject> petTypes = new ArrayList<>();   // 강아지 종류에 대한 정의

    public SettingFilterFragment(Context context, PageActivity control) {
        super(context, control);
        setView(R.layout.page_matching_filter);

        this.v = getView();

        this.dm = new DialogManager(getControl());
        this.dm.setTitle("Matching failed.");
        this.dm.setOnYesButtonClickListener(new OnYesClickListener() {
            @Override
            public void onClick(DialogInterface dialog) {
                dialog.dismiss();
            }
        }, "CLOSE");
        this.pm = new ProgressManager(getControl());

        this.petTypeSelector = findViewById(R.id.filter_pet_selector);
        this.startMatching = findViewById(R.id.filter_start_matching);
        this.petTypeSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView)parent.getChildAt(0)).setTextColor(Color.WHITE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        this.startMatching.setOnClickListener(new OnButtonClickHandler(getControl()));

        // 서버에서 강아지 종류를 불러온다.
        RequestHttpTask task = new RequestHttpTask(RequestURLs.GET_PET_TYPES, null, getContext(), new RequestHttpListener() {
            @Override
            public void onResponse(int responseCode, BCRResponse response) {
                // 강아지 종류를 불러왔다.
                pm.disable();

                if(responseCode == ResponseCode.OK) {
                    // 처리 성공
                    JSONObject data = response.getData();
                    ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1);

                    try {
                        JSONArray petNames = data.getJSONArray("pet-names");
                        JSONArray petUUIDs = data.getJSONArray("pet-uuids");

                        for(int i = 0; i < petNames.length(); i ++) {
                            String petName = petNames.getString(i);
                            int petId = petUUIDs.getInt(i);

                            // 등록 객체 포함
                            petTypes.add(new PetTypeObject(petName, petId));
                            // 아답터 포함
                            adapter.add(petName);
                        }
                    } catch(JSONException jex) {
                        dm.setDescription(getContext().getString(R.string.default_exec_error) + "\n" + jex.getMessage());
                        dm.show();
                    }

                    // adapter -> list
                    petTypeSelector.setAdapter(adapter);
                } else {
                    // 처리 오류
                    dm.setDescription(getContext().getString(R.string.default_http_error) + "\n" + response.getResponseMsg());
                    dm.show();
                }
            }
        });
        task.execute();
        this.pm.setMessage(getContext().getString(R.string.default_process));
        this.pm.enable();
    }


    /*
        강아지 매칭이 완료됬다
     */
    class OnMatchingHandler implements RequestHttpListener {
        @Override
        public void onResponse(int responseCode, BCRResponse response) {
            // 요청 완료
            if(responseCode == 200) {
                try {
                    String petData = response.getData().getString("pet");
                    getControl().switchPage(new MatchedFragment(getContext(), getControl(), petData));
                } catch(JSONException jex) {
                    Toast.makeText(getContext(), "JSON Exception. " + jex.getMessage(), Toast.LENGTH_SHORT).show();
                    jex.printStackTrace();
                } catch(NullPointerException ex) {
                    ex.printStackTrace();

                    // null == 강아지 없슴
                    dm.setDescription("매칭할 강아지가 없습니다.\n나중에 다시 시도해보세요.");
                    dm.show();
                }
            } else {
                dm.setDescription("Internal server error.\n" + response.getResponseMsg());
                dm.show();
            }
        }
    }

    class OnButtonClickHandler implements View.OnClickListener {
        ProgressManager pm = null;

        public OnButtonClickHandler(Context context) {
            this.pm = new ProgressManager(context);
            this.pm.setMessage(getContext().getString(R.string.default_process));
        }

        @Override
        public void onClick(View v) {
            SharedPreferences prep = getContext().getSharedPreferences("PetCommunity", MODE_PRIVATE);
            String pin = prep.getString("user-pin", null);
            if(pin == null) {
                Toast.makeText(getContext(), "Fatal error, Auth failed.", Toast.LENGTH_SHORT).show();
                return;
            }

            // 매칭 테스크 구동
            BCRRequest request = new BCRRequest();
            request.addArgs("pet-type", petTypes.get(petTypeSelector.getSelectedItemPosition()).getTypeUUID());
            request.addArgs("user-pin", pin);

            RequestHttpTask task = new RequestHttpTask(RequestURLs.MATCHING_OTHER_PET, request, getContext(), new OnMatchingHandler());
            task.execute();
        }
    }
}
