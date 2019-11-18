package com.sociorich.app.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sociorich.app.R;
import com.sociorich.app.activities.ProfileUpActivity;
import com.sociorich.app.adapters.AwardAdapter;
import com.sociorich.app.app_utils.ConstantMethods;
import com.sociorich.app.modals.AwardModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AwardFragment extends Fragment {
    private RecyclerView awrdList;
    private TextView missionTxt;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_award,container,false);
        awrdList = view.findViewById(R.id.awrd_list);
        missionTxt = view.findViewById(R.id.mission_txt);
        awrdList.setLayoutManager(new LinearLayoutManager(getActivity()));
        getAwardData(ProfileUpActivity.recogArr);
        missionTxt.setText(ProfileUpActivity.missionStatement);
        return view;
    }

    private void getAwardData(JSONArray jsonArray){
        List<AwardModel> awardModels = new ArrayList<>();
        try {
            if(jsonArray.length()==0){
                Toast.makeText(getActivity(), "No data found", Toast.LENGTH_SHORT).show();
            }
            else {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String title = jsonObject.getString("title");
                    String date = jsonObject.getString("date");
                    String desc = jsonObject.getString("desc");
                    String showDate = "";
                    if(!date.equals("null")) {
                        showDate = ConstantMethods.getDateAsWeb(date);
                    }
                    AwardModel awardModel = new AwardModel();
                    awardModel.setTitle(title);
                    awardModel.setDate(showDate);
                    awardModel.setDesc(desc);
                    awardModels.add(awardModel);
                }
                AwardAdapter awardAdapter = new AwardAdapter(awardModels, getActivity());
                awrdList.setAdapter(awardAdapter);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
