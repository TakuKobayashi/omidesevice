package kobayashi.taku.com.omoideservice;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

public class MainActivity extends AppCompatActivity {
    private CallbackManager callbackManager;
    private static int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ApplicationHelper.requestPermissions(this, REQUEST_CODE);

        Button button = (Button) findViewById(R.id.gotoPremiumContentView);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, PremiumContentActivity.class);
                startActivity(intent);
            }
        });
        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("email", "public_profile");
        Log.d(Config.TAG, "accessToken:" + AccessToken.getCurrentAccessToken().getToken());

        // Other app specific specialization
//        if(isLoggedIn()){
//            loginButton.setVisibility(View.INVISIBLE);
//        }else {
            callbackManager = CallbackManager.Factory.create();
            // Callback registration
            loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    // App code
                    Log.d(Config.TAG, "success: " + loginResult.getAccessToken());
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
//        }
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
}
