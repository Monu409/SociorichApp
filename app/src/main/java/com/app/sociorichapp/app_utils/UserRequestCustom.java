package com.app.sociorichapp.app_utils;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;

import java.util.HashMap;
import java.util.Map;

public class UserRequestCustom extends com.android.volley.toolbox.StringRequest {
   // M_Shared_Pref m_shared_pref;
    Context context;
    public UserRequestCustom(Context ctx, int method, String url, Response.Listener listener, Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
        context = ctx;
      //  m_shared_pref = new M_Shared_Pref(ctx);

    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        Log.i("response",response.headers.toString());
        Map<String, String> responseHeaders = response.headers;
        String loginstatus = responseHeaders.get("loginstatus");


     /*   if(loginstatus.equalsIgnoreCase("0")){
            Log.i("loginstatus",loginstatus);
      *//*     m_shared_pref.setPrefranceValue(App_Info.Flat_IsLoggedIn, false);
            m_shared_pref.setPrefranceValue(App_Info.Flat_SubUser_Name, "");
            m_shared_pref.setPrefranceValue(App_Info.Flat_SubUser_subid, "");
            Intent in = new Intent(context ,LoginActvity.class );
            in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            in.putExtra("mobile","");
            in.putExtra("logout","header_logout");*//*
            *//* context.startActivity(in);
            ((Activity)context).finish();*//*
        }*/
        return super.parseNetworkResponse(response);
    }
    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> params = new HashMap<String, String>();
        String usertoken= ConstantMethods.getStringPreference("user_token",context);
        params.put("Content-Type", "application/json");
        params.put("Accept", "application/json");
        params.put("authorization", "Bearer "+usertoken);
     /*   params.put("Token", m_shared_pref.getPrefranceStringValue(App_Info.FCM_TOKEN));
        params.put("Appversion", m_shared_pref.getPrefranceStringValue(App_Info.App_Version));
        params.put("Id", m_shared_pref.getPrefranceStringValue(App_Info.Flat_ID_SUBID));
        params.put("Usertype", m_shared_pref.getPrefranceStringValue(App_Info.Flat_User_Type));*/

        return params;
    }


}

