package com.example.mytaobaounion.Utils;


//import static com.vondear.rxtool.RxTool.getContext;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.mytaobaounion.Base.BaseApplication;
import com.example.mytaobaounion.Model.Domain.CacheWithDuration;
import com.example.mytaobaounion.Presenter.Impl.SearchPresenterImpl;
import com.google.gson.Gson;

import java.util.List;

public class JsonCacheUtils {
    private static final String JSON_CACHE_SP_NAME = "json_cache_sp_name";
    private static JsonCacheUtils jsonCacheUtils=null;
    private final SharedPreferences mSharedPreferences;
    private final Gson gson;

    public JsonCacheUtils(){
        mSharedPreferences = BaseApplication.getAppContext().getSharedPreferences(JSON_CACHE_SP_NAME,Context.MODE_PRIVATE);
        gson = new Gson();
    }


    public static JsonCacheUtils getInstance(){
        if(jsonCacheUtils==null){
            jsonCacheUtils=new JsonCacheUtils();
        }
        return jsonCacheUtils;
    }



    public void saveCache(String key,Object value){
        this.saveCache(key,value,-1L);
    }

    //这里key是固定的（逻辑层设置的KEY_HISTORY）而不是history词条，value是cacheWithDuration（即带duration的history词条

    /**
     * @param key 固定值，即逻辑层设置的KEY_HISTORY
     * @param value 自定义的Histories（相当于history词条的集合），下面的方法中会转化成cacheWithDuration，里面储存的是Histories的json串以及duration
     * @param duration “保质期”
     * 在sharedPreferences中，以KEY_HISTORY为key，以cacheWithDuration的json串为value，因为历史数据都储存在Histories里的集合里，所以就相当于全都储存在value的json串中
     */
    public void saveCache(String key,Object value,long duration){
        SharedPreferences.Editor editor = mSharedPreferences.edit();

        //先将数据转化成json串，再转成带Duration的json串
        String jsonVal = gson.toJson(value);

        //-1表示永久缓存
        if(duration!=-1L){
            duration+=System.currentTimeMillis();
        }

        CacheWithDuration cache=new CacheWithDuration(duration,jsonVal);
        String jsonValWithDuration = gson.toJson(cache);
        editor.putString(key,jsonValWithDuration);
        editor.apply();

    }


    public void removeCache(String key){
        mSharedPreferences.edit().remove(key).apply();
    }


    //把储存的json串转化成原来的数据类型，因此这里的泛型是指saveCache中储存的Object
    public <T> T getCache(String key,Class<T> tClass){
        String cacheWithDurationJson = mSharedPreferences.getString(key, null);
        if(cacheWithDurationJson==null){
            LogUtils.i(JsonCacheUtils.class,"cacheWithDuration为空");
            return null;
        }

        //储存数据时经历了两次json转化，一次将数据转化成json串并利用其构造cacheDuration对象，第二次是将cacheDuration对象转化成json串储存起来，因此反序列化时也要转化两次
        CacheWithDuration cacheWithDuration = gson.fromJson(cacheWithDurationJson, CacheWithDuration.class);
        long duration = cacheWithDuration.getDuration();

        //注意条件，-1代表可以永久缓存，因此如果!=-1并且超时，则缓存过期，返回null
        if(duration!=-1&&duration-System.currentTimeMillis()<=0){
            return null;
        }
        else{
            //第二次转换
            String cacheJson = cacheWithDuration.getCache();
            T t = gson.fromJson(cacheJson, tClass);
            return t;
        }
    }


}
