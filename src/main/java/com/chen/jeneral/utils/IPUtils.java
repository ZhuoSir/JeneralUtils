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

    public static String PUBLIC_IP = getPublicIP();

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

        org.jsoup.nodes.Document doc = null;
        Connection con = null;
        con = Jsoup.connect("http://1212.ip138.com/ic.asp").timeout(10000);

        try {
            doc = con.get();
            ip = doc.body().getElementsByAttributeValueEnding("align", "center").text();
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
