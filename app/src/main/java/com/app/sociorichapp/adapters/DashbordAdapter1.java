package com.app.sociorichapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.app.sociorichapp.R;
import com.app.sociorichapp.activities.CommentActivity;
import com.app.sociorichapp.app_utils.CircleImageView;
import com.app.sociorichapp.app_utils.ConstantMethods;
import com.app.sociorichapp.modals.DashModal;
import com.app.sociorichapp.activities.LoginActivity;
import com.bumptech.glide.Glide;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.List;
import static com.app.sociorichapp.app_utils.AppApis.MY_INSPIRE_VERIFY;

public abstract class DashbordAdapter1 extends RecyclerView.Adapter<DashbordAdapter1.DashBordHolder> {
    private List<DashModal> dashModals;
    private Context context;
    private LayoutInflater layoutInflater;
    public DashbordAdapter1(List<DashModal> dashModals, Context context){
        this.dashModals = dashModals;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }
    public abstract void load();
    @NonNull
    @Override
    public DashBordHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = layoutInflater.inflate(R.layout.adapter_dashbord,null);
        return new DashBordHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DashBordHolder dashBordHolder, int i) {
        AppCompatActivity activity = (AppCompatActivity) context;
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int sst=width-20;
        dashBordHolder.catTxt.setText(dashModals.get(i).getCatNameStr());
        dashBordHolder.unameTxt.setText(dashModals.get(i).getUserNameStr());
        dashBordHolder.dateTxt.setText(dashModals.get(i).getDateStr());
        dashBordHolder.postDataTxt.setText(dashModals.get(i).getPostDataStr());
        Glide.with(context).load(dashModals.get(i).getProfilePicStr()).into(dashBordHolder.userProfileImg);
        dashBordHolder.insprTxt.setText(dashModals.get(i).getLikeStr()+" Inspire");
        dashBordHolder.vrfyTxt.setText(dashModals.get(i).getVerifyStr()+" Verify");
        dashBordHolder.crditTxt.setText(dashModals.get(i).getVerifyStr()+" Socio Credits");
        dashBordHolder.cmntTxt.setText(dashModals.get(i).getCommentStr()+" Comments");
        String postId = dashModals.get(i).getPostIdStr();
        String loginStatus = ConstantMethods.getStringPreference("login_status",context);
        String mVerify = dashModals.get(i).getmVeryfyStr();
        String mLike = dashModals.get(i).getmLikeStr();
        if(!mVerify.equals("")){
            dashBordHolder.verifyImg.setImageResource(R.drawable.thumbclick);
        }
        else{

        }
        if(!mLike.equals("")){
            dashBordHolder.inspireImg.setImageResource(R.drawable.heart_icon);
        }

        dashBordHolder.cmntTxt.setOnClickListener(v->{
            if(loginStatus.equals("login")) {
                Intent intent = new Intent(context, CommentActivity.class);
                intent.putExtra("post_id", postId);
                intent.putExtra("dash_modal",dashModals.get(i));
                context.startActivity(intent);
            }
            else{
                context.startActivity(new Intent(context, LoginActivity.class));
            }
        });
        dashBordHolder.varifyLay.setOnClickListener(v->{
            if(loginStatus.equals("login")){
                if(dashBordHolder.verifyImg.getDrawable().getConstantState()==context.getResources().getDrawable(R.drawable.thumbclick).getConstantState()){
                    String totalInsp = dashBordHolder.vrfyTxt.getText().toString();
                    String numberOnly= totalInsp.replaceAll("[^0-9]", "");
                    int totalInspNum = Integer.parseInt(numberOnly);
                    int afterInsp = totalInspNum-1;
                    dashBordHolder.vrfyTxt.setText(afterInsp+" Verify");
                    dashBordHolder.verifyImg.setImageResource(R.drawable.thumb);
                }
                else{
                    checkVerifyAndInspire("VERIFY",postId,dashBordHolder);
                    dashBordHolder.verifyImg.setImageResource(R.drawable.thumbclick);
                }
            }
            else{
                context.startActivity(new Intent(context, LoginActivity.class));
            }
        });
        dashBordHolder.inspireLay.setOnClickListener(v->{
            if(loginStatus.equals("login")) {
                if (dashBordHolder.inspireImg.getDrawable().getConstantState() == context.getResources().getDrawable(R.drawable.heart_icon).getConstantState()) {
                    String totalInsp = dashBordHolder.insprTxt.getText().toString();
                    String numberOnly = totalInsp.replaceAll("[^0-9]", "");
                    int totalInspNum = Integer.parseInt(numberOnly);
                    int afterInsp = totalInspNum - 1;
                    dashBordHolder.insprTxt.setText(afterInsp + " Inspire");
                    dashBordHolder.inspireImg.setImageResource(R.drawable.heart);
                } else {
                    checkVerifyAndInspire("LIKE", postId, dashBordHolder);
                    dashBordHolder.inspireImg.setImageResource(R.drawable.heart_icon);
                }
            }
            else{
                context.startActivity(new Intent(context, LoginActivity.class));
            }
        });
        int mediaSize = dashModals.get(i).getMediaList().size();
        if(mediaSize == 0){
            dashBordHolder.frameLayout.setVisibility(View.GONE);
        }
        if(mediaSize==1) {
            ImageView imageView = new ImageView(context);
            Glide.with(context).load(dashModals.get(i).getMediaList().get(0)).into(imageView);
            imageView.setPadding(0,5,0,0);
            imageView.setLayoutParams(new FrameLayout.LayoutParams(width,400));
            dashBordHolder.frameLayout.addView(imageView);
        }
        if(mediaSize == 2) {
            ImageView imageView = new ImageView(context);
            Glide.with(context).load(dashModals.get(i).getMediaList().get(0)).into(imageView);
            imageView.setPadding(0,5,0,0);
            imageView.setLayoutParams(new FrameLayout.LayoutParams(width/2,
                    400));
            ImageView imageView1 = new ImageView(context);
            Glide.with(context).load(dashModals.get(i).getMediaList().get(1)).into(imageView1);
            imageView1.setLayoutParams(new FrameLayout.LayoutParams(width/2,
                    400));
            imageView1.setX(width/2);
            imageView1.setPadding(5,5,0,0);
            dashBordHolder.frameLayout.addView(imageView);
            dashBordHolder.frameLayout.addView(imageView1);
        }
        if(mediaSize == 3) {
            ImageView imageView = new ImageView(context);
            Glide.with(context).load(dashModals.get(i).getMediaList().get(0)).into(imageView);
            imageView.setPadding(0,5,0,0);
            imageView.setLayoutParams(new FrameLayout.LayoutParams(width/2,
                    400));
            ImageView imageView1 = new ImageView(context);
            Glide.with(context).load(dashModals.get(i).getMediaList().get(1)).into(imageView1);
            imageView1.setX(width/2);
            imageView1.setPadding(5,5,0,0);
            imageView1.setLayoutParams(new FrameLayout.LayoutParams(width/2,
                    200));
            ImageView imageView2 = new ImageView(context);
            Glide.with(context).load(dashModals.get(i).getMediaList().get(2)).into(imageView2);
            imageView2.setX(width/2);
            imageView2.setY(200);
            imageView2.setPadding(5,5,0,0);
            imageView2.setLayoutParams(new FrameLayout.LayoutParams(width/2,
                    200));
            dashBordHolder.frameLayout.addView(imageView);
            dashBordHolder.frameLayout.addView(imageView1);
            dashBordHolder.frameLayout.addView(imageView2);
        }
        if(mediaSize >= 4) {
            ImageView imageView = new ImageView(context);
            Glide.with(context).load(dashModals.get(i).getMediaList().get(0)).into(imageView);
            imageView.setPadding(0,5,0,0);
            imageView.setLayoutParams(new FrameLayout.LayoutParams(sst/2,400));
            //x=200,y==0
            ImageView imageView1 = new ImageView(context);
            Glide.with(context).load(dashModals.get(i).getMediaList().get(1)).into(imageView1);
            imageView1.setX(sst/2);
            imageView1.setPadding(2,5,0,0);
            imageView1.setLayoutParams(new FrameLayout.LayoutParams(sst/2,400/3));

            ImageView imageView2 = new ImageView(context);
            Glide.with(context).load(dashModals.get(i).getMediaList().get(2)).into(imageView2);
            imageView2.setX(sst/2);
            imageView2.setY(400/3);
            imageView2.setPadding(2,5,0,0);
            imageView2.setLayoutParams(new FrameLayout.LayoutParams(sst/2,400/3));

            ImageView imageView3= new ImageView(context);
            Glide.with(context).load(dashModals.get(i).getMediaList().get(3)).into(imageView3);
            imageView3.setX(sst/2);
            imageView3.setY((400/3+400/3));
            imageView3.setPadding(2,5,0,0);
            imageView3.setLayoutParams(new FrameLayout.LayoutParams(sst/2,400/3));

            TextView textView=new TextView(context);
            textView.setText("+ 6");
            textView.setX(sst/2);
            textView.setTextColor(Color.parseColor("#ffffff"));
            textView.setTypeface(null, Typeface.BOLD);
            textView.setY(400/3+400/3);
            textView.setGravity(Gravity.CENTER);
            textView.setTextSize(20);
            textView.setLayoutParams(new FrameLayout.LayoutParams(sst/2,400/3));
            dashBordHolder.frameLayout.addView(imageView);
            dashBordHolder.frameLayout.addView(imageView1);
            dashBordHolder.frameLayout.addView(imageView2);
            dashBordHolder.frameLayout.addView(imageView3);
            dashBordHolder.frameLayout.addView(textView);
        }
        if ((i >= getItemCount() - 1)) {
            load();
        }
    }

    @Override
    public int getItemCount() {
        return dashModals.size();
    }

    public class DashBordHolder extends RecyclerView.ViewHolder {
        public TextView catTxt,unameTxt,dateTxt,postDataTxt,insprTxt,vrfyTxt,cmntTxt,crditTxt,tInsprTxt,tVrfyTxt;
        public CircleImageView userProfileImg;
        private LinearLayout inspireLay,varifyLay;
        public ImageView inspireImg,verifyImg;
        FrameLayout frameLayout;
        public DashBordHolder(@NonNull View itemView) {
            super(itemView);
            catTxt = itemView.findViewById(R.id.category_txt);
            unameTxt = itemView.findViewById(R.id.uname_txt);
            dateTxt = itemView.findViewById(R.id.date_txt);
            postDataTxt = itemView.findViewById(R.id.post_txt);
            userProfileImg = itemView.findViewById(R.id.profil_pic);
            insprTxt = itemView.findViewById(R.id.inspr_txt);
            vrfyTxt = itemView.findViewById(R.id.verfy_txt);
            cmntTxt = itemView.findViewById(R.id.cmnt_txt);
            crditTxt = itemView.findViewById(R.id.scredit_txt);
            tInsprTxt = itemView.findViewById(R.id.t_inspire_txt);
            tVrfyTxt = itemView.findViewById(R.id.t_vrfy_txt);
            frameLayout = itemView.findViewById(R.id.image_grid);
            inspireLay = itemView.findViewById(R.id.inspire_lay);
            varifyLay = itemView.findViewById(R.id.verify_lay);
            inspireImg = itemView.findViewById(R.id.insp_img);
            verifyImg = itemView.findViewById(R.id.verf_img);
        }
    }

    private void checkVerifyAndInspire(String changeValue, String postId, DashBordHolder dashBordHolder){
        String token = ConstantMethods.getStringPreference("user_token",context);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("postId",postId);
            jsonObject.put("type",changeValue);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking
                .post(MY_INSPIRE_VERIFY)
                .addJSONObjectBody(jsonObject)
                .addHeaders("authorization","Bearer "+token)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String result = response.getString("result");
                            if(result.equals("success")){
                                if(changeValue.equals("LIKE")){
                                    String totalInsp = dashBordHolder.insprTxt.getText().toString();
                                    String numberOnly= totalInsp.replaceAll("[^0-9]", "");
                                    int totalInspNum = Integer.parseInt(numberOnly);
                                    int afterInsp = totalInspNum+1;
                                    dashBordHolder.insprTxt.setText(afterInsp+" Inspire");
                                }
                                else if(changeValue.equals("VERIFY")){
                                    String totalInsp = dashBordHolder.vrfyTxt.getText().toString();
                                    String numberOnly= totalInsp.replaceAll("[^0-9]", "");
                                    int totalInspNum = Integer.parseInt(numberOnly);
                                    int afterInsp = totalInspNum+1;
                                    dashBordHolder.vrfyTxt.setText(afterInsp+" Verify");
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

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
