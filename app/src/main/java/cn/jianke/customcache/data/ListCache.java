package cn.jianke.customcache.data;
import java.io.Serializable;
import java.util.ArrayList;

public class ListCache<T> implements Serializable{
    private static final long serialVersionUID = -3276096981990292013L;
    private ArrayList<T> objList;
    public ArrayList<T> getObjList() {
        return objList;
    }

    public void setObjList(ArrayList<T> objList) {
        this.objList = objList;
    }
}
