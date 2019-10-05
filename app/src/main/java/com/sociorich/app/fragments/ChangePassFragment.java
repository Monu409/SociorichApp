package com.sociorich.app.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.sociorich.app.R;
import com.sociorich.app.app_utils.ConstantMethods;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.sociorich.app.app_utils.AppApis.CHANGE_PASSWORD;

public class ChangePassFragment extends Fragment {
    private EditText crntPassEdt,newPassEdt,reTypeEdt;
    private Button saveBtn;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_pass,container,false);
        crntPassEdt = view.findViewById(R.id.curnt_pass_edt);
        newPassEdt = view.findViewById(R.id.new_pass_edt);
        reTypeEdt = view.findViewById(R.id.retype_pass_edt);
        saveBtn = view.findViewById(R.id.submit_btn);
        saveBtn.setOnClickListener(v->changePassword());
        return view;
    }

    private void changePassword(){
        String crntPassStr = crntPassEdt.getText().toString();
        String newPassStr = newPassEdt.getText().toString();
        String reTypeStr = reTypeEdt.getText().toString();
        if(crntPassStr.isEmpty()||newPassStr.isEmpty()||reTypeStr.isEmpty()){
            Toast.makeText(getActivity(), "All Fields are mandatory", Toast.LENGTH_SHORT).show();
        }
        else if(!newPassStr.equals(reTypeStr)){
            Toast.makeText(getActivity(), "Re-type Password is not match", Toast.LENGTH_SHORT).show();
        }
        else{
            ConstantMethods.showProgressbar(getActivity());
            String userToken = ConstantMethods.getStringPreference("user_token",getActivity());
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("oldPassword",crntPassStr);
                jsonObject.put("newPassword",newPassStr);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            AndroidNetworking
                    .post(CHANGE_PASSWORD)
                    .addHeaders("authorization","Bearer "+userToken)
                    .addJSONObjectBody(jsonObject)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            ConstantMethods.dismissProgressBar();
                            try {
                                String result = response.getString("result");
                                if(result.equals("success")){
                                    Toast.makeText(getActivity(),"Password Changed Successfully",Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
                            ConstantMethods.dismissProgressBar();
                            String errJson = anError.getErrorBody();
                            try {
                                JSONObject errObj = new JSONObject(errJson);
                                JSONArray jsonArray = errObj.getJSONArray("errors");
                                JSONObject childErr = jsonArray.getJSONObject(0);
                                String errorMessage = childErr.getString("errorMessage");
                                Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        }
    }
}
