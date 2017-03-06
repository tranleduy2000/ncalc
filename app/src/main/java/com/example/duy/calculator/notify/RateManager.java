package com.example.duy.calculator.notify;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.example.duy.calculator.R;
import com.kobakei.ratethisapp.RateThisApp;

/**
 * Created by DUy on 13-Jan-17.
 */

public class RateManager {

    public static void init(Context context) {
        RateThisApp.Config config = new RateThisApp.Config(3, 15);
        config.setTitle(R.string.my_own_title);
        config.setMessage(R.string.my_own_message);
        config.setYesButtonText(R.string.my_own_rate);
        config.setNoButtonText(R.string.my_own_thanks);
        config.setCancelButtonText(R.string.my_own_cancel);
        RateThisApp.init(config);
    }

    /**
     * create intent go to market play store
     */
    public static void rateApp(Activity activity) {
        Uri uri = Uri.parse("market://details?id=com.duy.com.duy.calculator.free");
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            activity.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            activity.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=com.duy.com.duy.calculator.free")));
        }
    }

}
