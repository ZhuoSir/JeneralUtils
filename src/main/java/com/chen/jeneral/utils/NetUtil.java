package com.chen.jeneral.utils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

/**
 *
 * Created by sunny-chen on 17/3/3.
 */
public class NetUtil {

    /** 连接超时时间*/
    private static final int    DEF_CONN_TIMEOUT = 30000;

    /** 读时间超时*/
    private static final int    DEF_READ_TIMEOUT = 30000;

    /** 编码格式*/
    private static final String DEF_CHATSET = "UTF-8";

    /** 用户代理*/
    private static final String userAgent = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.66 Safari/537.36";


    /**
     * 发送请求到服务器
     *
     * @param strUrl 请求地址
     * @param params 请求参数
     * @param method 请求方法
     * @return 网络请求字符串
     * @throws Exception
     */
    public static String sendRequest(String strUrl, Map params, String method) {
        HttpURLConnection conn   = null;
        BufferedReader reader = null;
        String rs     = null;

        try {
            StringBuffer sb = new StringBuffer();
            if (null == method || "GET".equals(method)) {
                strUrl = strUrl + "?" + urlencode(params);
            }

            URL url = new URL(strUrl);
            conn = (HttpURLConnection) url.openConnection();

            if (null == method || "GET".equals(method)) {
                conn.setRequestMethod("GET");
            } else {
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
            }

            conn.setRequestProperty("User-agent", userAgent);
            conn.setUseCaches(false);
            conn.setConnectTimeout(DEF_CONN_TIMEOUT);
            conn.setReadTimeout(DEF_READ_TIMEOUT);
            conn.setInstanceFollowRedirects(false);
            conn.connect();

            if (params != null && "POST".equals(method)) {
                try {
                    DataOutputStream out = new DataOutputStream(conn.getOutputStream());
                    out.writeBytes(urlencode(params));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            InputStream is = conn.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, DEF_CHATSET));

            String strRead = null;
            while ((strRead = reader.readLine()) != null) {
                sb.append(strRead);
            }

            rs = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null)
                    reader.close();
                if (conn != null)
                    conn.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return rs;
    }


    // 将map型转为请求参数型
    private static String urlencode(Map<String, Object> data) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry i : data.entrySet()) {
            try {
                sb.append(i.getKey())
                        .append("=")
                            .append(URLEncoder.encode(i.getValue() + "", "UTF-8"))
                                .append("&");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }


    /**
     * 下载方法
     *
     * @param remoteFilePath 下载相关路径
     * @param localFilePath  存储路径
     * */
    public static void download(String remoteFilePath, String localFilePath) {
        HttpURLConnection conn = null;
        BufferedInputStream bis  = null;
        BufferedOutputStream bos  = null;

        try {
            File localFile = new File(localFilePath);
            URL urlFile   = new URL(remoteFilePath);

            conn = (HttpURLConnection) urlFile.openConnection();
            conn.connect();

            bis = new BufferedInputStream(conn.getInputStream());
            bos = new BufferedOutputStream(new FileOutputStream(localFile));

            int    length = 0;
            byte[] buffer = new byte[2048];
            while ((length = bis.read(buffer)) != -1) {
                bos.write(buffer, 0 , length);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != bos)
                    bos.flush();
                if (null != bis)
                    bis.close();
                if (null != conn)
                    conn.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
