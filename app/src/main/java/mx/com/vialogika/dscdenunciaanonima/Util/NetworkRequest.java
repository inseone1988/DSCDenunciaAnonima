package mx.com.vialogika.dscdenunciaanonima.Util;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import static net.gotev.uploadservice.Placeholders.*;
import net.gotev.uploadservice.UploadNotificationConfig;

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
}
