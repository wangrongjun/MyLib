package com.wang.web;

import com.wang.java_util.DateUtil;
import com.wang.java_util.FileUtil;

import java.io.File;

public class CopyClass {

    public static String androidStudioModuleName = "server";
    public static String serverName = "ebook";

    public static void main(String[] args) throws Exception {
        String fromPath = androidStudioModuleName + "/build/classes/main/";
        String toPath = "C:/IDE/apache-tomcat-7.0.67/webapps/" + serverName + "/WEB-INF/";

        FileUtil.copyDir(new File(fromPath), new File(toPath));
        FileUtil.deleteDir(new File(toPath, "classes"));
        new File(toPath, "main").renameTo(new File(toPath, "classes"));

        fromPath = "C:\\IDE\\android-studio-project\\MyLib\\java_lib\\build\\libs\\java_lib.jar";
        toPath = toPath + "lib/java_lib.jar";
        new File(toPath).delete();
        FileUtil.copy(fromPath, toPath);

        System.out.println(DateUtil.getCurrentTime());
        System.out.println("Web update succeed!");
    }

}
