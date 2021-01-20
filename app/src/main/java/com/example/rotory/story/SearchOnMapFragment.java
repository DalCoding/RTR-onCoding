package com.example.rotory.story;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.rotory.Adapter.LocationAdapter;
import com.example.rotory.R;
import com.example.rotory.kakao.ApiClient;
import com.example.rotory.kakao.ApiInterface;
import com.example.rotory.kakao.CategoryResult;
import com.example.rotory.kakao.Document;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;

import java.net.URLEncoder;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SearchOnMapFragment extends Fragment {
    private static final String TAG = "SearchOnMapFragment";

    EditText roadStoryAddressEditText;
    RecyclerView recyclerView;
    Context context;
    LocationAdapter locationAdapter;

    ArrayList<Document> documentArrayList = new ArrayList<>();

    public SearchOnMapFragment() {}


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        if (context != null) {
            context = null;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_search_on_map, container, false);

        initUI (rootView);

        return rootView;
    }

    private void initUI(ViewGroup rootView) {
        roadStoryAddressEditText = rootView.findViewById(R.id.roadStoryAddressEditText);
        recyclerView = rootView.findViewById(R.id.writeRoadMapSearchRecyclerView);
        locationAdapter = new LocationAdapter(documentArrayList, getContext(),
                roadStoryAddressEditText, recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(locationAdapter);

        roadStoryAddressEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Log.d(TAG,"검색창에서 텍스트 변화 인지 시작");
                recyclerView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Log.d(TAG,"검색창에서 텍스트 변화 인지 시작");
                if (s.length() >= 1){
                    documentArrayList.clear();
                    locationAdapter.clear();
                    locationAdapter.notifyDataSetChanged();
                    ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
                    Call<CategoryResult> call = null;

                    call = apiInterface.getSearchLocation(getString(R.string.restapi_key),s.toString(), 15);

                    Log.d(TAG,URLEncoder.encode(s.toString()));
                    call.enqueue(new Callback<CategoryResult>() {
                        @Override
                        public void onResponse(Call<CategoryResult> call, Response<CategoryResult> response) {
                            Log.d(TAG,response.toString());
                            if (response.isSuccessful()){
                                Log.d(TAG,"응답 받기 성공");
                                Log.d(TAG, response.body().getDocuments().toString());
                                assert response.body() != null;
                                for (Document document : response.body().getDocuments()){
                                    locationAdapter.addItem(document);
                                }
                                locationAdapter.notifyDataSetChanged();

                            }
                        }

                        @Override
                        public void onFailure(Call<CategoryResult> call, Throwable t) {
                            Log.d(TAG, "텍스트 변화 인지 실패");

                        }
                    });
                }else {
                    if (s.length() <=0){
                        recyclerView.setVisibility(View.GONE);
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        roadStoryAddressEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    Log.d(TAG,"Focus changeListener On : 검색창 누름 인식");
                }else {
                    Log.d(TAG,"Focus changeListener Off : 검색창 꺼짐");
                    recyclerView.setVisibility(View.GONE);
                }
            }
        });

    }
}