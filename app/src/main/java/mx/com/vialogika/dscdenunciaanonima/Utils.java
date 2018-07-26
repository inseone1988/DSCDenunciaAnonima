package mx.com.vialogika.dscdenunciaanonima;

import android.content.Context;
import android.os.Environment;
import android.webkit.MimeTypeMap;

import com.iceteck.silicompressorr.SiliCompressor;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

    public static boolean moveCacheFile(Context context, File cacheFile, final Evidences callbacks) {
        File outputMediaFile = new File(getOutputMediaDirectory(context));
        boolean ret = false;
        FileInputStream fis = null;
        FileOutputStream fos = null;
        String filetype = mediaType(cacheFile.getAbsolutePath());
        String finalDest = null;
        switch(filetype){
            case "image":
                finalDest = processImage(context,cacheFile.getAbsolutePath(),outputMediaFile);
                callbacks.onEvidenceSaved(finalDest);
                break;
            case "video":
                processVideo(context, cacheFile.getAbsolutePath(), getOutputMediaDirectory(context), new Video() {
                    @Override
                    public void onVideoCompressed(String filepath) {
                        callbacks.onVideoSaved(filepath);
                    }
                });
                callbacks.onEvidenceSaved(finalDest);
                break;
            default :
                try {
                    fis = new FileInputStream(cacheFile);

                    ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
                    byte[] buffer = new byte[1024];
                    int read = -1;
                    while( (read = fis.read(buffer) ) != -1 ) {
                        baos.write(buffer, 0, read);
                    }
                    baos.close();
                    fis.close();
                    fos = new FileOutputStream(outputMediaFile);
                    baos.writeTo(fos);
                    fos.close();

                    // delete cache
                    cacheFile.delete();
                    callbacks.onEvidenceSaved(finalDest);
                    ret = true;
                }
                catch(Exception e) {
                    //Log.e(TAG, "Error saving previous rates!");
                }
                finally {
                    try { if ( fis != null ) fis.close(); } catch (IOException e) { }
                    try { if ( fos != null ) fos.close(); } catch (IOException e) { }
                }
                break;
        }

        return ret;
    }

    public static String getFormatFilename(){
        String prefix = "DSC_";
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String formatted = dateTimeFormat.format(new Date());
        return prefix + formatted;
    }

    public static String getFileType(String uri){
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(uri);
        if(extension != null){
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }

    public static String getFileExtension(String uri){
        return MimeTypeMap.getFileExtensionFromUrl(uri);
    }

    public static String getOutputMediaDirectory(Context context){
        return Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + context.getPackageName()
                + "/Files";
    }

    public static  File getOutputMediaFile(Context context,String uri){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        String filepath = "";
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + context.getPackageName()
                + "/Files");

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date());
        File mediaFile;
        String mImageName="DSC_"+ timeStamp + "." + getFileExtension(uri);
        filepath = mediaStorageDir.getPath() + File.separator + mImageName;
        mediaFile = new File(filepath);
        return mediaFile;
    }

    public static String processImage(Context context,String cachefile,File imgdst){
        return SiliCompressor.with(context).compress(cachefile,imgdst);
    }

    public static String processVideo(Context context, String cacheFile, String destination, final Video callback){
        final String path = null;
        try{
            new VideoCompressAsyncTask(context, new Video() {
                @Override
                public void onVideoCompressed(String filepath) {
                    callback.onVideoCompressed(filepath);
                }
            }).execute(cacheFile,destination);
        }catch(Exception e){
            e.printStackTrace();
        }
        return path;
    }

    public static void processOther(){

    }

    public static String mediaType(String uri){
        String mediaType = "";
        String extension = getFileExtension(uri);
        switch(extension){
            case "jpg":
                mediaType = "image";
                break;
            case "jpeg":
                mediaType = "image";
                break;
            case "png":
                mediaType = "image";
                break;
            case "bmp":
                mediaType = "image";
                break;
            case "pdf":
                mediaType = "document";
                break;
            case "doc":
                mediaType = "document";
                break;
            case "docx":
                mediaType = "document";
                break;
            case "mp4":
                mediaType = "video";
                break;
            default:
                mediaType = "Not supported";
                break;
        }
        return mediaType;
    }

    interface Evidences{
        void onEvidenceSaved(String mediaPath);
        void onVideoSaved(String meldiaPath);
    }

    interface Video{
        void onVideoCompressed(String filepath);
    }
}
