package kobayashi.taku.com.omoideservice;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class TokenRefreshService extends FirebaseInstanceIdService {
	@Override
	public void onTokenRefresh() {
		String refreshedToken = FirebaseInstanceId.getInstance().getToken();
		// 自前サーバーへのRegistrationId送信処理を実装
		Log.d(Config.TAG, "------------------------------------------");
		Log.d(Config.TAG, "token:" + refreshedToken);
	}
}