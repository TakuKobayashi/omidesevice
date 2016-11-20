package kobayashi.taku.com.omoideservice;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {
    private CallbackManager callbackManager;
    private static int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_view);
        ApplicationHelper.requestPermissions(this, REQUEST_CODE);

        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("email", "public_profile");

        ImageView image = (ImageView) findViewById(R.id.login_banner);
        image.setImageResource(R.mipmap.img_sec);

        // Other app specific specialization
        if(isLoggedIn()){
            loginButton.setVisibility(View.INVISIBLE);
            SharedPreferences sp = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
            if(!sp.contains("user_token")){
                sendToToken(AccessToken.getCurrentAccessToken().getToken());
            }
        }else {
            callbackManager = CallbackManager.Factory.create();
            // Callback registration
            loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    // App code
                    Log.d(Config.TAG, "success: " + loginResult.getAccessToken());
                    LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
                    loginButton.setVisibility(View.INVISIBLE);
                    sendToToken(loginResult.getAccessToken().getToken());
                }

                @Override
                public void onCancel() {
                    // App code
                    Log.d(Config.TAG, "cancel");
                }

                @Override
                public void onError(FacebookException exception) {
                    // App code
                    Log.d(Config.TAG, "error:" + exception.getMessage());
                }
            });
        }
    }

    private void sendToToken(String facebookAccessToken){
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("facebook_access_token", facebookAccessToken);
        params.put("push_token", refreshedToken);
        params.put("nfc_tag_id", "aaa");
        ApiRequest apiRequest = new ApiRequest();
        apiRequest.addCallback(new ApiRequest.ResponseCallback() {
            @Override
            public void onSuccess(String url, String body) {
                SharedPreferences sp = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                try {
                    JSONObject jObject = new JSONObject(body);
                    editor.putString("user_token", jObject.getString("user_token"));
                    editor.apply();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d(Config.TAG, "url:" + url + " body:" + body );
            }
        });
        apiRequest.setParams(params);
        apiRequest.execute(Config.ROOT_URL + "/account/sign_in");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        Log.d(Config.TAG, "accessToken:" + accessToken );
        return accessToken != null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ApplicationHelper.releaseImageView((ImageView) findViewById(R.id.login_banner));
    }
}
