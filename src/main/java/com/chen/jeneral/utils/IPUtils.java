package com.chen.jeneral.utils;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;

public class IPUtils {

    public static String LocalhostIP = getLocalHostIp();

    public static String INTERNET_IP = getIntranetIp();

    private IPUtils(){}

    /**
     * 获得本地IP
     * @return 本地IP
     */
    public static String getLocalHostIp(){
        try{
            return InetAddress.getLocalHost().getHostAddress();
        } catch(Exception e){
            throw new RuntimeException(e);
        }
    }


    /**
     * 获得内网IP
     * @return 内网IP
     */
    public static String getIntranetIp(){
        try{
            Enumeration<NetworkInterface> networks = NetworkInterface.getNetworkInterfaces();
            InetAddress ip = null;
            Enumeration<InetAddress> addrs;
            while (networks.hasMoreElements())
            {
                addrs = networks.nextElement().getInetAddresses();
                while (addrs.hasMoreElements())
                {
                    ip = addrs.nextElement();
                    if (ip != null
                            && ip instanceof Inet4Address
                            && ip.isSiteLocalAddress()
                            && !ip.getHostAddress().equals(LocalhostIP))
                    {
                        return ip.getHostAddress();
                    }
                }
            }

            // 如果没有外网IP，就返回内网IP
            return LocalhostIP;
        } catch(Exception e){
            throw new RuntimeException(e);
        }
    }


    /**
     * 得到本机的外网ip，出现异常时返回本地IP
     * @return
     */
    public static String getPublicIP() {
        String ip = null;

        try {
            ip = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        org.jsoup.nodes.Document doc = null;
        Connection con = null;
        // 连接 http://1212.ip138.com/ic.asp
        con = Jsoup.connect("http://1212.ip138.com/ic.asp").timeout(10000);

        try {
            doc = con.get();
            // 获得包含本机ip的文本串：您的IP是：[xxx.xxx.xxx.xxx] 来自：YY
            org.jsoup.select.Elements els = doc.body().select("center");
            for (org.jsoup.nodes.Element el : els) {
                ip = el.text();
            }
            // 从文本串过滤出ip，用正则表达式将非数字和.替换成空串""
            ip = ip.replaceAll("[^0-9.]", "");
        } catch (IOException e) {
            e.printStackTrace();
            return ip;
        }

        return ip;
    }


    /**
     * 获取请求IP
     *
     * */
    public static String getRequestIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
            ip = request.getHeader("Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
            ip = request.getRemoteAddr();
        }
        return ip.equals("0:0:0:0:0:0:0:1") ? "127.0.0.1" : ip;
    }
}
