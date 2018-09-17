package mx.com.vialogika.dscdenunciaanonima.Util;

import android.content.Context;

import com.afollestad.materialdialogs.MaterialDialog;

import mx.com.vialogika.dscdenunciaanonima.R;

public class Dialogs {

    public static void Permissions(Context context){
        new MaterialDialog.Builder(context)
                .title(R.string.reqPermTitle)
                .content(R.string.reqPermExpl)
                .positiveText(R.string.prompt_ok)
                .show();

    }
}
