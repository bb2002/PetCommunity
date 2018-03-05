package kr.co.aperturedev.petcommunity.view.dialogs.window.custom;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import kr.co.aperturedev.petcommunity.R;
import kr.co.aperturedev.petcommunity.modules.obj.PetTypeObject;


/**
 * Created by 5252b on 2018-02-20.
 */

public class SelectPetTypeDialog extends Dialog {
    ArrayList<PetTypeObject> items = null;          // 보여지는 아이템
    ArrayList<PetTypeObject> backupItems = null;    // 원래 데이터

    EditText searchBar = null;          // 검색 바
    ListView petItemsList = null;       // 팻 리스트
    ArrayAdapter adapter = null;        // Adapter
    ICustomDialogEventListener listener = null;

    public SelectPetTypeDialog(@NonNull Context context, ArrayList<PetTypeObject> items, ICustomDialogEventListener listener) {
        super(context);

        this.items = items;
        this.backupItems = new ArrayList<>();   // 데이터를 복사 한다.
        this.backupItems.addAll(this.items);
        this.listener = listener;
    }

    public interface ICustomDialogEventListener {
        void onClose(PetTypeObject type);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_select_pettype);

        // 개체 찾기
        this.searchBar = findViewById(R.id.selecttype_search);
        this.petItemsList = findViewById(R.id.selecttype_list);

        // 이벤트 처리
        this.searchBar.addTextChangedListener(new SearchTextWatcher());
        this.petItemsList.setOnItemClickListener(new OnGameSelectHandler());

        // 아답터 삽입
        this.adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1);
        this.petItemsList.setAdapter(this.adapter);

        rl();
    }

    private void rl() {
        this.adapter.clear();
        for(PetTypeObject obj : this.items) {
            String name = obj.getTypeName();

            this.adapter.add(name);
        }

        this.adapter.notifyDataSetChanged();
    }

    class SearchTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(s.length() == 0) {
                items.addAll(backupItems);
                rl();    // 검색어가 없다면 초기 데이터로 돌린다.

                Log.d("PC", "초기값" + backupItems.size());
                return;
            }

            // 검색 한다.
            items.clear();
            for(PetTypeObject obj : backupItems) {
                String name = obj.getTypeName();

                if(name.contains(s)) {
                    // 검색 성립
                    items.add(obj);
                }
            }
            rl();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    class OnGameSelectHandler implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            PetTypeObject obj = items.get(position);
            if(listener != null) {
                listener.onClose(obj);
                dismiss();
            }
        }
    }
}
