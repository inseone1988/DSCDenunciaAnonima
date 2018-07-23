package mx.com.vialogika.dscdenunciaanonima;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;

public class MainReportActivity extends AppCompatActivity {

    private List<String> paths = new ArrayList<>();
    private List<File> files;

    private CardView evidencesChooser;
    private RecyclerView mRecyclerview;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private CardView noData;
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
        files = new ArrayList<>();
    }

    private void getItems(){
        evidencesChooser = findViewById(R.id.choose_evidences);
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
        if (files.size() < 1){
            noData.setVisibility(View.VISIBLE);
            reportResume.setVisibility(View.GONE);
        }else{
            noData.setVisibility(View.GONE);
            reportResume.setVisibility(View.VISIBLE);
        }
    }

    private void deleteEvidence(int position){
        paths.remove(position);
        files.remove(position);
        mRecyclerview.removeViewAt(position);
        mAdapter.notifyItemRemoved(position);
        mAdapter.notifyItemRangeChanged(position,paths.size());
    }

    private void setListeners(){
        evidencesChooser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooser();
            }
        });
    }

    private void setupActionBar(){
        ActionBar toolbar = getSupportActionBar();
        toolbar.setTitle(R.string.appBarTitle);

    }

    private void chooser(){
        EasyImage.configuration(this)
                .setImagesFolderName("evidences");
        EasyImage.openChooserWithDocuments(this,"Elegir archivo",1);
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
            @Override
            public void onImagesPicked(@NonNull List<File> list, EasyImage.ImageSource imageSource, int i) {
                //Handle documents as byte[]
                files.addAll(list);
                for(int v = 0;v<list.size();v++){
                    String path = list.get(v).getAbsolutePath();
                    paths.add(path);
                    mAdapter.notifyDataSetChanged();
                    setCardsVisibility();
                }
            }
        });
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
            Bitmap image = null;
            File imgFile = new File(path);
            if (imgFile.exists()){
                image = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            }
            return image;
        }
    }
    interface ViewAdapter{
        void onCloseClick(int position);
    }
}
