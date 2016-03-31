package com.example.myming.wifisignalstrength;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.lang.String;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.util.Log;

/**
 * Created by My.Ming on 2016/3/29.
 */
public class SuperWiFi extends MainActivity {
    static final String TAG = "SuperWiFi";
    static SuperWiFi wifi = null;
    static Object sync = new Object();
    static int TESTTIME = 25;
    WifiManager wm = null;
    private Vector<String> scanned = null;
    boolean isScanning = false;
    private int[] APRSS = new int[10];
    private FileOutputStream out;
    private int p;

    public SuperWiFi(Context context) {
        this.wm = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        this.scanned = new Vector<String>();
    }

    public void ScanRss() {
        startScan();
    }

    public boolean isscan() {
        return isScanning;
    }

    public Vector<String> getRSSlist() {
        return scanned;
    }

    private void startScan() {
        this.isScanning = true;
        Thread scanThread = new Thread(new Runnable() {
            public void run() {
                scanned.clear();
                for(int j = 1; j <= 10; ++j) {
                    APRSS[j - 1] = 0;
                }
                p = 1;
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日  HH:mm:ss");
                Date curDate = new Date(System.currentTimeMillis());
                String str = formatter.format(curDate);
                for(int k = 1; k <= 10; ++k) {
                    write2file("RSS-IWCTAP"+k+".txt", "testID:"+testID+" TestTime: "+str+" BEGIN\n");
                }

                while(p <= TESTTIME) {
                    performScan();
                    p = p + 1;
                }
                for(int i = 1; i <= 10; ++i) {
                    scanned.add("IWCTAP" + i + "= "+APRSS[i - 1] / TESTTIME+"\n");
                }
                for(int k = 1; k <= 10; ++k) {
                    write2file("RSS-IWCTAP"+k+".txt", "testID:"+testID+"END\n");
                }
                isScanning = false;
            }
        });
        scanThread.start();
    }

    private void performScan() {
        if(wm == null) return;
        try {
            if(!wm.isWifiEnabled()) {
                wm.setWifiEnabled(true);
            }

            wm.startScan();
            try{
                Thread.sleep(3000);
            } catch(InterruptedException e) {
                e.printStackTrace();
            }

            this.scanned.clear();
            List<ScanResult> sr = wm.getScanResults();
            Iterator<ScanResult> it = sr.iterator();
            while(it.hasNext()) {
                ScanResult ap = it.next();
                Log.d(TAG, Integer.toString(ap.level));
                Log.d(TAG, ap.SSID);
                for(int k = 1; k <= 10; ++k) {
                    if(ap.SSID.equals("Xuxu")) {
                        APRSS[k-1] = APRSS[k-1] + ap.level;
                        write2file("Xuxu"+k+".txt", ap.level + "\n");
                    }
                }
            }
        } catch(Exception e) {
            this.isScanning = false;
            this.scanned.clear();
            Log.d(TAG, e.toString());
        }
    }

    private void write2file(String filename, String a) {
        try{
            File file = new File("/sdcard/" + filename);
            if(!file.exists()) {
                file.createNewFile();
            }
            RandomAccessFile randomFile = new RandomAccessFile("/sdcard/" + filename, "rw");
            long fileLength = randomFile.length();
            randomFile.seek(fileLength);
            randomFile.writeBytes(a);
            randomFile.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
