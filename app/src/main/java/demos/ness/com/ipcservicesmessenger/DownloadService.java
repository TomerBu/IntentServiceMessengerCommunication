package demos.ness.com.ipcservicesmessenger;

import android.app.IntentService;
import android.content.Intent;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 */
public class DownloadService extends IntentService {

    static final String TAG = "DownloadService";

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public DownloadService() {
        //name Used to name the worker thread, important only for debugging.
        super("LogServiceWorkerThread");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String message = intent.getStringExtra("Message");
        Log.d(TAG, "onStartCommand: " + message);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String message = intent.getStringExtra("Message");
        Log.d(TAG, "onHandleIntent: " + message);
        Messenger origin = intent.getParcelableExtra("messenger");
        doWork(origin, message);
    }

    private void doWork(Messenger origin, String message) {

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Message m = Message.obtain();
        m.obj = "Response From Service: im done with @" + message;
        try {
            origin.send(m);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
