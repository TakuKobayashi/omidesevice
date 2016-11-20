package kobayashi.taku.com.omoideservice;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class GCMReceiverService extends FirebaseMessagingService {
	@Override
	public void onCreate() {
		super.onCreate();
		Log.d(Config.TAG, "------------------------------------------create");
	}

	// fot FCM
	@Override
	public void onMessageReceived(RemoteMessage message){
		Log.d(Config.TAG, "------------------------------------------");
		String from = message.getFrom();
		Log.d(Config.TAG, "from:" + from);
		Map<String,String> data = message.getData();
		for(Map.Entry<String, String> e : data.entrySet()){
			Log.d(Config.TAG, "key:" + e.getKey() + " value:" + e.getValue());
		}
	}

	@Override
	public void onDeletedMessages() {
		Log.d(Config.TAG, "------------------------------------------delete");
	}

	@Override
	public void onMessageSent(String msgId) {
		Log.d(Config.TAG, "------------------------------------------sent");
	}

}