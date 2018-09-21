package come.help.collect.note;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Telephony;
import android.util.Log;

import com.business.utillibrary.util.logger.Logger;

import java.util.ArrayList;
import java.util.List;


public class NoteUtils {
    private static Uri SMS_INBOX = Uri.parse("content://sms/");


    public static List<NoteBean> getSmsFromPhone(Context context) {
        return getSmsFromPhone(context, 0);
    }

    /**
     * @param context
     * @param size    获取条数.小于等于0获取全部。
     */
    public static List<NoteBean> getSmsFromPhone(Context context, int size) {
        ContentResolver cr = context.getContentResolver();
        String[] projection = {Telephony.Sms.ADDRESS,
                Telephony.Sms.BODY,
                Telephony.Sms.DATE,
                Telephony.Sms.PERSON
        };
        Cursor cur = cr.query(SMS_INBOX, projection, null, null, "date desc");
        List<NoteBean> beanList = new ArrayList<>();
        if (null == cur) {
            return null;
        }
        while (cur.moveToNext()) {
            NoteBean bean = new NoteBean();
            bean.setDate(cur.getLong(cur.getColumnIndex(Telephony.Sms.DATE)));
            bean.setAddress(cur.getString(cur.getColumnIndex(Telephony.Sms.ADDRESS)));
            bean.setMessage(cur.getString(cur.getColumnIndex(Telephony.Sms.BODY)));
            if (size <= 0) {
                Log.i("liao", bean.toString());
                beanList.add(bean);
            } else {
                if (beanList.size() < size) {
                    Log.i("liao", bean.toString());
                    beanList.add(bean);
                } else {
                    break;
                }
            }
        }
        cur.close();
        return beanList;
    }

}
