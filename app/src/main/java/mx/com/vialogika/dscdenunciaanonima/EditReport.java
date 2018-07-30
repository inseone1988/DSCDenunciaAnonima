package mx.com.vialogika.dscdenunciaanonima;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

public class EditReport extends AppCompatActivity {

    private EditText mName,MPosition,mSite,mClient,mEventDateTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_report);
        init();
        getItems();
    }

    private void init(){
        getSupportActionBar().hide();
    }

    private void getItems(){

    }

    private void setListeners(){

    }
}
