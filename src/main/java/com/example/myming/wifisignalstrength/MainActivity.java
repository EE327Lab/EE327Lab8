package com.example.myming.wifisignalstrength;

import android.support.v7.app.AppCompatActivity;
import java.util.Vector;
import com.example.myming.wifisignalstrength.*;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {
    private SuperWiFi rss_scan = null;
    Vector<String> RSSList = null;
    private String testlist = null;
    public static int testID = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final EditText ipText = (EditText)findViewById(R.id.ipText);
        final Button changactivity = (Button)findViewById(R.id.button1);
        final Button cleanlist = (Button)findViewById(R.id.button2);

        rss_scan = new SuperWiFi(this);
        testlist = "";
        testID = 0;

        changactivity.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                testID = testID + 1;
                rss_scan.ScanRss();
                while(rss_scan.isscan()) {}
                RSSList = rss_scan.getRSSlist();
                final EditText ipText = (EditText)findViewById(R.id.ipText);
                testlist = testlist + "testID:" + testID + "\n" + RSSList.toString() + "\n";
                ipText.setText(testlist);
            }
        });
        cleanlist.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                testlist = "";
                ipText.setText(testlist);
                testID = 0;
            }
        });
    }
}
