package cn.jianke.customcache.data;
import android.content.Context;
import android.content.SharedPreferences;
import java.util.LinkedHashSet;
import java.util.Set;
import cn.jianke.customcache.utils.StringUtil;

public class SharePreferenceUtil {
    private static String DATA_INFO_SHAREDPREFERENCE = "data";
    // sington
    private static SharePreferenceUtil builder;
    //SharedPreferences Editor object
    private static SharedPreferences.Editor editor;
    //SharedPreferences object
    private static SharedPreferences mPre;
    private static String SP_LOCAL_CACHE = "sp_local_cache";

    private SharePreferenceUtil(){
    }

    public synchronized static SharePreferenceUtil getInstance(Context context) {
        return getInstance(context, DATA_INFO_SHAREDPREFERENCE);
    }

    public synchronized static SharePreferenceUtil getInstance(Context context, String fileName) {
        if (builder == null) {
            builder = new SharePreferenceUtil();
        }
        if (StringUtil.isEmpty(fileName)) {
            fileName = DATA_INFO_SHAREDPREFERENCE;
        }
        mPre = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        editor = mPre.edit();

        return builder;
    }

    public synchronized void clearData(Context mContext) {
        try {
            SpLocalCache.clear(mContext, getSpLocalCache());
            if (editor != null)
                editor.clear().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void setSpLocalCache(String cacheName){
        try {
            Set<String> cacheSet = getSpLocalCache();
            cacheSet.add(cacheName);
            editor.putStringSet(SP_LOCAL_CACHE, cacheSet);
            editor.commit();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public synchronized Set<String> getSpLocalCache(){
        Set<String> result;
        result = mPre.getStringSet(SP_LOCAL_CACHE, new LinkedHashSet<String>());
        return result;
    }
}