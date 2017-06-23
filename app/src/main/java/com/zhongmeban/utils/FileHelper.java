package com.zhongmeban.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;
import com.orhanobut.logger.Logger;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 定义文件存储路径; 创建、删除、判断文件等；
 * 根据时间来判断文件是否该删除
 *
 * @author Forezp
 *         2015-9-11-上午10:06:45
 */
public class FileHelper {
    private boolean hasSD = false;
    private Context context;
    private String SDPATH;
    private String rootPath;//app文件存储根目录
    private String cachePath;//app 文件存储媒体类
    private String cacheDisk;//app 文件存储媒体类

    private static final String ZMBFileDir = "YiZhi";
    private static final String CacheFileAttention = "Attention";
    private static final String CacheFile = "cacheYZ";
    private static final String CacheDiskFile = "cacheDisk";
    private static FileHelper instance;


    public FileHelper() {
        hasSD = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        SDPATH = Environment.getExternalStorageDirectory().getPath();
        if (hasSD) {
            rootPath = SDPATH + File.separator + ZMBFileDir + File.separator + ZMBFileDir +
                File.separator;
            cachePath = SDPATH + File.separator + ZMBFileDir + File.separator + CacheFile +
                File.separator;
            cacheDisk = SDPATH + File.separator + ZMBFileDir + File.separator + CacheFile +
                File.separator + CacheDiskFile + File.separator;

        } else {
            SDPATH = Environment.getRootDirectory().getPath();
            rootPath = SDPATH + File.separator + ZMBFileDir + File.separator + ZMBFileDir +
                File.separator;
            cachePath = SDPATH + File.separator + ZMBFileDir + File.separator + CacheFile +
                File.separator;
            cacheDisk = SDPATH + File.separator + ZMBFileDir + File.separator + CacheFile +
                File.separator + CacheDiskFile + File.separator;
        }
        hasFileDir(cachePath);
        hasFileDir(rootPath);
        hasFileDir(cacheDisk);
        Logger.d(hasSD + "--文件目录--" + cachePath);
    }


    //没有私有化构造函数，单例。
    public static FileHelper getInstance() {
        if (instance == null) {
            instance = new FileHelper();
        }
        return instance;
    }


