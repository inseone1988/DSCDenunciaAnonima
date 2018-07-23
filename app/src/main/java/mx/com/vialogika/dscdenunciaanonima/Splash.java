package mx.com.vialogika.dscdenunciaanonima;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.internal.MDButton;

public class Splash extends AppCompatActivity {
    private boolean SHOW_MESSAGE;
    MaterialDialog dialog;
    Button CaptureDABtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        init();
    }

    private void init(){
        hideActionBar();
        getItems();
        initMD();
        setStartListener();
    }

    private void hideActionBar(){
        ActionBar bar = getSupportActionBar();
        assert bar !=null;
        bar.hide();
    }

    private void getItems(){
        CaptureDABtn = findViewById(R.id.start);
    }

    private void initMD(){
        SHOW_MESSAGE = showMessage();
        if(SHOW_MESSAGE){
            materialDialogInit();
        }else{
            startMainActivity();
        }
    }

    private void materialDialogInit(){
        dialog =  new MaterialDialog.Builder(this)
                .title(R.string.welcome_title)
                .content(R.string.welcome_message)
                .positiveText(R.string.prompt_ok)
                .negativeText(R.string.prompt_cancel)
                .checkBoxPromptRes(R.string.dont_ask_again,true,null)
                .build();
        setDialogListeners(dialog);
    }

    private void setDialogListeners(MaterialDialog dialog){
        //Separated for readibility
        dialog.getBuilder()
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        rememberShowMessage(dialog.isPromptCheckBoxChecked());
                        startMainActivity();
                    }
                })
                .build();
    }

    private void setStartListener(){
        CaptureDABtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SHOW_MESSAGE){
                    dialog.show();
                }else{
                    startMainActivity();
                }
            }
        });
    }

    private boolean showMessage(){
        SharedPreferences sp = getPreferences(Context.MODE_PRIVATE);
        return sp.getBoolean("show_welcome_mssg",true);
    }

    private void startMainActivity(){
        requestPermission(new permissions() {
            @Override
            public void onPermissionsChecked() {
                start();
            }
        });
    }

    private void rememberShowMessage(Boolean value){
        SharedPreferences sp = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("show_welcome_mssg",value);
        editor.apply();
        SHOW_MESSAGE = value;
    }

    private void start(){
        Intent intent = new Intent(this,MainReportActivity.class);
        startActivity(intent);
    }


    private void requestPermission(permissions cb){
        int PERMISSION_ALL = 1;
        String[] permissions = new String[]{Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if(!hasPermissions(this,permissions)){
            ActivityCompat.requestPermissions(this,permissions,PERMISSION_ALL);
        }
        cb.onPermissionsChecked();
    }

    private boolean hasPermissions(Context context, String... permissions){
        for(String permission : permissions){
            if(ActivityCompat.checkSelfPermission(context,permission) != PackageManager.PERMISSION_GRANTED){
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    interface permissions{
        void onPermissionsChecked();
    }
}
