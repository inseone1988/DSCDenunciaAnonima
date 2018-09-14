package mx.com.vialogika.dscdenunciaanonima;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import mx.com.vialogika.dscdenunciaanonima.Util.DatePickerActions;
import mx.com.vialogika.dscdenunciaanonima.Util.DatePickerFragment;
import mx.com.vialogika.dscdenunciaanonima.Util.Now;
import mx.com.vialogika.dscdenunciaanonima.Util.TimePickerActions;
import mx.com.vialogika.dscdenunciaanonima.Util.TimePickerFragment;

public class EditReport extends AppCompatActivity {

    private String today = Now.getDate();
    private String hour = Now.getHour();
    private String date;
    private String[] values;

    private EditText mName,MPosition,mSite,mClient,mEventDate,mEvenTime,mWhat,mWhere,mWHow,mExp;
    private ImageView mSelectDate,mSelectTime;
    private Button btn;
    private ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_report);
        init();
        getItems();
        displayCurrentTimes();
        setListeners();

    }

    //Do nothing on back pressed


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void init(){
        getSupportActionBar().hide();
    }

    private void setDate(String Date){
        date = Date;
        mEventDate.setText(date);
    }

    private void setTime(String Time){
        String time = Time;
        mEvenTime.setText(time);
    }

    private void getItems(){
        scrollView = findViewById(R.id.sv1);
        mName = findViewById(R.id.name);
        MPosition = findViewById(R.id.position);
        mSite = findViewById(R.id.site);
        mClient = findViewById(R.id.client);
        mWhat = findViewById(R.id.eventwhat);
        mWhere = findViewById(R.id.eventwhere);
        mWHow = findViewById(R.id.eventhow);
        mExp = findViewById(R.id.eventfacts);
        mEventDate = findViewById(R.id.eventdate);
        mEvenTime = findViewById(R.id.eventtime);
        mSelectDate = findViewById(R.id.selectdate);
        mSelectTime = findViewById(R.id.selecttime);
        btn = findViewById(R.id.savereport);
    }

    private void displayCurrentTimes(){
        mEventDate.setText(today);
        mEvenTime.setText(hour);
    }

    private String getSelectedDate(int year,int month,int day){
        Calendar c = Calendar.getInstance();
        c.set(year,month,day);
        Date eDate = new Date(c.getTimeInMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(eDate);
    }

    private String getSelectedTime(int hour,int minutes){
        StringBuilder sb = new StringBuilder();
        sb.append(hour);
        sb.append(":");
        sb.append(minutes);
        return sb.toString();
    }

    private void setListeners(){
        mSelectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerFragment dpf = new DatePickerFragment();
                dpf.setCallback(new DatePickerActions() {
                    @Override
                    public void onDateSelected(int year, int month, int day) {
                    String date = getSelectedDate(year,month,day);
                    setDate(date);
                    }
                });
                dpf.show(getFragmentManager(),"DateSelect");
            }
        });

        mSelectTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerFragment tpf = new TimePickerFragment();
                tpf.setCallback(new TimePickerActions() {
                    @Override
                    public void onTimeSelected(int hour, int minutes) {
                        String SelTime = getSelectedTime(hour,minutes);
                        setTime(SelTime);
                    }
                });
                tpf.show(getFragmentManager(),"TimeSelect");
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAndContinue();
            }
        });
    }

    private void notValidInputToast(){
        Toast.makeText(getApplication(),R.string.notempty,Toast.LENGTH_SHORT).show();
    }

    private boolean checkValidInputs(){
        EditText[] inputs = {mName,MPosition,mSite,mClient,mEventDate,mEvenTime,mWhat,mWhere,mWHow,mExp};
        values = new String[inputs.length];
        for (int i = 0 ;i < inputs.length; i++){
            String inputValue = inputs[i].getText().toString();
            if(inputValue.equals("")){
                int[] loc = new int[2];
                inputs[i].getLocationOnScreen(loc);
                scrollView.smoothScrollTo(loc[0],loc[1]);
                inputs[i].requestFocus();
                notValidInputToast();
                return false;
            }else{
                values[i] = inputValue;
            }
        }
        return true;
    }

    private void checkAndContinue(){
        if (checkValidInputs()){
            Object[] strings = values;
            Intent response = new Intent();
            response.putExtra("reportData",strings);
            setResult(1050,response);
            finish();
        }
    }

    interface onReportEdited{
        void getValues(String[] values);
    }
}
