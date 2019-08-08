package com.app.sociorichapp.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.app.sociorichapp.R;
import com.app.sociorichapp.app_utils.ConstantMethods;
import com.app.sociorichapp.modals.FrndModal;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static com.app.sociorichapp.app_utils.AppApis.CANCEL_REQUEST;
import static com.app.sociorichapp.app_utils.AppApis.SEND_REQUEST;

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.FrndHolder> {
    private Context context;
    private List<FrndModal> frndModalList;
    private LayoutInflater layoutInflater;

    public FriendsAdapter(Context context, List<FrndModal> frndModalList){
        this.context = context;
        this.frndModalList = frndModalList;
        layoutInflater = LayoutInflater.from(context);
    }
    @NonNull
    @Override
    public FrndHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = layoutInflater.inflate(R.layout.adapter_friend_list,null);
        return new FrndHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FrndHolder frndHolder, int i) {
        frndHolder.nameTxt.setText(frndModalList.get(i).getfNameStr());
        String imgPath = frndModalList.get(i).getImgUrl();
        String userIdentuty = frndModalList.get(i).getIdentity();
        String friendStatus = frndModalList.get(i).getFriendStatus();
        if(friendStatus.equals("2")){
            frndHolder.connctTxt.setText("Cancel Request");
        }
        if(!imgPath.equals("")) {
            Glide.with(context)
                    .load(imgPath)
                    .placeholder(R.drawable.user_profile)
                    .error(R.drawable.user_profile)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .priority(Priority.HIGH)
                    .into(frndHolder.frndImg);
        }
        frndHolder.connctTxt.setOnClickListener(fc->{
            String buttonTxt = frndHolder.connctTxt.getText().toString();
            if(buttonTxt.equals("Connect")) {
                sendRequest(userIdentuty, frndHolder,SEND_REQUEST);
            }
            else if(buttonTxt.equals("Cancel Request")){
                sendRequest(userIdentuty, frndHolder,CANCEL_REQUEST);
            }
        });
    }

    @Override
    public int getItemCount() {
        return frndModalList.size();
    }

    class FrndHolder extends RecyclerView.ViewHolder {
        public TextView nameTxt,connctTxt;
        public ImageView frndImg;
        public FrndHolder(@NonNull View itemView) {
            super(itemView);
            nameTxt = itemView.findViewById(R.id.fr_name_txt);
            frndImg = itemView.findViewById(R.id.fr_img);
            connctTxt = itemView.findViewById(R.id.connct_txt);
        }
    }

    private void sendRequest(String userIdentity, FrndHolder frndHolder,String apiName){
        String userToken = ConstantMethods.getStringPreference("user_token",context);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("toUserId",userIdentity);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking
                .post(apiName)
                .addHeaders("authorization","Bearer "+userToken)
                .addJSONObjectBody(jsonObject)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String result = response.getString("result");
                            if(result.equals("success")){
                                if(apiName.equals(SEND_REQUEST)) {
                                    Toast.makeText(context, "Request sent", Toast.LENGTH_SHORT).show();
                                    frndHolder.connctTxt.setText("Cancel Request");
                                }
                                else if(apiName.equals(CANCEL_REQUEST)){
                                    Toast.makeText(context, "Request cancel", Toast.LENGTH_SHORT).show();
                                    frndHolder.connctTxt.setText("Connect");
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }
}
