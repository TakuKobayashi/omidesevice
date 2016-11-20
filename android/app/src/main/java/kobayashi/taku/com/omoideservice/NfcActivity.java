package kobayashi.taku.com.omoideservice;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.NfcF;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import java.util.ArrayList;

public class NfcActivity extends Activity {
    private IntentFilter[] intentFiltersArray;
    private String[][] techListsArray;
    private NfcAdapter mAdapter;
    private PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc);

        ArrayList<String> infoList = new ArrayList<String>();

        Intent intent = getIntent();
        String action = intent.getAction();
        infoList.add("intentAction:" + action);
        Log.d(Config.TAG, "----------------------------------");
        if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {
            Tag tag = (Tag) intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

            // NFCからID情報取得
            byte[] ids = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
            infoList.add("IDbytes:" + ids.length);
            StringBuilder tagId = new StringBuilder();
            for (int i=0; i<ids.length; i++) {
                tagId.append(String.format("%02x", ids[i] & 0xff));
            }
            Log.d(Config.TAG, "id: " + tagId.toString());
            infoList.add("nfcId:" + tagId.toString());

            // カードID取得。Activityはカード認識時起動に設定しているのでここで取れる。


            for(String t : tag.getTechList()){
                Log.d(Config.TAG, "t: " + tagId.toString());
            }
        }

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!isFinishing()){
                    finish();
                }
            }
        }, 2000);

        MediaPlayer se = MediaPlayer.create(this, R.raw.pay);
        se.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });
        se.start();
    }

    @Override
    public void onNewIntent(Intent intent) {
        Log.d(Config.TAG, "new Intent " + intent.getAction() + " with intent: " + intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
