package com.orbital.lead.logic.LocalStorage;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.orbital.lead.logic.CustomLogging;
import com.orbital.lead.model.Constant;
import com.orbital.lead.model.EnumPictureType;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by joseph on 20/6/2015.
 */
public class LocalStorage {

    private static LocalStorage mStorage = new LocalStorage();
    private final String TAG = this.getClass().getSimpleName();

    private final File rootDir = Environment.getExternalStorageDirectory();
    private CustomLogging mLogging;

    private LocalStorage(){}

    public static LocalStorage getInstance(){
        mStorage.initLogging();
        return mStorage;
    }


    public boolean isProfilePictureExist(String fileName){
        String path = this.getProfilePictureDirectory() + fileName;
        return this.isFileExist(path);
    }

    public boolean isPictureExist(String fileName){
        String path = this.getPictureDirectory() + fileName;
        return this.isFileExist(path);
    }

    public boolean saveProfilePicture(String fileName, Bitmap bmp){
        return this.saveImage(this.getProfilePictureDirectory() + fileName, bmp);
    }

    public boolean savePicture(String fileName, Bitmap bmp){
        return this.saveImage(this.getPictureDirectory() + fileName, bmp);
    }

    public Bitmap loadProfilePicture(String fileName){
        return this.loadImage(this.getProfilePictureDirectory() + fileName);
    }

    public Bitmap loadPicture(String fileName){
        return this.loadImage(this.getPictureDirectory() + fileName);
    }

    public String getProfilePictureDirectory(){
        return rootDir + Constant.STORAGE_APPLICATION_PROFILE_FOLDER_PATH;
    }

    public String getPictureDirectory(){
        return rootDir + Constant.STORAGE_APPLICATION_PICTURE_FOLDER_PATH;
    }

    public void checkUpdateFolderExist(){
        this.checkProfileFolder();
        this.checkPictureFolder();
    }

    private void initLogging(){
        this.mLogging = CustomLogging.getInstance();
    }

    /**
     *  @param filePath File path without the root directory (external storage directory)
     * **/
    private boolean isFileExist(String filePath){
        File mFile = new File(filePath);
        return mFile.exists();
    }

    private EnumPictureType getFileType(String fileName){
        String ext = this.getFileExtension(fileName);
        this.mLogging.debug(TAG, "getFileType ext => " + ext);
        switch (ext){
            case "png":
                return EnumPictureType.PNG;
            case "jpeg":
                return EnumPictureType.JPEG;
            case "jpg":
                return EnumPictureType.JPEG;
            default:
                return EnumPictureType.NONE;
        }
    }

    private String getFileExtension(String fileName) {
        String extension = "";
        try {
            int i = fileName.lastIndexOf('.');
            if (i >= 0) {
                extension = fileName.substring(i+1).trim().toLowerCase();
            }

            return extension;
        }catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private boolean saveImage(String fullFilePath, Bitmap bmp){
        try {
            this.mLogging.debug(TAG, "saveImage fullFilePath => " + fullFilePath);
            FileOutputStream out = new FileOutputStream(
                    fullFilePath);

            Bitmap.CompressFormat format = null;
            EnumPictureType type = this.getFileType(fullFilePath);
            switch (type){
                case JPEG:
                    format = Bitmap.CompressFormat.JPEG;
                    break;
                case PNG:
                    format = Bitmap.CompressFormat.PNG;
                    break;
                case NONE:
                    format = null;
                    break;
            }

            if(format != null){
                bmp.compress(format, 100, out);
                out.flush();
                out.close();
                this.mLogging.debug(TAG, "saveImage => file saved.");
                return true;
            }else{
                this.mLogging.debug(TAG, "saveImage => file format not found.");
                return false;
            }

        } catch(Exception e) {
            e.printStackTrace();
            this.mLogging.debug(TAG, "saveImage Error => " + e.getMessage());
            return false;
        }
    }

    private Bitmap loadImage(String fullFilePath){
        try {
            File f = new File(fullFilePath);
            if (!this.isFileExist(fullFilePath)) { return null; }
            Bitmap tmp = BitmapFactory.decodeFile(fullFilePath);
            this.mLogging.debug(TAG, "loadImage => Image loaded");
            return tmp;
        } catch (Exception e) {
            e.printStackTrace();
            this.mLogging.debug(TAG, "loadImage Error => " + e.getMessage());
            return null;
        }
    }

    private void checkProfileFolder(){
        File f1 = new File(this.rootDir + Constant.STORAGE_APPLICATION_PROFILE_FOLDER_PATH);
        if(!f1.exists()){
            this.mLogging.debug(TAG, "Profile folder not exist");
            this.createDirectory(f1);
        }
    }

    private void checkPictureFolder(){
        File f1 = new File(this.rootDir + Constant.STORAGE_APPLICATION_PICTURE_FOLDER_PATH);
        if(!f1.exists()){
            this.mLogging.debug(TAG, "Picture folder not exist");
            this.createDirectory(f1);
        }
    }

    private void createDirectory(File f){
        this.mLogging.debug(TAG, "Create directory => " + f.getPath());
        f.mkdirs();
    }


/*
    private enum FileType{
        PNG ("png"),
        JPEG ("jpg"),
        NONE ("");

        private final String ext;
        private FileType(String s){
            ext = s;
        }

        @Override
        public String toString(){
            return ext;
        }
    }
*/




}
