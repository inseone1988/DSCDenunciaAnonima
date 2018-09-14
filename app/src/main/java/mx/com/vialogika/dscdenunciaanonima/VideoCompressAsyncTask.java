package mx.com.vialogika.dscdenunciaanonima;

import android.content.Context;
import android.os.AsyncTask;

import com.iceteck.silicompressorr.SiliCompressor;

import java.net.URISyntaxException;

public class VideoCompressAsyncTask extends AsyncTask<String,String,String> {

    private Context mContext;
    Utils.Video cb;

    public VideoCompressAsyncTask(Context context, Utils.Video callback){
        mContext = context;
        cb = callback;
    }

    @Override
    protected String doInBackground(String... paths) {
        String filepath = null;
        try{
            filepath = SiliCompressor.with(mContext).compressVideo(paths[0],paths[1]);
        }catch(URISyntaxException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String compressedFilePath) {
        super.onPostExecute(compressedFilePath);
        cb.onVideoCompressed(compressedFilePath);
    }
}
