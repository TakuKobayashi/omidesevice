package kobayashi.taku.com.omoideservice;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

public class OmoideServiceApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		FacebookSdk.sdkInitialize(getApplicationContext());
		AppEventsLogger.activateApp(this);
		ExtraLayout.getInstance(ExtraLayout.class).init(this);
		ExtraLayout.getInstance(ExtraLayout.class).setBaseDisplaySize(1080, 1920);
		ExtraLayout.getInstance(ExtraLayout.class).setDisplayPolicy(ExtraLayout.DISPLAY_POLICY_SHOW_ALL);
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
	}
}
