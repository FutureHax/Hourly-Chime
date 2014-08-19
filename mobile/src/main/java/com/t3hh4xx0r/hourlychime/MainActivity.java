package com.t3hh4xx0r.hourlychime;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ExclusionTimePeriod.main();
//        findViewById(R.id.send).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                BootReceiver.setAlarm(v.getContext(), true);
//            }
//        });
//        findViewById(R.id.stop).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                BootReceiver.stopForce(v.getContext());
//            }
//        });
//        BootReceiver.setAlarm(this, false);
    }


}
