package com.sociorich.app.adapters;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.sociorich.app.R;
import com.sociorich.app.activities.CommentActivity;
import com.sociorich.app.activities.DashboardActivity;
import com.sociorich.app.activities.Edit_Post;
import com.sociorich.app.activities.FullImageActivity;
import com.sociorich.app.activities.LoginActivity;
import com.sociorich.app.activities.PlayVideoActivity;
import com.sociorich.app.activities.ShowProfileActivity;
import com.sociorich.app.app_utils.CircleImageView;
import com.sociorich.app.app_utils.ConstantMethods;
import com.sociorich.app.app_utils.SpannedGridLayoutManager;
import com.sociorich.app.modals.CommentModal;
import com.sociorich.app.modals.DashModal;
import com.sociorich.app.testpac.CustomAdapter;
import com.sociorich.app.testpac.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import static com.sociorich.app.app_utils.AppApis.BASE_URL;
import static com.sociorich.app.app_utils.AppApis.DELETE_POST;
import static com.sociorich.app.app_utils.AppApis.MY_INSPIRE_VERIFY;
import static com.sociorich.app.app_utils.AppApis.SEND_REWARD;
import static com.sociorich.app.app_utils.AppApis.SHARE_POST;

public class DashbordAdapter extends RecyclerView.Adapter<DashbordAdapter.DashBordHolder> {
    private List<DashModal> dashModals;
    private Context context;
    private LayoutInflater layoutInflater;
    public static String postId;
    public static List<CommentModal> commentModals;

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
        int height = size.y;
        int sst = width - 20;
        dashBordHolder.catTxt.setText(dashModals.get(i).getCatNameStr());
        dashBordHolder.unameTxt.setText(dashModals.get(i).getUserNameStr());
        dashBordHolder.dateTxt.setText(dashModals.get(i).getDateStr());
        String descStr = dashModals.get(i).getDesStr();
        String postStr = dashModals.get(i).getPostDataStr();
        String shrNameStr = dashModals.get(i).getShareUsrName();
        String shrDateStr = dashModals.get(i).getShareDate();
        String shrCatStr = dashModals.get(i).getShareCat();
        dashBordHolder.shrNameTxt.setText(shrNameStr);
        dashBordHolder.shrDateTxt.setText(shrDateStr);
        dashBordHolder.shrCatTxt.setText(shrCatStr);
        boolean isDescStrUrl = URLUtil.isValidUrl(descStr);
        boolean isPostStr = URLUtil.isValidUrl(postStr);
        String loginStatus = ConstantMethods.getStringPreference("login_status", context);
        JSONObject shrObj = dashModals.get(i).getShrObj();
        if(shrObj!=null){
            dashBordHolder.shareView.setVisibility(View.VISIBLE);
            dashBordHolder.sharedTxt.setVisibility(View.VISIBLE);
            dashBordHolder.blankView.setVisibility(View.VISIBLE);
        }
        if(isDescStrUrl){
            dashBordHolder.desTxt.setTextColor(Color.parseColor("#EE124FF0"));
            dashBordHolder.desTxt.setPaintFlags(dashBordHolder.desTxt.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            dashBordHolder.desTxt.setOnClickListener(dd->{
                if (loginStatus.equals("login")) {
                    Uri uri = Uri.parse(descStr);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    context.startActivity(intent);
                }
                else {
                    context.startActivity(new Intent(context, LoginActivity.class));
                }
            });
        }

        if(isPostStr){
            dashBordHolder.postDataTxt.setTextColor(Color.parseColor("#EE124FF0"));
            dashBordHolder.postDataTxt.setPaintFlags(dashBordHolder.postDataTxt.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            dashBordHolder.postDataTxt.setOnClickListener(dd->{
                if (loginStatus.equals("login")) {
                    Uri uri = Uri.parse(descStr);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    context.startActivity(intent);
                }
                else {
                    context.startActivity(new Intent(context, LoginActivity.class));
                }
            });
        }
        dashBordHolder.postDataTxt.setText(dashModals.get(i).getPostDataStr());
        dashBordHolder.desTxt.setText(descStr);
        String profileImgUrl = dashModals.get(i).getProfilePicStr();
        if(profileImgUrl.equals("")){
            dashBordHolder.userProfileImg.setImageResource(R.drawable.user_profile);
        }else {
            Glide.with(context).load(profileImgUrl).into(dashBordHolder.userProfileImg);
        }
        dashBordHolder.insprTxt.setText(dashModals.get(i).getLikeStr() + " Inspire");
        dashBordHolder.vrfyTxt.setText(dashModals.get(i).getVerifyStr() + " Verify");
        String intSocioCredit = dashModals.get(i).getSocioCreStr();
        String remove0 = intSocioCredit.substring(0, intSocioCredit.length() - 2);
        dashBordHolder.crditTxt.setText(remove0 + " Socio Credits");
        dashBordHolder.cmntTxt.setText(dashModals.get(i).getCommentStr() + " Comments");
        postId = dashModals.get(i).getPostIdStr();

        String mVerify = dashModals.get(i).getmVeryfyStr();
        String mLike = dashModals.get(i).getmLikeStr();

        dashBordHolder.vMenuImg.setOnClickListener(v-> {
            if (loginStatus.equals("logout")||loginStatus.equals("")) {
                context.startActivity(new Intent(context, LoginActivity.class));
            }
        });


        if (loginStatus.equals("login")) {
            String userIdentity = ConstantMethods.getUserID(context);
            String currentUsrIdntty = dashModals.get(i).getUserIdentity();
            if (!userIdentity.equals(currentUsrIdntty)) {
                dashBordHolder.rwrdBtn.setVisibility(View.VISIBLE);
                dashBordHolder.shareLay.setVisibility(View.VISIBLE);
                dashBordHolder.vMenuImg.setOnClickListener(v->reportDialog());
            } else {
                dashBordHolder.rwrdBtn.setVisibility(View.GONE);
                dashBordHolder.shareLay.setVisibility(View.GONE);
                String identity = dashModals.get(i).getPostIdStr();
                String postedId = dashModals.get(i).getUserIdentity();
                String title = dashModals.get(i).getPostDataStr();
                String des = dashModals.get(i).getDesStr();
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
//                    jsonObject.put("mediaList",jsonArray);
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
                        sendReward(rewartQty, postId,dashBordHolder);
                        String crntReward = dashBordHolder.rwrdTxt.getText().toString();
                        int rewrdNo = Integer.parseInt(crntReward.replaceAll("[\\D]", ""));
                        int giveReward = Integer.parseInt(rewartQty);
                        int total = rewrdNo+giveReward;
                        dashBordHolder.rwrdLay.setVisibility(View.GONE);
                        dashBordHolder.rwrdTxt.setText("You have been rewarded\n" + total + " Equa Credits to this post.");
                        dashBordHolder.rwrdEdt.setText("");
                        Toast.makeText(activity, "Reward sent successfully", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            dashBordHolder.shareLay.setOnClickListener(s -> sharedPostPopup(i));
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
            commentModals = dashModals.get(i).getCommentModals();
            if (loginStatus.equals("login")) {
                Intent intent = new Intent(context, CommentActivity.class);
                intent.putExtra("post_id", postId);
                intent.putExtra("dash_modal", (Serializable)commentModals);
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

        });
        dashBordHolder.userProfileImg.setOnClickListener(up->{
            if (loginStatus.equals("login")) {
                String identity = dashModals.get(i).getPostOwnerUserId();
                Intent intent = new Intent(context, ShowProfileActivity.class);
                intent.putExtra("user_identity", identity);
                context.startActivity(intent);
            }
            else{
                context.startActivity(new Intent(context, LoginActivity.class));
            }
        });
        dashBordHolder.unameTxt.setOnClickListener(up->{
            if (loginStatus.equals("login")) {
                String identity = dashModals.get(i).getPostOwnerUserId();
                Intent intent = new Intent(context, ShowProfileActivity.class);
                intent.putExtra("user_identity",identity);
                context.startActivity(intent);
            }
            else{
                context.startActivity(new Intent(context, LoginActivity.class));
            }
        });
        int mediaSize = dashModals.get(i).getMediaList().size();
        if (mediaSize==0) {
            dashBordHolder.frameLayout.setVisibility(View.GONE);
        }
        SpannedGridLayoutManager manager = new SpannedGridLayoutManager(
                new SpannedGridLayoutManager.GridSpanLookup() {
                    @Override
                    public SpannedGridLayoutManager.SpanInfo getSpanInfo(int position) {
                        // Conditions for 2x2 items
                        if (position % 6 == 0 || position % 6 == 4) {
                            return new SpannedGridLayoutManager.SpanInfo(2, 2);
                        } else {
                            return new SpannedGridLayoutManager.SpanInfo(1, 1);
                        }
                    }
                },
                3, // number of columns
                1f // how big is default item
        );
        dashBordHolder.frameLayout.setLayoutManager(manager); // set LayoutManager to RecyclerView
        //  call the constructor of CustomAdapter to send the reference and data to Adapter
        CustomAdapter customAdapter = new CustomAdapter(context,dashModals.get(i).getMediaList());
        dashBordHolder.frameLayout.setAdapter(customAdapter);
//        if (mediaSize == 1) {
//            ImageView imageView = new ImageView(context);
//            tcking.github.com.giraffeplayer2.VideoView videoView = new tcking.github.com.giraffeplayer2.VideoView(context);
//            boolean b = isImageFile(dashModals.get(i).getMediaList().get(0));
//            if(!b) {
//                String vdoUrl = dashModals.get(i).getMediaList().get(0);
//                videoView.setVideoPath(vdoUrl);
//                dashBordHolder.frameLayout.addView(videoView);
//                videoView.setOnClickListener(v -> {
//                    if (loginStatus.equals("login")) {
//                        Intent intent = new Intent(context, PlayVideoActivity.class);
//                        intent.putExtra("video_url", vdoUrl);
//                        intent.putExtra("header_name", "Media Post");
//                        context.startActivity(intent);
//                    } else {
//                        context.startActivity(new Intent(context, LoginActivity.class));
//                    }
//                });
//            }
//            else {
//                //VideoView videoView = new VideoView(context);
//                Glide.with(context).load(dashModals.get(i).getMediaList().get(0)).override(600, 300).centerCrop().into(imageView);
//                imageView.setPadding(0, 5, 0, 0);
//                imageView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//                dashBordHolder.frameLayout.addView(imageView);
//                dashBordHolder.frameLayout.setOnClickListener(v -> {
//                    if (loginStatus.equals("login")) {
//                        Intent intent = new Intent(context, FullImageActivity.class);
//                        ArrayList<String> stringArrayList = (ArrayList<String>) dashModals.get(i).getMediaList();
//                        intent.putExtra("all_images", stringArrayList);
//                        intent.putExtra("header_name", "Media Post");
//                        context.startActivity(intent);
//                    } else {
//                        context.startActivity(new Intent(context, LoginActivity.class));
//                    }
//                });
//            }
//        }
//        if (mediaSize == 2) {
//            ImageView imageView = new ImageView(context);
//            Glide.with(context).load(dashModals.get(i).getMediaList().get(0)).into(imageView);
//            imageView.setPadding(0, 5, 0, 0);
//            imageView.setLayoutParams(new FrameLayout.LayoutParams(width / 2,
//                    ViewGroup.LayoutParams.MATCH_PARENT));
//            ImageView imageView1 = new ImageView(context);
//            Glide.with(context).load(dashModals.get(i).getMediaList().get(1)).into(imageView1);
//            imageView1.setLayoutParams(new FrameLayout.LayoutParams(width / 2,
//                    ViewGroup.LayoutParams.MATCH_PARENT));
//            imageView1.setX(width / 2);
//            imageView1.setPadding(5, 5, 0, 0);
////            imageView.setScaleType(ImageView.ScaleType.CENTER);
//
//            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
//            imageView.setAdjustViewBounds(true);
//
////            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
////            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
////            imageView.setScaleType(ImageView.ScaleType.FIT_END);
////            imageView.setScaleType(ImageView.ScaleType.MATRIX);
////            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
//            dashBordHolder.frameLayout.addView(imageView);
//            dashBordHolder.frameLayout.addView(imageView1);
//            imageView.setOnClickListener(v->{
//                if (loginStatus.equals("login")) {
//                    Intent intent = new Intent(context, FullImageActivity.class);
//                    ArrayList<String> stringArrayList = (ArrayList<String>)dashModals.get(i).getMediaList();
//                    intent.putExtra("all_images",stringArrayList);
//                    intent.putExtra("header_name","Media Post");
//                    intent.putExtra("image_position",0);
//                    context.startActivity(intent);
//                }
//                else{
//                    context.startActivity(new Intent(context, LoginActivity.class));
//                }
//            });
//            imageView1.setOnClickListener(v->{
//                if (loginStatus.equals("login")) {
//                    Intent intent = new Intent(context, FullImageActivity.class);
//                    ArrayList<String> stringArrayList = (ArrayList<String>)dashModals.get(i).getMediaList();
//                    intent.putExtra("all_images",stringArrayList);
//                    intent.putExtra("header_name","Media Post");
//                    intent.putExtra("image_position",1);
//                    context.startActivity(intent);
//                }
//                else{
//                    context.startActivity(new Intent(context, LoginActivity.class));
//                }
//            });
//        }
//        if (mediaSize == 3) {
//            ImageView imageView = new ImageView(context);
//            Glide.with(context).load(dashModals.get(i).getMediaList().get(0)).into(imageView);
//            imageView.setPadding(0, 5, 0, 0);
//            imageView.setLayoutParams(new FrameLayout.LayoutParams(width / 2,
//                    400));
//            ImageView imageView1 = new ImageView(context);
//            Glide.with(context).load(dashModals.get(i).getMediaList().get(1)).into(imageView1);
//            imageView1.setX(width / 2);
//            imageView1.setPadding(5, 5, 0, 0);
//            imageView1.setLayoutParams(new FrameLayout.LayoutParams(width / 2,
//                    200));
//            ImageView imageView2 = new ImageView(context);
//            Glide.with(context).load(dashModals.get(i).getMediaList().get(2)).into(imageView2);
//            imageView2.setX(width / 2);
//            imageView2.setY(200);
//            imageView2.setPadding(5, 5, 0, 0);
//            imageView2.setLayoutParams(new FrameLayout.LayoutParams(width / 2,
//                    200));
//            dashBordHolder.frameLayout.addView(imageView);
//            dashBordHolder.frameLayout.addView(imageView1);
//            dashBordHolder.frameLayout.addView(imageView2);
//            imageView.setOnClickListener(v->{
//                if (loginStatus.equals("login")) {
//                    Intent intent = new Intent(context, FullImageActivity.class);
//                    ArrayList<String> stringArrayList = (ArrayList<String>)dashModals.get(i).getMediaList();
//                    intent.putExtra("all_images",stringArrayList);
//                    intent.putExtra("header_name","Media Post");
//                    intent.putExtra("image_position",0);
//                    context.startActivity(intent);
//                }
//                else{
//                    context.startActivity(new Intent(context, LoginActivity.class));
//                }
//            });
//            imageView1.setOnClickListener(v->{
//                if (loginStatus.equals("login")) {
//                    Intent intent = new Intent(context, FullImageActivity.class);
//                    ArrayList<String> stringArrayList = (ArrayList<String>)dashModals.get(i).getMediaList();
//                    intent.putExtra("all_images",stringArrayList);
//                    intent.putExtra("header_name","Media Post");
//                    intent.putExtra("image_position",1);
//                    context.startActivity(intent);
//                }
//                else{
//                    context.startActivity(new Intent(context, LoginActivity.class));
//                }
//            });
//            imageView2.setOnClickListener(v->{
//                if (loginStatus.equals("login")) {
//                    Intent intent = new Intent(context, FullImageActivity.class);
//                    ArrayList<String> stringArrayList = (ArrayList<String>)dashModals.get(i).getMediaList();
//                    intent.putExtra("all_images",stringArrayList);
//                    intent.putExtra("header_name","Media Post");
//                    intent.putExtra("image_position",2);
//                    context.startActivity(intent);
//                }
//                else{
//                    context.startActivity(new Intent(context, LoginActivity.class));
//                }
//            });
//        }
//        if (mediaSize >= 4) {
//            ImageView imageView = new ImageView(context);
//            Glide.with(context).load(dashModals.get(i).getMediaList().get(0)).into(imageView);
//            imageView.setPadding(0, 5, 0, 0);
//            imageView.setLayoutParams(new FrameLayout.LayoutParams(sst / 2, 400));
//            //x=200,y==0
//            ImageView imageView1 = new ImageView(context);
//            Glide.with(context).load(dashModals.get(i).getMediaList().get(1)).into(imageView1);
//            imageView1.setX(sst / 2);
//            imageView1.setPadding(2, 5, 0, 0);
//            imageView1.setLayoutParams(new FrameLayout.LayoutParams(sst / 2, 400 / 3));
//
//            ImageView imageView2 = new ImageView(context);
//            Glide.with(context).load(dashModals.get(i).getMediaList().get(2)).into(imageView2);
//            imageView2.setX(sst / 2);
//            imageView2.setY(400 / 3);
//            imageView2.setPadding(2, 5, 0, 0);
//            imageView2.setLayoutParams(new FrameLayout.LayoutParams(sst / 2, 400 / 3));
//
//            ImageView imageView3 = new ImageView(context);
//            Glide.with(context).load(dashModals.get(i).getMediaList().get(3)).into(imageView3);
//            imageView3.setX(sst / 2);
//            imageView3.setY((400 / 3 + 400 / 3));
//            imageView3.setPadding(2, 5, 0, 0);
//            imageView3.setLayoutParams(new FrameLayout.LayoutParams(sst / 2, 400 / 3));
//
//            TextView textView = new TextView(context);
//            int plusImages = dashModals.get(i).getMediaList().size()-4;
//            textView.setText("+ "+plusImages);
//            textView.setX(sst / 2);
//            textView.setTextColor(Color.parseColor("#ef633f"));
//            textView.setTypeface(null, Typeface.BOLD);
//            textView.setY(400 / 3 + 400 / 3);
//            textView.setGravity(Gravity.CENTER);
//            textView.setTextSize(20);
//            textView.setLayoutParams(new FrameLayout.LayoutParams(sst / 2, 400 / 3));
//            dashBordHolder.frameLayout.addView(imageView);
//            dashBordHolder.frameLayout.addView(imageView1);
//            dashBordHolder.frameLayout.addView(imageView2);
//            dashBordHolder.frameLayout.addView(imageView3);
//            dashBordHolder.frameLayout.addView(textView);
//            imageView.setOnClickListener(v->{
//                if (loginStatus.equals("login")) {
//                    Intent intent = new Intent(context, FullImageActivity.class);
//                    ArrayList<String> stringArrayList = (ArrayList<String>)dashModals.get(i).getMediaList();
//                    intent.putExtra("all_images",stringArrayList);
//                    intent.putExtra("header_name","Media Post");
//                    intent.putExtra("image_position",0);
//                    context.startActivity(intent);
//                }
//                else{
//                    context.startActivity(new Intent(context, LoginActivity.class));
//                }
//            });
//            imageView1.setOnClickListener(v->{
//                if (loginStatus.equals("login")) {
//                    Intent intent = new Intent(context, FullImageActivity.class);
//                    ArrayList<String> stringArrayList = (ArrayList<String>)dashModals.get(i).getMediaList();
//                    intent.putExtra("all_images",stringArrayList);
//                    intent.putExtra("header_name","Media Post");
//                    intent.putExtra("image_position",1);
//                    context.startActivity(intent);
//                }
//                else{
//                    context.startActivity(new Intent(context, LoginActivity.class));
//                }
//            });
//            imageView2.setOnClickListener(v->{
//                if (loginStatus.equals("login")) {
//                    Intent intent = new Intent(context, FullImageActivity.class);
//                    ArrayList<String> stringArrayList = (ArrayList<String>)dashModals.get(i).getMediaList();
//                    intent.putExtra("all_images",stringArrayList);
//                    intent.putExtra("header_name","Media Post");
//                    intent.putExtra("image_position",2);
//                    context.startActivity(intent);
//                }
//                else{
//                    context.startActivity(new Intent(context, LoginActivity.class));
//                }
//            });
//            imageView3.setOnClickListener(v->{
//                if (loginStatus.equals("login")) {
//                    Intent intent = new Intent(context, FullImageActivity.class);
//                    ArrayList<String> stringArrayList = (ArrayList<String>)dashModals.get(i).getMediaList();
//                    intent.putExtra("all_images",stringArrayList);
//                    intent.putExtra("header_name","Media Post");
//                    intent.putExtra("image_position",3);
//                    context.startActivity(intent);
//                }
//                else{
//                    context.startActivity(new Intent(context, LoginActivity.class));
//                }
//            });
//        }
    }

    @Override
    public int getItemCount() {
        return dashModals.size();
    }

    public class DashBordHolder extends RecyclerView.ViewHolder {
        public TextView catTxt, unameTxt, dateTxt, postDataTxt, insprTxt, vrfyTxt,
                cmntTxt, crditTxt, tInsprTxt, tVrfyTxt, rwrdTxt,desTxt,shrNameTxt,
                shrDateTxt,shrCatTxt,sharedTxt;
        public CircleImageView userProfileImg;
        private LinearLayout inspireLay, varifyLay, rwrdLay, shareLay;
        public ImageView inspireImg, verifyImg, vMenuImg;
        RecyclerView frameLayout;
        Button sendRwrd, cnclRwrd;
        EditText rwrdEdt;
        RelativeLayout rewadrLay1,shareView;
        private LinearLayout rwrdBtn;
        View blankView;



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
            shrNameTxt = itemView.findViewById(R.id.sr_uname_txt);
            shrDateTxt = itemView.findViewById(R.id.sr_date_txt);
            shrCatTxt = itemView.findViewById(R.id.sr_category_txt);
            shareView = itemView.findViewById(R.id.share_view);
            sharedTxt = itemView.findViewById(R.id.shared_txt);
            blankView = itemView.findViewById(R.id.blank_view);
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

    private void sendReward(String reward, String postId,DashBordHolder dashBordHolder) {
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
//                                Toast.makeText(context, "Send reward successfully", Toast.LENGTH_SHORT).show();
                                String totalRewrd = dashBordHolder.crditTxt.getText().toString();
////                                totalRewrd = totalRewrd.substring(0, totalRewrd.length() - 2);
//                                String []totalRewrd1 = totalRewrd.split("\\.");
                                int trInt = Integer.parseInt(totalRewrd.replaceAll("[^0-9]", ""));
                                trInt = trInt+ Integer.parseInt(reward);
                                dashBordHolder.crditTxt.setText(""+trInt+" Socio Credits");
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

    private void sharedPostPopup(int position) {
        String identity = dashModals.get(position).getPostIdStr();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Share post");
        String message = BASE_URL+"shared-post/"+identity;
        builder.setMessage(message);
        builder.setPositiveButton("Copy to clipboard", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(context, "Copied", Toast.LENGTH_SHORT).show();
                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("", message);
                clipboard.setPrimaryClip(clip);
            }
        });
        builder.setNegativeButton("Share Post", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                JSONObject shareJSON = dashModals.get(position).getPostObj();
                shareJSON.remove("identity");
                shareJSON.remove("createdBy");
                shareJSON.remove("createdAt");
                shareJSON.remove("modifiedBy");
                shareJSON.remove("modifiedAt");
                shareJSON.remove("socioMoneyDonated");
                shareJSON.remove("expressions");
                shareJSON.remove("taggedUserIds");
                shareJSON.remove("sharedTitle");
                shareJSON.remove("type");
                shareJSON.remove("status");
                shareJSON.remove("sharedId");
                shareJSON.remove("sharedDate");
                shareJSON.remove("postedBy");
                shareJSON.remove("ownerUserId");

                try {
                    String userIdentity = ConstantMethods.getUserID(context);
                    String sharedId = dashModals.get(position).getUserIdentity();
                    String modifiedAt = dashModals.get(position).getLongTime();
                    shareJSON.put("sharedId",sharedId);
                    shareJSON.put("postedBy",userIdentity);
                    shareJSON.put("ownerUserId",userIdentity);
                    shareJSON.put("sharedDate",modifiedAt);
                    sharePost(shareJSON);
                }
                catch (Exception e){
                    e.getStackTrace();
                }

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
                        Toast.makeText(context, "Successfully Shared", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(context, anError.getMessage(), Toast.LENGTH_SHORT).show();
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
                    alertDialogForLogout(identity);
                } else if (options[item].equals("Edit Post")) {
                    dialog.dismiss();
                    Intent intent = new Intent(context, Edit_Post.class);
                    intent.putExtra("json_string",jsonString);
                    Log.e("json",jsonString);
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
                    Toast.makeText(context, "Successfully Sent", Toast.LENGTH_SHORT).show();
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

    private void alertDialogForLogout(String identity){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setTitle("Delete Post");
        builder1.setMessage("Do you want to delete this post?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        deletePost(identity);
                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    public void addMoreData(List<DashModal> dashModalsNew){
        dashModals.addAll(dashModalsNew);
        notifyDataSetChanged();
    }



//    View.OnClickListener galleryLstnr = new View.OnClickListener() {
//        @Override
//        public void onClick(View view) {
//            String loginStatus = ConstantMethods.getStringPreference("login_status", context);
//            if (loginStatus.equals("login")) {
//                Intent intent = new Intent(context, FullImageActivity.class);
//                ArrayList<String> stringArrayList = (ArrayList<String>)dashModals.get(i).getMediaList();
//                intent.putExtra("all_images",stringArrayList);
//                intent.putExtra("header_name","Media Post");
//                intent.putExtra("image_position",0);
//                context.startActivity(intent);
//            }
//            else{
//                context.startActivity(new Intent(context, LoginActivity.class));
//            }
//        }
//    };

    public static boolean isImageFile(String path) {
        String mimeType = URLConnection.guessContentTypeFromName(path);
        return mimeType != null && mimeType.startsWith("image");
    }

}
