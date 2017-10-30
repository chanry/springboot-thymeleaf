package com.trs.gallery.util;

import org.apache.commons.io.FilenameUtils;

public class FileUtil {
	
	/**
	 * Java文件操作 获取不带扩展名的文件名
	 * @Description (TODO这里用一句话描述这个方法的作用)
	 * @param filename
	 * @return
	 */
    public static String getFileNameNoEx(String filename) { 
        if ((filename != null) && (filename.length() > 0)) { 
            int dot = filename.lastIndexOf('.'); 
            if ((dot >-1) && (dot < (filename.length()))) { 
                return filename.substring(0, dot); 
            } 
        } 
        return filename; 
    }
    
    /**
     * 根据文件路径获取文件扩展名
     * @Description (TODO这里用一句话描述这个方法的作用)
     * @param filename
     * @return
     */
    public static String getFileExtension(String path) {
    	return FilenameUtils.getExtension(path);
    }
    
    public static String getFileName(String path) {
    	return FilenameUtils.getName(path);
    }
    
    public static void main(String[] args) {
		String str = " 1.乘坐地铁六号线至港城路终点站下换乘公交外高桥1路至滨江公园站。 2.乘坐浦东17路、高中线、外高桥3路、81、640、181路到终点站港城路站，再转乘外高桥1路到公园门口；乘坐971、815等车辆至高桥可换乘以上车辆到港城路地铁站。 3.乘坐508路至护塘桥可换外高桥1路直达公园。 自驾：从市区可走内环高架-逸仙路高架-外环线-外环隧道-江东路出口-滨江森林公园。";
		System.out.println(str.getBytes().length);
	}
    
}
