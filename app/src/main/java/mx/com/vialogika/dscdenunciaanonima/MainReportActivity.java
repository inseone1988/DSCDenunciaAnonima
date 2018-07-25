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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;



import java.io.File;
import java.util.ArrayList;
import java.util.List;

import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;
import droidninja.filepicker.utils.Orientation;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;

public class MainReportActivity extends AppCompatActivity {

    private ArrayList<String> mPaths = new ArrayList<>();


    private List<String> paths = new ArrayList<>();
   // private CardView evidencesChooser;
    private RecyclerView mRecyclerview;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private CardView noData;
    private ImageView photoChooser;
    private ImageView documentsChooser;

    private LinearLayout reportResume;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_report);
        setupActionBar();
        getItems();
        init();
        setListeners();
        setCardsVisibility();
    }

    private void init(){

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
    }

    private void setCardsVisibility(){
        if (paths.size() < 1){
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
    }

    private void setupActionBar(){
        ActionBar toolbar = getSupportActionBar();
        toolbar.setTitle(R.string.appBarTitle);

    }

    private void imageChooser(){
        FilePickerBuilder.getInstance()
                .setSelectedFiles(mPaths)
                .pickPhoto(this);
    }

    private void documentChooser(){
        FilePickerBuilder.getInstance()
                .setSelectedFiles(mPaths)
                .pickFile(this);
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
        }
        mAdapter.notifyDataSetChanged();
        setCardsVisibility();
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
