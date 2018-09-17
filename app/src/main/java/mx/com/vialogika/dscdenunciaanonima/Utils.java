package mx.com.vialogika.dscdenunciaanonima;

import android.content.Context;
import android.os.Environment;
import android.webkit.MimeTypeMap;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

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
