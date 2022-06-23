package com.example.mytaobaounion.Utils;

public class UrlUtils {
    public static String createHomePagerUrl(int catagoryId,int page){
        return "discovery/"+catagoryId+"/"+page;
    }

    public static String getCoverPath(String s,int size) {
        return "https:" + s+"_"+size+"x"+size+".jpg";
    }

    //上面方法的重载
    public static String getCoverPath(String s) {
        if(s.startsWith("http")||s.startsWith("https")){
            return s;
        }
        else{
            return "https:"+s;
        }
    }

    public static String getTicketUrl(String url) {
        if(url.startsWith("http")||url.startsWith("https")){
            return url;
        }
        else{
            //分号分号分号！！！
            return "https:"+url;
        }
    }


    public static String getSelectedContentUrl(int catagoryId) {
        return "recommend/"+catagoryId;
    }

    public static String getRedPacketContentUrl(int page){
        return "onSell/"+page;
    }
}