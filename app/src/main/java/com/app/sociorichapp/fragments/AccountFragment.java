package com.app.sociorichapp.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.app.sociorichapp.R;
import com.app.sociorichapp.app_utils.ConstantMethods;

import org.json.JSONException;
import org.json.JSONObject;

import static com.app.sociorichapp.app_utils.AppApis.MY_INTEREST;
import static com.app.sociorichapp.app_utils.AppApis.UPDATE_PHONENO;

public class AccountFragment extends Fragment {
    private ImageView nameEdtIcn,phoneEdtIcn;
    private TextView emailTxt,nameTxt,phoneTxt;
    private LinearLayout nameLay,phoneLay;
    private EditText nameEdt,phoneEdt;
    private Button nameSvBtn,nameCnclBtn,phoneSvBtn,phoneCnclBtn;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account,container,false);
        nameEdtIcn = view.findViewById(R.id.name_edit_icn);
        phoneEdtIcn = view.findViewById(R.id.mob_edit_icn);
        emailTxt = view.findViewById(R.id.email_txt);
        nameTxt = view.findViewById(R.id.name_txt);
        phoneTxt = view.findViewById(R.id.phone_txt);
        nameLay = view.findViewById(R.id.profile_edit_lay);
        phoneLay = view.findViewById(R.id.phone_edit_lay);
        nameEdt = view.findViewById(R.id.name_edt);
        phoneEdt = view.findViewById(R.id.phone_edt);
        nameSvBtn = view.findViewById(R.id.name_save_btn);
        nameCnclBtn = view.findViewById(R.id.name_cncl_btn);
        phoneSvBtn = view.findViewById(R.id.phone_save_btn);
        phoneCnclBtn = view.findViewById(R.id.phone_cncl_btn);

        nameEdtIcn.setOnClickListener(v->{
            nameTxt.setVisibility(View.GONE);
            nameLay.setVisibility(View.VISIBLE);
        });
        nameSvBtn.setOnClickListener(v->{
            nameTxt.setVisibility(View.VISIBLE);
            nameLay.setVisibility(View.GONE);
            String nameStr = nameEdt.getText().toString();
            changeUserName(nameStr);
            nameTxt.setText(nameStr);
        });
        phoneEdtIcn.setOnClickListener(v->{
            phoneTxt.setVisibility(View.GONE);
            phoneLay.setVisibility(View.VISIBLE);
        });
        phoneSvBtn.setOnClickListener(v->{
            phoneTxt.setVisibility(View.VISIBLE);
            phoneLay.setVisibility(View.GONE);
            String phoneStr = phoneEdt.getText().toString();
            changePhnName(phoneStr);
            phoneTxt.setText(phoneStr);
        });
        return view;
    }

    private void changeUserName(String nameStr){
        ConstantMethods.showProgressbar(getActivity());
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("displayName",nameStr);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String userToken = ConstantMethods.getStringPreference("user_token",getActivity());
        AndroidNetworking
                .post(MY_INTEREST)
                .addJSONObjectBody(jsonObject)
                .addHeaders("authorization", "Bearer "+userToken)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ConstantMethods.dismissProgressBar();
                        Log.e("res",""+response);
                    }

                    @Override
                    public void onError(ANError anError) {
                        ConstantMethods.dismissProgressBar();
                        Log.e("res",""+anError);
                    }
                });
    }

    private void changePhnName(String phn){
        ConstantMethods.showProgressbar(getActivity());
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("displayName",phn);
            jsonObject.put("phoneCountryCode","91");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String userToken = ConstantMethods.getStringPreference("user_token",getActivity());
        AndroidNetworking
                .post(UPDATE_PHONENO)
                .addJSONObjectBody(jsonObject)
                .addHeaders("authorization", "Bearer "+userToken)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ConstantMethods.dismissProgressBar();
                        Log.e("res",""+response);
                    }

                    @Override
                    public void onError(ANError anError) {
                        ConstantMethods.dismissProgressBar();
                        Log.e("res",""+anError);
                    }
                });
    }
}
