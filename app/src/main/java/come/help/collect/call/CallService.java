package come.help.collect.call;

import android.app.Service;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

public class CallService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
        //获得电话管理器
        TelephonyManager manager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        //为管理器设置监听器,监听电话的呼叫状态
        if (manager != null) {
            manager.listen(new MyPhoneListener(), PhoneStateListener.LISTEN_CALL_STATE);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    private class MyPhoneListener extends PhoneStateListener {
        private String num;//记录来电号码
        private MediaRecorder mRecorder;

        public void onCallStateChanged(int state, String incomingNumber) {
            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING://来电振动
                    num = incomingNumber;
                    Log.i("liao", "RINGING :" + num);
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:// 当接通电话开始通话时  可以进行录音
                    mRecorder = new MediaRecorder();
                    mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC); //此处只实现了录本地MIC输入的声音,若想录入对立通话者的声音
                    mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                    mRecorder.setOutputFile("/mnt/sdcard/" + num + "_" + System.currentTimeMillis() + ".3gp");
                    mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                    try {
                        mRecorder.prepare();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    mRecorder.start();

                    break;
                case TelephonyManager.CALL_STATE_IDLE://挂断电话时停止录音
                    if (mRecorder != null) {
                        mRecorder.stop(); //停止
                        mRecorder.release();//释放
                        mRecorder = null;//垃圾回收
                    }
                    break;
            }
        }
    }

}
