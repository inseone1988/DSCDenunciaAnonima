package mx.com.vialogika.dscdenunciaanonima.Util;

import android.content.Context;
import android.support.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import mx.com.vialogika.dscdenunciaanonima.MainReportActivity;
import mx.com.vialogika.dscdenunciaanonima.R;

public class Dialogs {

    public static void Permissions(Context context){
        new MaterialDialog.Builder(context)
                .title(R.string.reqPermTitle)
                .content(R.string.reqPermExpl)
                .positiveText(R.string.prompt_ok)
                .show();

    }

    public static void noNetworkDialog(Context context){
        new MaterialDialog.Builder(context)
                .title(R.string.networkerrortitle)
                .content(R.string.networkerror)
                .positiveText(R.string.prompt_ok)
                .show();
    }

    public static void ReportSaved(final Context context,final ReportCallbacks callbacks){
        new MaterialDialog.Builder(context)
                .title(R.string.savedReportTitle)
                .customView(R.layout.successdialog,true)
                .positiveText(R.string.prompt_ok)
                .cancelable(false)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                    callbacks.onReportSaved();
                    }
                })
                .show();
    }
}
