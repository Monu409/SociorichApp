package com.sociorich.app.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.sociorich.app.R;
import com.sociorich.app.activities.DashboardActivity;
import com.sociorich.app.app_utils.ConstantMethods;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import static com.sociorich.app.app_utils.AppApis.MY_INTEREST;

public class DeleteIntrestAdapter extends RecyclerView.Adapter<DeleteIntrestAdapter.QtyHolder> {
    private List<String> qltyList;
    private Context context;
    List<String> catKey;
    private LayoutInflater layoutInflater;

    public DeleteIntrestAdapter(List<String> qltyList,List<String> catKey, Context context){
        this.qltyList = qltyList;
        this.catKey = catKey;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }
    @NonNull
    @Override
    public QtyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = layoutInflater.inflate(R.layout.adapter_dlt_intrst,null);
        return new QtyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QtyHolder qtyHolder, int i) {
        qtyHolder.qltyTxt.setText(qltyList.get(i));
//        List<String> qltyList = ConstantMethods.getArrayListShared(context,"quality_key");
        qtyHolder.deleteImg.setOnClickListener(d->{
            alertDialogForDelete(i);
        });
    }

    @Override
    public int getItemCount() {
        return qltyList.size();
    }

    public class QtyHolder extends RecyclerView.ViewHolder {
        public TextView qltyTxt;
        public ImageView deleteImg;
        public QtyHolder(@NonNull View itemView) {
            super(itemView);
            qltyTxt = itemView.findViewById(R.id.qlty_txt);
            deleteImg = itemView.findViewById(R.id.delete_img);
        }
    }
    private void alertDialogForDelete(int i){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setTitle("Delete Interest");
        builder1.setMessage("Do you want to delete this interest?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Map<String,String> catMap = DashboardActivity.catMap;
                        for (Map.Entry<String, String> entry : catMap.entrySet()) {
                            if (entry.getValue().equals(qltyList.get(i))) {
                                System.out.println(entry.getKey());
                                catKey.remove(entry.getKey());
                            }
                        }
                        qltyList.remove(i);
                        notifyItemRemoved(i);
                        notifyItemRangeChanged(i, qltyList.size());
                        setInterestValues(catKey);

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

    private void setInterestValues(List<String> selectedValues) {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < selectedValues.size(); i++) {
            try {
                jsonArray.put(i, selectedValues.get(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        try {
            jsonObject.put("interestCategories", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String userToken = ConstantMethods.getStringPreference("user_token", context);
        AndroidNetworking
                .post(MY_INTEREST)
                .addHeaders("authorization", "Bearer " + userToken)
                .addJSONObjectBody(jsonObject)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("test", "" + response);
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("test", "" + anError);
                    }
                });
    }
}
