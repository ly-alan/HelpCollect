package come.help.collect.note;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.business.utillibrary.util.logger.Logger;

import java.util.List;

public class NoteBroadCast extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("liao", "NoteBroadCast --> onReceive");
        List<NoteBean> list = NoteUtils.getSmsFromPhone(context, 1);
        Log.d("liao", "size = " + (list == null ? "0" : list.size()));
        if (list != null && list.size() > 0) {
            Log.d("liao", "data = " + list.get(0).toString());
        }
    }
}
