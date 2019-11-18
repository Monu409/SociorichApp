package com.sociorich.app.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.sociorich.app.R;
import com.sociorich.app.activities.ShowProfileActivity;
import com.sociorich.app.app_utils.AppApis;
import com.sociorich.app.app_utils.CircleImageView;
import com.sociorich.app.app_utils.ConstantMethods;
import com.sociorich.app.modals.FRModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static com.sociorich.app.app_utils.AppApis.FRIEND_REQUEST_ACCEPT;
import static com.sociorich.app.app_utils.AppApis.FRIEND_REQUEST_REJECT;

public class FriendRequestAdapter extends RecyclerView.Adapter<FriendRequestAdapter.FRHolder>{
    private List<FRModel> frModels;
    private Context context;
    private LayoutInflater layoutInflater;

    public FriendRequestAdapter(List<FRModel> frModels, Context context){
        this.frModels = frModels;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        if(frModels.size()==0){
            Toast.makeText(context, "No Data Found", Toast.LENGTH_SHORT).show();
        }
    }
    @NonNull
    @Override
    public FRHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.adapter_friend_request,null);
        return new FRHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FRHolder holder, int position) {
        String imgPath = frModels.get(position).getProImg();
        Glide.with(context).load(imgPath).into(holder.circleImageView);
        holder.nameTxt.setText(frModels.get(position).getUserName());
        holder.mutualTxt.setText(frModels.get(position).getMotualCon());
        holder.acceptTxt.setOnClickListener(v->acceptFriendRequest(FRIEND_REQUEST_ACCEPT,"Request Accepted"));
        holder.ignoreTxt.setOnClickListener(v->acceptFriendRequest(FRIEND_REQUEST_REJECT,"Request Rejected"));
        holder.nameTxt.setOnClickListener(v->{
            String identity = frModels.get(position).getIdentity();
            Intent intent = new Intent(context, ShowProfileActivity.class);
            intent.putExtra("user_identity", identity);
            context.startActivity(intent);
        });
        holder.circleImageView.setOnClickListener(v->{
            String identity = frModels.get(position).getIdentity();
            Intent intent = new Intent(context, ShowProfileActivity.class);
            intent.putExtra("user_identity", identity);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return frModels.size();
    }

    public class FRHolder extends RecyclerView.ViewHolder {
        public CircleImageView circleImageView;
        public TextView nameTxt,mutualTxt,acceptTxt,ignoreTxt;
        public FRHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.user_img);
            nameTxt = itemView.findViewById(R.id.name_txt);
            mutualTxt = itemView.findViewById(R.id.mutual_txt);
            acceptTxt = itemView.findViewById(R.id.accept_txt);
            ignoreTxt = itemView.findViewById(R.id.ignore_txt);
        }
    }

    private void acceptFriendRequest(String apiName, String message){
        String token = ConstantMethods.getStringPreference("user_token", context);
        String userId = ConstantMethods.getUserID(context);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("fromUserId",userId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking
                .post(apiName)
                .addJSONObjectBody(jsonObject)
                .addHeaders("authorization", "Bearer " + token)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String result = response.getString("result");
                            if(result.equals("success")){
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(context, anError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
