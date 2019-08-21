package com.app.sociorichapp.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.app.sociorichapp.R;
import com.app.sociorichapp.activities.CommentActivity;
import com.app.sociorichapp.activities.DashboardActivity;
import com.app.sociorichapp.activities.Edit_Post;
import com.app.sociorichapp.app_utils.CircleImageView;
import com.app.sociorichapp.app_utils.ConstantMethods;
import com.app.sociorichapp.modals.DashModal;
import com.app.sociorichapp.modals.LoginActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static com.app.sociorichapp.app_utils.AppApis.DELETE_POST;
import static com.app.sociorichapp.app_utils.AppApis.MY_INSPIRE_VERIFY;
import static com.app.sociorichapp.app_utils.AppApis.SEND_REWARD;
import static com.app.sociorichapp.app_utils.AppApis.SHARE_POST;

public class DashbordAdapter extends RecyclerView.Adapter<DashbordAdapter.DashBordHolder> {
    private List<DashModal> dashModals;
    private Context context;
    private LayoutInflater layoutInflater;

    public DashbordAdapter(List<DashModal> dashModals, Context context) {
        this.dashModals = dashModals;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public DashBordHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = layoutInflater.inflate(R.layout.adapter_dashbord, null);
        return new DashBordHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DashBordHolder dashBordHolder, int i) {
        AppCompatActivity activity = (AppCompatActivity) context;
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int sst = width - 20;
        dashBordHolder.catTxt.setText(dashModals.get(i).getCatNameStr());
        dashBordHolder.unameTxt.setText(dashModals.get(i).getUserNameStr());
        dashBordHolder.dateTxt.setText(dashModals.get(i).getDateStr());
        String descStr = dashModals.get(i).getDesStr();
        boolean isUrl = URLUtil.isValidUrl(descStr);
        if(isUrl){
            dashBordHolder.desTxt.setTextColor(Color.parseColor("#EE124FF0"));
            dashBordHolder.desTxt.setPaintFlags(dashBordHolder.desTxt.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            dashBordHolder.desTxt.setOnClickListener(dd->{
                Uri uri = Uri.parse(descStr);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                context.startActivity(intent);
            });
        }
        dashBordHolder.postDataTxt.setText(dashModals.get(i).getPostDataStr());
        dashBordHolder.desTxt.setText(descStr);
//        dashBordHolder.desTxt.setText(dashModals.get(i).getDesStr());
        Glide.with(context)
                .load(dashModals.get(i).getProfilePicStr())
                .placeholder(R.drawable.user_profile)
                .error(R.drawable.user_profile)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(com.bumptech.glide.Priority.HIGH)
                .into(dashBordHolder.userProfileImg);
        dashBordHolder.insprTxt.setText(dashModals.get(i).getLikeStr() + " Inspire");
        dashBordHolder.vrfyTxt.setText(dashModals.get(i).getVerifyStr() + " Verify");
        dashBordHolder.crditTxt.setText(dashModals.get(i).getSocioCreStr() + " Socio Credits");
        dashBordHolder.cmntTxt.setText(dashModals.get(i).getCommentStr() + " Comments");
        String postId = dashModals.get(i).getPostIdStr();
        String loginStatus = ConstantMethods.getStringPreference("login_status", context);
        String mVerify = dashModals.get(i).getmVeryfyStr();
        String mLike = dashModals.get(i).getmLikeStr();

        if (loginStatus.equals("login")) {
            String userIdentity = ConstantMethods.getUserID(context);
            String currentUsrIdntty = dashModals.get(i).getUserIdentity();
            if (!userIdentity.equals(currentUsrIdntty)) {
                dashBordHolder.rewadrLay1.setVisibility(View.VISIBLE);
                dashBordHolder.shareLay.setVisibility(View.VISIBLE);
                dashBordHolder.vMenuImg.setOnClickListener(v->reportDialog());
            } else {
                dashBordHolder.rewadrLay1.setVisibility(View.GONE);
                dashBordHolder.shareLay.setVisibility(View.GONE);
                String identity = dashModals.get(i).getPostIdStr();
                String postedId = dashModals.get(i).getUserIdentity();
                String title = dashModals.get(i).getPostDataStr();
                String des = "";
                String categoryId = dashModals.get(i).getCategoryId();
                JSONObject jsonObject = new JSONObject();
                JSONArray jsonArray = new JSONArray();
                try {
                    jsonObject.put("identity",identity);
                    jsonObject.put("postedBy",postedId);
                    jsonObject.put("ownerUserId",postedId);
                    jsonObject.put("title",title);
                    jsonObject.put("desc",des);
                    jsonObject.put("categoryId",categoryId);
                    jsonObject.put("mediaList",jsonArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String stringJson = String.valueOf(jsonObject);
                dashBordHolder.vMenuImg.setOnClickListener(v->deleteEditDialog(identity,stringJson));
            }
            dashBordHolder.rwrdBtn.setOnClickListener(r -> {
                dashBordHolder.rwrdLay.setVisibility(View.VISIBLE);
            });
            dashBordHolder.sendRwrd.setOnClickListener(s -> {
                String rewartQty = dashBordHolder.rwrdEdt.getText().toString();

                if(rewartQty.isEmpty()){
                    Toast.makeText(activity, "Add Some Amount", Toast.LENGTH_SHORT).show();
                }
                else{
                    int rewardQtyInt = Integer.parseInt(rewartQty);
                    if(rewardQtyInt>50){
                        Toast.makeText(activity, "Amount between 1-50 only", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        sendReward(rewartQty, postId);
                        dashBordHolder.rwrdLay.setVisibility(View.GONE);
                        dashBordHolder.rwrdTxt.setText("You have reward " + rewartQty + " Equa Credits\nto this post.");
                        dashBordHolder.rwrdEdt.setText("");
                        Toast.makeText(activity, "Reward sent successfully", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            dashBordHolder.shareLay.setOnClickListener(s -> sharedPostPopup());
            dashBordHolder.cnclRwrd.setOnClickListener(c -> dashBordHolder.rwrdLay.setVisibility(View.GONE));
        } else {
            dashBordHolder.rewadrLay1.setVisibility(View.GONE);
        }

        if (!mVerify.equals("")) {
            dashBordHolder.verifyImg.setImageResource(R.drawable.thumbclick);
        } else {

        }
        if (!mLike.equals("")) {
            dashBordHolder.inspireImg.setImageResource(R.drawable.heart_icon);
        }

        dashBordHolder.cmntTxt.setOnClickListener(v -> {
            if (loginStatus.equals("login")) {
                Intent intent = new Intent(context, CommentActivity.class);
                intent.putExtra("post_id", postId);
                intent.putExtra("dash_modal", dashModals.get(i));
                context.startActivity(intent);
            } else {
                context.startActivity(new Intent(context, LoginActivity.class));
            }
        });
        dashBordHolder.varifyLay.setOnClickListener(v -> {
            if (loginStatus.equals("login")) {
                if (dashBordHolder.verifyImg.getDrawable().getConstantState() == context.getResources().getDrawable(R.drawable.thumbclick).getConstantState()) {
                    String totalInsp = dashBordHolder.vrfyTxt.getText().toString();
                    String numberOnly = totalInsp.replaceAll("[^0-9]", "");
                    int totalInspNum = Integer.parseInt(numberOnly);
                    int afterInsp = totalInspNum - 1;
                    dashBordHolder.vrfyTxt.setText(afterInsp + " Verify");
                    dashBordHolder.verifyImg.setImageResource(R.drawable.thumb);
                } else {
                    checkVerifyAndInspire("VERIFY", postId, dashBordHolder);
                    dashBordHolder.verifyImg.setImageResource(R.drawable.thumbclick);
                }
            } else {
                context.startActivity(new Intent(context, LoginActivity.class));
            }
        });
        dashBordHolder.inspireLay.setOnClickListener(v -> {
            if (loginStatus.equals("login")) {
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
            } else {
                context.startActivity(new Intent(context, LoginActivity.class));
            }
//            dashBordHolder.rwrdBtn.setOnClickListener(s->{
//                dashBordHolder.rwrdLay.setVisibility(View.VISIBLE);
//            });
//            dashBordHolder.sendRwrd.setOnClickListener(s->{
//               String rwrdStr = dashBordHolder.rwrdEdt.getText().toString();
//               sendReward(rwrdStr,postId);
//            });
        });
        int mediaSize = dashModals.get(i).getMediaList().size();
        if (mediaSize == 0) {
            dashBordHolder.frameLayout.setVisibility(View.GONE);
        }
        if (mediaSize == 1) {
            ImageView imageView = new ImageView(context);
            Glide.with(context).load(dashModals.get(i).getMediaList().get(0)).into(imageView);
            imageView.setPadding(0, 5, 0, 0);
            imageView.setLayoutParams(new FrameLayout.LayoutParams(width, 400));
            dashBordHolder.frameLayout.addView(imageView);
        }
        if (mediaSize == 2) {
            ImageView imageView = new ImageView(context);
            Glide.with(context).load(dashModals.get(i).getMediaList().get(0)).into(imageView);
            imageView.setPadding(0, 5, 0, 0);
            imageView.setLayoutParams(new FrameLayout.LayoutParams(width / 2,
                    400));
            ImageView imageView1 = new ImageView(context);
            Glide.with(context).load(dashModals.get(i).getMediaList().get(1)).into(imageView1);
            imageView1.setLayoutParams(new FrameLayout.LayoutParams(width / 2,
                    400));
            imageView1.setX(width / 2);
            imageView1.setPadding(5, 5, 0, 0);
            dashBordHolder.frameLayout.addView(imageView);
            dashBordHolder.frameLayout.addView(imageView1);
        }
        if (mediaSize == 3) {
            ImageView imageView = new ImageView(context);
            Glide.with(context).load(dashModals.get(i).getMediaList().get(0)).into(imageView);
            imageView.setPadding(0, 5, 0, 0);
            imageView.setLayoutParams(new FrameLayout.LayoutParams(width / 2,
                    400));
            ImageView imageView1 = new ImageView(context);
            Glide.with(context).load(dashModals.get(i).getMediaList().get(1)).into(imageView1);
            imageView1.setX(width / 2);
            imageView1.setPadding(5, 5, 0, 0);
            imageView1.setLayoutParams(new FrameLayout.LayoutParams(width / 2,
                    200));
            ImageView imageView2 = new ImageView(context);
            Glide.with(context).load(dashModals.get(i).getMediaList().get(2)).into(imageView2);
            imageView2.setX(width / 2);
            imageView2.setY(200);
            imageView2.setPadding(5, 5, 0, 0);
            imageView2.setLayoutParams(new FrameLayout.LayoutParams(width / 2,
                    200));
            dashBordHolder.frameLayout.addView(imageView);
            dashBordHolder.frameLayout.addView(imageView1);
            dashBordHolder.frameLayout.addView(imageView2);
        }
        if (mediaSize >= 4) {
            ImageView imageView = new ImageView(context);
            Glide.with(context).load(dashModals.get(i).getMediaList().get(0)).into(imageView);
            imageView.setPadding(0, 5, 0, 0);
            imageView.setLayoutParams(new FrameLayout.LayoutParams(sst / 2, 400));
            //x=200,y==0
            ImageView imageView1 = new ImageView(context);
            Glide.with(context).load(dashModals.get(i).getMediaList().get(1)).into(imageView1);
            imageView1.setX(sst / 2);
            imageView1.setPadding(2, 5, 0, 0);
            imageView1.setLayoutParams(new FrameLayout.LayoutParams(sst / 2, 400 / 3));

            ImageView imageView2 = new ImageView(context);
            Glide.with(context).load(dashModals.get(i).getMediaList().get(2)).into(imageView2);
            imageView2.setX(sst / 2);
            imageView2.setY(400 / 3);
            imageView2.setPadding(2, 5, 0, 0);
            imageView2.setLayoutParams(new FrameLayout.LayoutParams(sst / 2, 400 / 3));

            ImageView imageView3 = new ImageView(context);
            Glide.with(context).load(dashModals.get(i).getMediaList().get(3)).into(imageView3);
            imageView3.setX(sst / 2);
            imageView3.setY((400 / 3 + 400 / 3));
            imageView3.setPadding(2, 5, 0, 0);
            imageView3.setLayoutParams(new FrameLayout.LayoutParams(sst / 2, 400 / 3));

            TextView textView = new TextView(context);
            textView.setText("+ 6");
            textView.setX(sst / 2);
            textView.setTextColor(Color.parseColor("#ffffff"));
            textView.setTypeface(null, Typeface.BOLD);
            textView.setY(400 / 3 + 400 / 3);
            textView.setGravity(Gravity.CENTER);
            textView.setTextSize(20);
            textView.setLayoutParams(new FrameLayout.LayoutParams(sst / 2, 400 / 3));
            dashBordHolder.frameLayout.addView(imageView);
            dashBordHolder.frameLayout.addView(imageView1);
            dashBordHolder.frameLayout.addView(imageView2);
            dashBordHolder.frameLayout.addView(imageView3);
            dashBordHolder.frameLayout.addView(textView);
        }
    }

    @Override
    public int getItemCount() {
        return dashModals.size();
    }

    public class DashBordHolder extends RecyclerView.ViewHolder {
        public TextView catTxt, unameTxt, dateTxt, postDataTxt, insprTxt, vrfyTxt,
                cmntTxt, crditTxt, tInsprTxt, tVrfyTxt, rwrdTxt,desTxt;
        public CircleImageView userProfileImg;
        private LinearLayout inspireLay, varifyLay, rwrdLay, shareLay;
        public ImageView inspireImg, verifyImg, vMenuImg;
        FrameLayout frameLayout;
        Button sendRwrd, cnclRwrd;
        EditText rwrdEdt;
        RelativeLayout rewadrLay1;
        private LinearLayout rwrdBtn;

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
            rwrdTxt = itemView.findViewById(R.id.rewrd_txt);
            rwrdLay = itemView.findViewById(R.id.reward_lay);
            rwrdBtn = itemView.findViewById(R.id.reward_btn);
            rwrdEdt = itemView.findViewById(R.id.rewrd_edt);
            sendRwrd = itemView.findViewById(R.id.send_rwrd);
            rewadrLay1 = itemView.findViewById(R.id.reward_lay1);
            cnclRwrd = itemView.findViewById(R.id.cncl_rwrd);
            shareLay = itemView.findViewById(R.id.share_lay);
            vMenuImg = itemView.findViewById(R.id.v_menu_img);
            desTxt = itemView.findViewById(R.id.des_txt);
        }
    }

    private void checkVerifyAndInspire(String changeValue, String postId, DashBordHolder dashBordHolder) {
        String token = ConstantMethods.getStringPreference("user_token", context);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("postId", postId);
            jsonObject.put("type", changeValue);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking
                .post(MY_INSPIRE_VERIFY)
                .addJSONObjectBody(jsonObject)
                .addHeaders("authorization", "Bearer " + token)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String result = response.getString("result");
                            if (result.equals("success")) {
                                if (changeValue.equals("LIKE")) {
                                    String totalInsp = dashBordHolder.insprTxt.getText().toString();
                                    String numberOnly = totalInsp.replaceAll("[^0-9]", "");
                                    int totalInspNum = Integer.parseInt(numberOnly);
                                    int afterInsp = totalInspNum + 1;
                                    dashBordHolder.insprTxt.setText(afterInsp + " Inspire");
                                } else if (changeValue.equals("VERIFY")) {
                                    String totalInsp = dashBordHolder.vrfyTxt.getText().toString();
                                    String numberOnly = totalInsp.replaceAll("[^0-9]", "");
                                    int totalInspNum = Integer.parseInt(numberOnly);
                                    int afterInsp = totalInspNum + 1;
                                    dashBordHolder.vrfyTxt.setText(afterInsp + " Verify");
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

    private void sendReward(String reward, String postId) {
        String token = ConstantMethods.getStringPreference("user_token", context);
        String userId = ConstantMethods.getUserID(context);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("postId", postId);
            jsonObject.put("byUserId", userId);
            jsonObject.put("amount", reward);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking
                .post(SEND_REWARD)
                .addJSONObjectBody(jsonObject)
                .addHeaders("authorization", "Bearer " + token)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("res", "" + response);
                        try {
                            String result = response.getString("result");
                            if (result.equals("success")) {
                                Toast.makeText(context, "Send reward successfully", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("res", "" + anError);
                    }
                });
    }

    private void sharedPostPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Share this post to your social network");
        builder.setPositiveButton("Copy to clipboard", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(context, "Copied", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Share Post", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(context, "Successfully Shared", Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();
    }

    private void sharePost(JSONObject jsonObject) {
        String token = ConstantMethods.getStringPreference("user_token", context);
        AndroidNetworking
                .post(SHARE_POST)
                .addJSONObjectBody(jsonObject)
                .addHeaders("authorization", "Bearer " + token)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

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

    private void deleteEditDialog(String identity,String jsonString) {
        final CharSequence[] options = {"Edit Post", "Delete Post"};
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);
        builder.setTitle("Select Option");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Delete Post")) {
                    dialog.dismiss();
                    deletePost(identity);
                } else if (options[item].equals("Edit Post")) {
                    dialog.dismiss();
                    Intent intent = new Intent(context, Edit_Post.class);
                    intent.putExtra("json_string",jsonString);
                    context.startActivity(intent);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void reportDialog(){
        final CharSequence[] options = {"Report Post"};
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Report Post")) {
                    dialog.dismiss();
                    showReportEdit();
                }
            }
        });
        builder.show();
    }

    private void showReportEdit(){
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        final EditText edittext = new EditText(context);
        alert.setTitle("GIVE YOUR FEEDBACK ON THIS POST");
        alert.setView(edittext);
        alert.setPositiveButton("REPORT POST", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String YouEditTextStr = edittext.getText().toString();
                if(YouEditTextStr.isEmpty()){
                    Toast.makeText(context, "Enter your feedback", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(context, "Successfull", Toast.LENGTH_SHORT).show();
                }
            }
        });
        alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });

        alert.show();
    }

    private void deletePost(String identity){
        String token = ConstantMethods.getStringPreference("user_token", context);
        AndroidNetworking
                .delete(DELETE_POST+identity)
                .addHeaders("authorization", "Bearer " + token)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(context, "Post deleted", Toast.LENGTH_SHORT).show();
                        ((AppCompatActivity)context).finish();
                        Intent intent = new Intent(context, DashboardActivity.class);
                        context.startActivity(intent);
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }

}