    /**
     * @param bm 要保存的bitmap
     * @param picName 保存文件的名字；默认存储在mediapath路径下。
     */
    public void saveBitmap(Bitmap bm, String picName) {
        Log.e("", "保存图片");
        try {
            File f = new File(cachePath, picName + ".JPEG");
            if (f.exists()) {
                f.delete();
            }
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            Log.e("", "已经保存");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //创建文件
    public File createFile(String path) {
        File file = new File(path);
        try {
            if (file.exists()) {
                deleteFile(file);
            }
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }


    //判断mediapath下的文件是否存在
    public boolean isFileExist(String fileName) {
        File file = new File(cachePath + fileName);
        file.isFile();
        return file.exists();
    }


    //删除File
    public static void deleteFile(File f) {
        if (f.isDirectory()) {
            File[] files = f.listFiles();
            if (files != null && files.length > 0) {
                for (int i = 0; i < files.length; ++i) {
                    deleteFile(files[i]);
                }
            }
        }
        f.delete();
    }


    //删除文件
    public void delFile(String fileName) {
        File file = new File(cachePath + fileName);
        if (file.isFile()) {
            file.delete();
        }
    }


    /**
     * 递归删除文件和文件夹
     *
     * @param file 要删除的根目录
     */
    public void DeleteFile(File file) {
        if (file.exists() == false) {
            return;
        } else {
            if (file.isFile()) {
                file.delete();
                return;
            }
            if (file.isDirectory()) {
                File[] childFile = file.listFiles();
                if (childFile == null || childFile.length == 0) {
                    //file.delete();
                    return;
                }
                for (File f : childFile) {
                    DeleteFile(f);
                }
                //file.delete();
            }
        }
    }


    //删除文件夹
    public boolean deleteDir(String path) {
        File dir = new File(path);
        if (dir == null || !dir.exists() || !dir.isDirectory()) {
            return false;
        }
        for (File file : dir.listFiles()) {
            if (file.isFile()) {
                file.delete(); // 删除所有文件
            } else if (file.isDirectory()) {
                deleteDir(path); // 递规的方式删除文件夹
            }
        }
        return dir.delete();// 删除目录本身
    }


    //判断文件是否存在
    public boolean fileIsExists(String path) {
        try {
            File f = new File(path);
            if (!f.exists()) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }


    /**
     * 删除多少天前的文件
     *
     * @param path 文件路径
     * @param day 多少天的前的文件需要删除
     */
    public void DeleteFileInDirectoryWithBeforeDays(String path, int day) {
        File file = new File(path);
        if (file.isFile()) {
            if (isNeedDelete(file, day)) {
                file.delete();
            }
            return;
        }
        if (file.isDirectory()) {
            File[] childFile = file.listFiles();
            for (File f : childFile) {
                DeleteFileInDirectoryWithBeforeDays(f.getPath(), day);
            }
            if (isNeedDelete(file, day)) {
                file.delete();
            }
        }
    }


    private static boolean isNeedDelete(File file, int day) {
        long time = file.lastModified();
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        Date fileDate = cal.getTime();
        Date nowDate = new Date();
        long tf = fileDate.getTime();
        long tn = nowDate.getTime();
        long millis = tn - tf;
        int offset = (int) (millis / (1000 * 60 * 60 * 24));
        if (offset >= day) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * 合并路径
     */
    public String combinPath(String path, String fileName) {
        return path + (path.endsWith(File.separator) ? "" : File.separator) + fileName;
    }


    /**
     * 复制文件
     *
     * @throws Exception
     */
    public boolean copyFile(File src, File tar) throws Exception {
        if (src.isFile()) {
            InputStream is = new FileInputStream(src);
            OutputStream op = new FileOutputStream(tar);
            BufferedInputStream bis = new BufferedInputStream(is);
            BufferedOutputStream bos = new BufferedOutputStream(op);
            byte[] bt = new byte[1024 * 8];
            int len = bis.read(bt);
            while (len != -1) {
                bos.write(bt, 0, len);
                len = bis.read(bt);
            }
            bis.close();
            bos.close();
        }
        if (src.isDirectory()) {
            File[] f = src.listFiles();
            tar.mkdir();
            for (int i = 0; i < f.length; i++) {
                copyFile(f[i].getAbsoluteFile(),
                    new File(tar.getAbsoluteFile() + File.separator + f[i].getName()));
            }
        }
        return true;
    }


    /**
     * 移动文件
     *
     * @throws Exception
     */
    public boolean moveFile(File src, File tar) throws Exception {
        if (copyFile(src, tar)) {
            deleteFile(src);
            return true;
        }
        return false;
    }


    /**
     * 获取最后的‘/’后的文件名
     */
    public String getLastName(String name) {
        int lastIndexOf = 0;
        try {
            lastIndexOf = name.lastIndexOf('/');
        } catch (Exception e) {
            e.printStackTrace();
        }
        return !name.equals("") ? name.substring(lastIndexOf + 1) : "";
    }


    /**
     * @param url 保存文件的文字
     * @return 文件名
     */
    public static String getFileName(String url) {
        String fileName = null;
        if (url != null && url.contains("/")) {
            String[] data = url.split("/");
            fileName = data[data.length - 1];
        }
        return fileName;
    }


    /**
     * 判断文件夹是否存在,不存在就创建
     *
     * @return true
     */
    public boolean hasFileDir(String path) {
        File file = new File(path);
        if (file.exists()) {
            if (!file.isDirectory()) {
                boolean delete = file.delete();
            }
        }

        if (!file.exists()) {
            file.mkdirs();
        }
        return file.exists();
    }


    public String getSDPATH() {
        return SDPATH;
    }


    public String getRootPath() {
        return rootPath;
    }


    public boolean isHasSD() {
        return hasSD;
    }


    public String getCachePath() {
        return cachePath;
    }


    public String getCacheDisk() {
        return cacheDisk;
    }


    public static final int SIZETYPE_B = 1;//获取文件大小单位为B的double值
    public static final int SIZETYPE_KB = 2;//获取文件大小单位为KB的double值
    public static final int SIZETYPE_MB = 3;//获取文件大小单位为MB的double值
    public static final int SIZETYPE_GB = 4;//获取文件大小单位为GB的double值


    /**
     * 获取文件指定文件的指定单位的大小
     *
     * @param filePath 文件路径
     * @param sizeType 获取大小的类型1为B、2为KB、3为MB、4为GB
     * @return double值的大小
     */
    public double getFileOrFilesSize(String filePath, int sizeType) {
        File file = new File(filePath);
        long blockSize = 0;
        try {
            if (file.isDirectory()) {
                blockSize = getFileSizes(file);
            } else {
                blockSize = getFileSize(file);
            }
        } catch (Exception e) {
            e.printStackTrace();

            Logger.e("获取文件大小", "获取失败!" + e.toString());
            return -1;
        }
        return FormetFileSize(blockSize, sizeType);
    }


    /**
     * 获取指定文件大小
     *
     * @throws Exception
     */
    private static long getFileSize(File file) throws Exception {
        long size = 0;
        if (file.exists()) {
            size = file.length();
        } else {
            file.createNewFile();
            Log.e("获取文件大小", "文件不存在!");
        }
        return size;
    }


    /**
     * 获取指定文件夹
     *
     * @throws Exception
     */
    private static long getFileSizes(File f) throws Exception {
        long size = 0;
        File flist[] = f.listFiles();
        for (int i = 0; i < flist.length; i++) {
            if (flist[i].isDirectory()) {
                size = size + getFileSizes(flist[i]);
            } else {
                size = size + getFileSize(flist[i]);
            }
        }
        return size;
    }


    /**
     * 转换文件大小
     */
    private static String FormetFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        String wrongSize = "0B";
        if (fileS == 0) {
            return wrongSize;
        }
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "GB";
        }
        return fileSizeString;
    }


    /**
     * 转换文件大小,指定转换的类型
     */
    private static double FormetFileSize(long fileS, int sizeType) {
        DecimalFormat df = new DecimalFormat("#.00");
        double fileSizeLong = 0;
        switch (sizeType) {
            case SIZETYPE_B:
                fileSizeLong = Double.valueOf(df.format((double) fileS));
                break;
            case SIZETYPE_KB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1024));
                break;
            case SIZETYPE_MB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1048576));
                break;
            case SIZETYPE_GB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1073741824));
                break;
            default:
                break;
        }
        return fileSizeLong;
    }
}

