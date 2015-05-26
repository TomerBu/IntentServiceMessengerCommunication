package demos.ness.com.ipcservicesmessenger;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.lang.ref.WeakReference;


public class MainActivity extends ActionBarActivity {

    private Handler mHandler;
    private TextView tvResponse;
    private EditText etMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initLayout();
        mHandler = new MainActivityHandler(this);
    }

    private void initLayout() {
        tvResponse = (TextView) findViewById(R.id.tvResponse);
        etMessage = (EditText) findViewById(R.id.etMessage);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            tvResponse.setText("Just an update from settings to Show that handler is still working properly");
                        }
                    });
                }
            });
            thread.start();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void sendMessage(View view) {
        Intent intent = new Intent(this, DownloadService.class);
        Messenger m = new Messenger(mHandler);
        intent.putExtra("messenger", m);
        intent.putExtra("Message", etMessage.getText().toString());
        etMessage.setText("");
        startService(intent);
    }

    private static class MainActivityHandler extends Handler {
        private WeakReference<MainActivity> main;

        public MainActivityHandler(MainActivity main) {
            this.main = new WeakReference<MainActivity>(main);
        }

        @Override
        public void handleMessage(Message msg) {
            Log.d("Tomer", "Got Message back from service: " + msg.obj.toString());
            if (main.get() != null) {
                MainActivity m = main.get();
                m.tvResponse.setText(msg.obj.toString());
            }
        }

    }
}
