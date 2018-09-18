package mx.com.vialogika.dscdenunciaanonima;

import android.Manifest;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;


import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadService;

import java.io.File;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;
import droidninja.filepicker.utils.Orientation;
import mx.com.vialogika.dscdenunciaanonima.Util.Dialogs;
import mx.com.vialogika.dscdenunciaanonima.Util.NetworkRequest;
import mx.com.vialogika.dscdenunciaanonima.Util.Now;
import mx.com.vialogika.dscdenunciaanonima.Util.Permissions;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;

public class MainReportActivity extends AppCompatActivity {

    private ArrayList<String> mPaths = new ArrayList<>();
    private Report cReport;
    private Resources res;

    private List<String> paths = new ArrayList<>();
    private CardView editReport;
    private RecyclerView mRecyclerview;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private CardView noData;
    private ImageView photoChooser;
    private ImageView documentsChooser;
    private TextView date;
    private TextView subject;
    private TextView resume;
    private Button sendReport;

    private LinearLayout reportResume;

    private RequestQueue rq;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_report);
        setupActionBar();
        getItems();
        res = getResources();
        init();
        setListeners();
        setCardsVisibility();
    }

    private void init(){
        initUploadService();
        setResumeValues();
        rq = Volley.newRequestQueue(this);
    }

    private void initUploadService(){
        UploadService.NAMESPACE = BuildConfig.APPLICATION_ID;
    }

    private void setResumeValues(){
        if(cReport == null){
            date.setText(String.format(res.getString(R.string.date),res.getString(R.string.waiting_edition)));
            subject.setText(String.format(res.getString(R.string.subject),res.getString(R.string.waiting_edition)));
            resume.setText(String.format(res.getString(R.string.resume),res.getString(R.string.waiting_edition)));
        }else{
            date.setText(String.format(res.getString(R.string.date),cReport.getDateTime()));
            subject.setText(String.format(res.getString(R.string.subject),cReport.getSubject()));
            resume.setText(String.format(res.getString(R.string.resume),truncateDescription()));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode){
            case 1051:
                if (grantResults[0] == PackageManager.PERMISSION_DENIED){
                    showPermissionsDialog();
                }
                break;
        }
    }

    private void showPermissionsDialog(){
        Dialogs.Permissions(this);
    }

    private String truncateDescription(){
        String truncated = null;
        int maxChar = 10;
        int curChar = cReport.getSubject().length();
        if(curChar > maxChar){
            truncated = cReport.getSubject().substring(0,maxChar) + "...";
        }else{
            truncated = cReport.getSubject();
        }
        return truncated;
    }

    private void editReport(){
        if(cReport == null){
            cReport = new Report();
        }
        int REQUESTCODE = 1050;
        Intent intent = new Intent(this,EditReport.class);
        putReportCurrentData(intent);
        startActivityForResult(intent,REQUESTCODE);
    }

    private void putReportCurrentData(Intent intent){
        if(cReport != null){
            String[] cvalues = new String[]{cReport.getName(),cReport.getPosition(),cReport.getSite(),cReport.getClient(),cReport.getDate(),cReport.getTime(), cReport.getWhat(), cReport.getWhere(),cReport.getHow(),cReport.getDescription()};
            intent.putExtra("reportValues",cvalues);
        }
    }


    private void getItems(){
        photoChooser = findViewById(R.id.photo_chooser);
        documentsChooser = findViewById(R.id.document_chooser);
        mRecyclerview = findViewById(R.id.evidences_recycle);
        mAdapter = new EvidenceAdapter(paths, new ViewAdapter() {
            @Override
            public void onCloseClick(int position) {
               deleteEvidence(position);
            }
        });
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerview.setLayoutManager(mLayoutManager);
        mRecyclerview.setAdapter(mAdapter);
        noData = findViewById(R.id.no_items_card);
        reportResume = findViewById(R.id.evidence_resume);
        date = findViewById(R.id.report_date);
        subject = findViewById(R.id.report_subject);
        resume = findViewById(R.id.report_resume);
        editReport = findViewById(R.id.reporte_nuvo);
        sendReport = findViewById(R.id.sendReport);
    }

    private boolean mustShowCard(){
        if(paths.size() > 0 || cReport != null){
            return true;
        }
        return false;
    }

    private void setCardsVisibility(){
        if (!mustShowCard()){
            noData.setVisibility(View.VISIBLE);
            reportResume.setVisibility(View.GONE);
        }else{
            noData.setVisibility(View.GONE);
            reportResume.setVisibility(View.VISIBLE);
        }
    }

    private void deleteEvidence(int position){
        paths.remove(position);
        mRecyclerview.removeViewAt(position);
        mAdapter.notifyItemRemoved(position);
        mAdapter.notifyItemRangeChanged(position,paths.size());
    }

    private void requestPermissions(){
        int REQUESTCODE = 1050;
        String pCamera = Manifest.permission.CAMERA;
        String pStorage = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        Permissions.requestPermission(this,new String[]{pCamera,pStorage},REQUESTCODE);
    }

    private void setListeners(){
        documentsChooser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    documentChooser();
            }
        });
        photoChooser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    imageChooser();
            }
        });
        editReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editReport();
            }
        });
        sendReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadFiles();
            }
        });
    }

    private void setupActionBar(){
        ActionBar toolbar = getSupportActionBar();
        toolbar.setTitle(R.string.appBarTitle);

    }

    private void uploadFiles(){
        try{
            String uploadId = UUID.randomUUID().toString();
            String identifier = String.valueOf(System.currentTimeMillis() / 1000L);
            String url = "https://www.vialogika.com.mx/dscic/requesthandler.php";
            if(paths.size() > 0){
                for (int i = 0;i < paths.size();i++){
                MultipartUploadRequest ur = new MultipartUploadRequest(this,url)
                        .addParameter("function","saveDEEvidence")
                        .setNotificationConfig(NetworkRequest.defaultNotificationConfig())
                        .setMaxRetries(3);
                    ur.addFileToUpload(paths.get(i),"file" + identifier);
                    ur.startUpload();
                }
            }else{
                Toast.makeText(this,R.string.nofilestoupload,Toast.LENGTH_SHORT);
            }
        }catch(Exception e ){
            e.printStackTrace();
        }

    }

    private void imageChooser(){
        boolean camerapermission = Permissions.hasPermission(this,Manifest.permission.CAMERA);
        if(camerapermission){
            FilePickerBuilder.getInstance()
                    .setSelectedFiles(mPaths)
                    .pickPhoto(this);
        }else{
            requestPermissions();
        }
    }

    private void documentChooser(){
        boolean storagepermission = Permissions.hasPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(storagepermission){
            FilePickerBuilder.getInstance()
                    .setSelectedFiles(mPaths)
                    .pickFile(this);
        }else{
           requestPermissions();
        }
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        switch(requestCode){
            case FilePickerConst.REQUEST_CODE_PHOTO:
                if(resultCode== AppCompatActivity.RESULT_OK && data != null){
                    paths.addAll(data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA));
                }
                break;
            case FilePickerConst.REQUEST_CODE_DOC:
                 if(resultCode== AppCompatActivity.RESULT_OK && data != null){
                     paths.addAll(data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_DOCS));
                 }
                 break;
            case 1050:
                if(data != null){
                    String[] values = (String[]) data.getSerializableExtra("reportData");
                    mapResultToObject(values);
                }
                break;
        }
        mAdapter.notifyDataSetChanged();
        setCardsVisibility();
    }

    private void mapResultToObject(String[] values){
        cReport.setName(values[0]);
        cReport.setPosition(values[1]);
        cReport.setSite(values[2]);
        cReport.setClient(values[3]);
        cReport.setDate(values[4]);
        cReport.setTime(values[5]);
        cReport.setDateTime(Now.getCurrentDateTime());
        cReport.setSubject(values[6]);
        cReport.setWhat(values[6]);
        cReport.setWhere(values[7]);
        cReport.setHow(values[8]);
        cReport.setDescription(values[9]);
        setResumeValues();
    }

    private void unknownFileFormat(){
        new MaterialDialog.Builder(this)
                .title(R.string.advice)
                .content(R.string.unknown_file_advice)
                .positiveText(R.string.prompt_ok)
                .negativeText(R.string.prompt_cancel)
                .show();
    }

    private class EvidenceAdapter extends RecyclerView.Adapter<EvidenceAdapter.EvidenceViewHolder>{

        private List<String> mDataset;
        private ViewAdapter cb;

        public EvidenceAdapter(List<String> dataset,ViewAdapter callbacks){
            this.mDataset = dataset;
            this.cb = callbacks;
        }

        public class EvidenceViewHolder extends RecyclerView.ViewHolder{
            public CardView eventEvidenceContainer;
            public TextView imagePath;
            public ImageView evidenceThumb;
            public ImageView closeButton;

            public EvidenceViewHolder(View v){
                super(v);
                this.eventEvidenceContainer = v.findViewById(R.id.evidence_view);
                this.imagePath = v.findViewById(R.id.image_path);
                this.evidenceThumb = v.findViewById(R.id.image_thumb);
                this.closeButton = v.findViewById(R.id.close);
            }
        }

        @NonNull
        @Override
        public EvidenceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.evidence_view,parent,false);
            return new EvidenceViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull EvidenceViewHolder holder, int position) {
           final int cPosition = position;
            holder.evidenceThumb.setImageBitmap(image(mDataset.get(position)));
            holder.imagePath.setText(mDataset.get(position));
            holder.closeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cb.onCloseClick(cPosition);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mDataset.size();
        }

        private Bitmap image(String path){
            String fileType = Utils.mediaType(path);
            return setThumbnail(path,fileType);
        }
    }

    private Bitmap setThumbnail(String path,String extension){
        Resources res = getResources();
        Bitmap image = null;
        File thumb = new File(path);
        if (thumb.exists()){
            switch(extension){
                case "video":
                    image = BitmapFactory.decodeResource(res,R.drawable.mp4);
                    break;
                case "image":
                    image = BitmapFactory.decodeFile(path);
                    image = ThumbnailUtils.extractThumbnail(image,100,100);
                    break;
                case "document":
                    if(Utils.getFileExtension(path).equals("pdf")){
                        image = BitmapFactory.decodeResource(res,R.drawable.pdf);
                    }else if(Utils.getFileExtension(path).equals("word")){
                        image = BitmapFactory.decodeResource(res,R.drawable.word);
                    }
                    break;
                default:
                    image = BitmapFactory.decodeResource(res,R.drawable.not_available_circle);
                    unknownFileFormat();
                    break;
            }
        }
        return image;
    }
    interface ViewAdapter{
        void onCloseClick(int position);
    }
}
