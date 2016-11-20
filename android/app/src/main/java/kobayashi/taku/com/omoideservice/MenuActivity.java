package kobayashi.taku.com.omoideservice;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;

public class MenuActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_view);

        ImageView image = (ImageView) findViewById(R.id.menu_banner);
        image.setImageResource(R.mipmap.img_sec);

        TextView greetText = (TextView) findViewById(R.id.menu_greet);
        greetText.setText(getText(R.string.menu_greet_message));

        TextView annouceText = (TextView) findViewById(R.id.menu_announce);
        annouceText.setText(getString(R.string.menu_announce_message));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
