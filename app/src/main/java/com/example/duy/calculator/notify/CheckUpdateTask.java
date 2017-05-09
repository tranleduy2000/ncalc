/*
 * Copyright 2017 Tran Le Duy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.duy.calculator.notify;

import android.content.DialogInterface;
import android.os.Handler;
import android.support.v7.app.AlertDialog;

import com.example.duy.calculator.activities.abstract_class.AbstractAppCompatActivity;
import com.example.duy.calculator.BuildConfig;
import com.example.duy.calculator.DLog;
import com.example.duy.calculator.R;
import com.example.duy.calculator.data.CalculatorSetting;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * CheckUpdateTask
 * Created by Duy on 17-Feb-17.
 */

public class CheckUpdateTask {
    /**
     * activity for show dialog
     */
    private AbstractAppCompatActivity mActivity;
    private Handler handler = new Handler();
    private DatabaseReference mDatabase;

    public CheckUpdateTask(AbstractAppCompatActivity activity) {
        this.mActivity = activity;
    }

    public void execute() {
        DLog.i("execute");
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        final String currentVersionName = BuildConfig.VERSION_NAME;
        mDatabase.child("VersionName").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    try {
                        String s = dataSnapshot.getValue(String.class);
                        DLog.i("Version on firebase is " + s);
                        if (!currentVersionName.equalsIgnoreCase(s)) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                            builder.setMessage(R.string.new_version_update);
                            builder.setTitle(R.string.update);
                            builder.setIcon(R.drawable.ic_system_update_black_24dp);
                            builder.setNegativeButton(R.string.close, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            builder.setPositiveButton(R.string.update, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mActivity.rateApp();
                                }
                            });
                            builder.setNeutralButton(R.string.dismiss, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    CalculatorSetting setting = new CalculatorSetting(mActivity);
                                    setting.setNotifyUpdate(false);
                                }
                            });
                            final AlertDialog alertDialog = builder.create();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    alertDialog.show();
                                }
                            }, 1000);
                        }
                    } catch (Exception e) {
                        DLog.e(e.getMessage());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
