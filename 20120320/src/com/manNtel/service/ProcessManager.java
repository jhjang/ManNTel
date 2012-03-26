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
     * Activity �߰� Method
     * @param activity
     */
    public void addActivity(Activity activity)
    {
        if(!isActivity(activity))
            mActivityArr.add(activity);
    }
     
    /**
     * Activity ���� Method
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
     * Parameter�� ���� Activity�� ���� ����Ʈ�� �ִ��� üũ�ϴ� Method
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
     * ��ϵǾ� �ִ� ��� Activity ����
     */
    public void allEndActivity()
    {
        for(Activity activity:mActivityArr) {
            activity.finish();
        }
    }
}