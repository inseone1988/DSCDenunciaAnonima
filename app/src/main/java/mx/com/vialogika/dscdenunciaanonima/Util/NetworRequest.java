package mx.com.vialogika.dscdenunciaanonima.Util;

import com.android.volley.VolleyError;

public interface NetworRequest {
    void onResponse(Object Response);
    void onRequestError(VolleyError error);
}
