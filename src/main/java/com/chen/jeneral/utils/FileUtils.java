package com.chen.jeneral.utils;

import java.io.*;

/**
 *
 * Created by sunny-chen on 17/3/2.
 */
public class FileUtils {

    /**
     * 读取文本文件内容
     * @param pathAndName 带有完整绝对路径的文件名
     * @return 返回文本文件的内容
     */
    public static String readText(final String pathAndName)
            throws Exception {
        return readText(pathAndName, "");
    }


    /**
     * 读取文本文件内容
     * @param pathAndName 带有完整绝对路径的文件名
     * @param encoding 文本文件打开的编码方式
     * @return 返回文本文件的内容
     */
    public static String readText(final String pathAndName, String encoding)
            throws Exception {
        if (null == encoding)
            throw new Exception("参数encoding（字符串格式）不能为null");

        encoding = encoding.trim();

        StringBuilder ret    = new StringBuilder();
        FileInputStream fis    = new FileInputStream(pathAndName);
        InputStreamReader isr    = "".equals(encoding) ? new InputStreamReader(fis)
                                                       : new InputStreamReader(fis, encoding);
        BufferedReader reader = new BufferedReader(isr);

        try {
            String buff;
            while ((buff = reader.readLine()) != null) {
                ret.append(buff);
            }
        } catch (Exception e) {
            ret.append(e.toString());
        }

        return ret.toString();
    }


    /**
     * 新建目录
     * @param folderPath 目录地址
     * @return 返回目录创建后的路径
     * */
    public static void createFolder(String folderPath) {
        File filePath = new File(folderPath);
        if (!filePath.exists()) {
            filePath.mkdir();
        }
    }


    /**
     * 多级目录创建
     * @param folderPath 准备要在本级目录下创建新目录的目录路径
     * @param paths 无限级目录参数
     * @return 返回创建文件后的路径
     */
    public static String createFolders(String folderPath, String[] paths) {
        String realPath = folderPath + File.separator;
        for (int i = 0, length = paths.length; i < length; i++) {
            createFolder(realPath + paths[i]);
        }

        return folderPath;
    }


    /**
     * 创建文件
     * @param filePathAndName 文件路径和文件名
     * @return 返回创建文件
     * */
    public static File createFile(String filePathAndName)
            throws IOException {
        return createFile(filePathAndName, "");
    }


    /**
     * 创建文件
     * @param fileContent 文件内容
     * @param filePathAndName 文件路径和文件名
     * @return 返回创建后的文件
     * */
    public static File createFile(String filePathAndName, String fileContent)
            throws IOException {
        File target = getFile(filePathAndName);

        FileWriter fileWriter = new FileWriter(target, true);
        PrintWriter writer     = new PrintWriter(fileWriter);
        writer.print(fileContent);
        writer.close();

        return target;
    }


    /**
     * 创建文件
     * @param fileContent 文件内容
     * @param filePathAndName 文件路径和文件名
     * @param encoding 编码格式
     * @return 返回创建后的文件
     * */
    public static File createFile(String filePathAndName, String fileContent, String encoding)
            throws IOException {
        File target = getFile(filePathAndName);
        PrintWriter writer = new PrintWriter(target, encoding);

        writer.print(fileContent);
        writer.close();

        return target;
    }


    /**
     * 向文件写入文本
     * @param filePathAndName 文件路径和文件名
     * @param text 写入文本
     * @return 返回创建后的文件
     * */
    public static void writeText(String filePathAndName, String text)
            throws IOException {
        File target = getFile(filePathAndName);

        FileWriter fileWriter = new FileWriter(target, true);
        PrintWriter writer     = new PrintWriter(fileWriter);

        writer.print(text);

        writer.close();
        fileWriter.close();
    }


    /**
     * 向文件写入文本;
     * <a>此方法不自动创建文件</>
     *
     * @param target 写入目标文件
     * @param text 写入文本
     * */
    public static void writeText(File target, String text)
            throws Exception {
        if (null == target || !target.exists()) {
            throw new Exception("文件不存在，请创建!");
        }

        FileWriter fileWriter = new FileWriter(target, true);
        PrintWriter writer     = new PrintWriter(fileWriter);

        writer.print(text);

        writer.close();
        fileWriter.close();
    }


    /**
     * 获取文件
     * <a>如果不存在，则创建</>
     * @param filePathAndName 文件路径和文件名
     * @return 返回文件
     * */
    public static File getFile(String filePathAndName)
            throws IOException {
        filePathAndName = filePathAndName.trim();

        File target = new File(filePathAndName);
        if (!target.exists()) {
            target.createNewFile();
        }

        return target;
    }


    /**
     * 删除文件
     *
     * @param filePathAndName 文件路径和文件名
     * @return 操作结果
     * */
    public static boolean deleFile(String filePathAndName)
            throws IOException {
        File target = new File(filePathAndName);

        return target.exists() && target.delete();
    }


    /**
     * 复制文件
     *
     * @param fromFilePath 源文件
     * @param toFilePath 目标文件
     * */
    public static boolean copyFile(String fromFilePath, String toFilePath) throws IOException {
        File from = new File(fromFilePath);
        File to   = new File(toFilePath);

        if (from.exists()) {
            InputStream is = new FileInputStream(fromFilePath);
            FileOutputStream os = new FileOutputStream(to);

            byte[] buffer       = new byte[1024];
            int    byteRead;
            while ((byteRead = is.read(buffer)) != -1) {
                os.write(buffer, 0, byteRead);
            }

            is.close();
            os.close();

            return true;
        } else {
            return false;
        }
    }


    /**
     * 复制文件
     *
     * @param fromFilePath 源文件
     * @param toFilePath 目标文件
     * */
    public static boolean moveFile(String fromFilePath, String toFilePath)
            throws IOException {
        return copyFile(fromFilePath, toFilePath) && deleFile(fromFilePath);
    }


    /**
     * 获取文件的总大小
     *
     * */
    public static long getFileTotalSizeInDir(File file) {
        if (file.isFile())
            return file.length();
        File[] children = file.listFiles();
        long total = 0;
        if (null != children) {
            for (File child : children) {
                total += getFileTotalSizeInDir(child);
            }
        }

        return total;
    }
}
