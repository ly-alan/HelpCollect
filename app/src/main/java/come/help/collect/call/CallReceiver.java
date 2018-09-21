package come.help.collect.call;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class CallReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
   // 如果是拨打电话
        Log.i("liao","call Receiver");
        context.startService(new Intent(context,CallService.class));
    }
}


