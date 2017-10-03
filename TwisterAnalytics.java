package app.evilhedgehog.twister;

import android.app.Application;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import java.util.HashMap;

public class TwisterAnalytics extends Application {
    HashMap<TrackerName, Tracker> mTrackers = new HashMap();
    private boolean noAd = false;

    public enum TrackerName {
        APP_TRACKER,
        GLOBAL_TRACKER,
        ECOMMERCE_TRACKER
    }

    synchronized Tracker getTracker(TrackerName trackerId) {
        if (!this.mTrackers.containsKey(trackerId)) {
            this.mTrackers.put(trackerId, GoogleAnalytics.getInstance(this).newTracker((int) C0106R.xml.analytics));
        }
        return (Tracker) this.mTrackers.get(trackerId);
    }
}
