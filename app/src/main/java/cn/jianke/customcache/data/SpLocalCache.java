package cn.jianke.customcache.data;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Base64;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Set;
import cn.jianke.customcache.module.ThreadManager;
import cn.jianke.customcache.utils.StringUtil;

public class SpLocalCache<T> {
    private static final String KEY_DATA = "data";

    private String cacheName;

    private Handler mHandler = new Handler();

    public static void clear(final Context context, final Set<String> cacheSet){
        ThreadManager.getInstance().getNewCachedThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                synchronized (SpLocalCache.class) {
                    if (cacheSet == null)
                        return;
                    for (String cacheName : cacheSet) {
                        SharedPreferences spLc = context.getSharedPreferences(
                                cacheName,
                                Context.MODE_PRIVATE
                        );
                        spLc.edit().clear().commit();
                    }
                }
            }
        });
    }

    public SpLocalCache(Class<T> forWhich){
        cacheName = forWhich.getName();
    }

    public SpLocalCache(Class<T> forWhich, Class modelClass){
        cacheName = forWhich.getName() + "_" + modelClass.getName();
    }

    public void save(final Context context, final T data){
                ThreadManager.getInstance().getNewCachedThreadPool().execute(new Runnable() {
                    @Override
                    public void run() {
                        synchronized (SpLocalCache.class) {
                            final SharedPreferences spLc = context.getSharedPreferences(
                                    cacheName,
                                    Context.MODE_PRIVATE
                            );
                            String strData = base64Encode(data);
                            if (strData != null)
                                spLc.edit()
                                        .putString(KEY_DATA, base64Encode(data))
                                        .commit();
                            SharePreferenceUtil.getInstance(context).setSpLocalCache(cacheName);
                        }
                    }
                });
    }

    public void read(final Context context,  final LocalCacheCallBack localCacheCallBack){
        ThreadManager.getInstance().getNewCachedThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                synchronized (SpLocalCache.class) {
                    SharedPreferences spLc = context.getSharedPreferences(
                            cacheName,
                            Context.MODE_PRIVATE
                    );
                    final String strData = spLc.getString(KEY_DATA, null);
                    if (StringUtil.isNotEmpty(strData)) {
                        final Object obj = base64Decode(strData);
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (localCacheCallBack != null)
                                    localCacheCallBack.readCacheComplete(obj);
                            }
                        });
                    }else {
                        if (localCacheCallBack != null)
                            localCacheCallBack.readCacheComplete(null);
                    }
                }
            }
        });
    }

    public Object read(final Context context){
        SharedPreferences spLc = context.getSharedPreferences(
                cacheName,
                Context.MODE_PRIVATE
        );
        String strData = spLc.getString(KEY_DATA, null);
        Object obj = base64Decode(strData);
        return obj;
    }

    public void clear(final Context context) {
                ThreadManager.getInstance().getNewCachedThreadPool().execute(new Runnable() {
                    @Override
                    public void run() {
                        synchronized (SpLocalCache.class) {
                            SharedPreferences spLc = context.getSharedPreferences(
                                    cacheName,
                                    Context.MODE_PRIVATE
                            );
                            spLc.edit().putString(KEY_DATA, "").commit();
                        }
                    }
                });
    }

    private String base64Encode(T data){
        String result = null;
        try {
            byte[] dataByte = getBytesFromObject(data);
            if(dataByte != null)
                result = Base64.encodeToString(dataByte, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private static Object base64Decode(String strData){
        Object result = null;
        try {
            byte[] dataByte = Base64.decode(strData, Base64.DEFAULT);
            result = getObjectFromBytes(dataByte);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private byte[] getBytesFromObject(T obj) throws Exception{
        if (obj == null){
            return null;
        }
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        ObjectOutputStream oo = new ObjectOutputStream(bo);
        oo.writeObject(obj);
        return bo.toByteArray();
    }

    private static Object getObjectFromBytes(byte[] objBytes) throws Exception {
        if (objBytes == null || objBytes.length == 0) {
            return null;
        }
        ByteArrayInputStream bi = new ByteArrayInputStream(objBytes);
        ObjectInputStream oi = new ObjectInputStream(bi);
        return oi.readObject();
    }

    public interface LocalCacheCallBack{
        void readCacheComplete(Object obj);
    }
}
