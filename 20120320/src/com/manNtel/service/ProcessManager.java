package com.manNtel.service;

import java.util.ArrayList;

import android.app.Activity;
 
/**
 * @date    2011.5.31
 * @author  fateindestiny(Kim Kiman)
 */
public class ProcessManager
{
    private static  ProcessManager  instance = null;
    private ArrayList<Activity>   mActivityArr;
     
    private ProcessManager()
    {
        // Constructor
        mActivityArr = new ArrayList<Activity>();
    }
     
    public static ProcessManager getInstance()
    {
        if(ProcessManager.instance == null) {
            synchronized(ProcessManager.class) {
                if(ProcessManager.instance == null) {
                    ProcessManager.instance = new ProcessManager();
                }
            }
        }
        return ProcessManager.instance;
    }
     
    /**
     * Activity 추가 Method
     * @param activity
     */
    public void addActivity(Activity activity)
    {
        if(!isActivity(activity))
            mActivityArr.add(activity);
    }
     
    /**
     * Activity 삭제 Method
     * @param activity
     */
    public void deleteActivity(Activity activity)
    {
        if(isActivity(activity)) {
            activity.finish();
            mActivityArr.remove(activity);
        }
    }
     
    /**
     * Parameter로 들어온 Activity가 현재 리스트에 있는지 체크하는 Method
     * @param activity
     * @return
     */
    public boolean isActivity(Activity activity)
    {
        for(Activity chkActivity:mActivityArr) {
            if(chkActivity == activity)
                return true;
        }
        return false;
    }
     
    /**
     * 등록되어 있는 모든 Activity 종료
     */
    public void allEndActivity()
    {
        for(Activity activity:mActivityArr) {
            activity.finish();
        }
    }
}