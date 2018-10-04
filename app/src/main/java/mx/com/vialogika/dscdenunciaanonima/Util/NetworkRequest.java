package mx.com.vialogika.dscdenunciaanonima.Util;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import static net.gotev.uploadservice.Placeholders.*;
import net.gotev.uploadservice.UploadNotificationConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import mx.com.vialogika.dscdenunciaanonima.Report;

import static net.gotev.uploadservice.Placeholders.ELAPSED_TIME;
import static net.gotev.uploadservice.Placeholders.PROGRESS;
import static net.gotev.uploadservice.Placeholders.TOTAL_FILES;
import static net.gotev.uploadservice.Placeholders.UPLOADED_FILES;
import static net.gotev.uploadservice.Placeholders.UPLOAD_RATE;

public class NetworkRequest {

    public static UploadNotificationConfig defaultNotificationConfig(){
        String Title = "Subiendo Archivos";
        UploadNotificationConfig config = new UploadNotificationConfig();
        config.setTitleForAllStatuses(Title)
                .setRingToneEnabled(true);
        config.getProgress().message = "Subido " + UPLOADED_FILES + " de " + TOTAL_FILES + " a " + UPLOAD_RATE + "-" + PROGRESS;
        config.getCompleted().message = "La carga se ha completado en " + ELAPSED_TIME;
        config.getError().message = "La carga se ha cancelado";
        config.getCancelled().message = "La carga se ha cancelado";
        return config;
    }

    public static void uploadReport(Context context, Report report, final NetworRequest callbacks){
        if(report.getUUID() == null){
            report.setUUID(UUID.randomUUID().toString());
        }
        RequestQueue rq = Volley.newRequestQueue(context);
        Gson gson = new Gson();
        try{
            final JSONObject data = new JSONObject();
            data.put("function","saveDEReport");
            data.put("data",gson.toJson(report));
            String url = "https://www.vialogika.com.mx/dscic/raw.php";
            JsonObjectRequest sr = new JsonObjectRequest(Request.Method.POST, url,data, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    callbacks.onResponse(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    callbacks.onRequestError(error);
                    error.printStackTrace();
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> params = super.getParams();
                    params.put("function","saveDEReport");
                    return params;
                }
            };
            rq.add(sr);
        }catch(JSONException e){
            e.printStackTrace();
        }
    }
}
