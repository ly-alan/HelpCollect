package come.help.collect;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityManager;

import java.util.List;

import come.help.collect.location.GDLocationService;
import come.help.collect.note.NoteUtils;
import come.help.collect.weixin.WxService;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onclick(View view) {
        switch (view.getId()) {
            case R.id.note:
                getNote();
                break;
            case R.id.location:
                getLocation();
                break;
            case R.id.call:
                break;
            case R.id.weixin:
                getWxInfo();
                break;
        }
    }

    /**
     * 获取短信
     */
    private void getNote() {
        NoteUtils.getSmsFromPhone(this, 2);
    }

    /**
     * 获取定位
     */
    private void getLocation() {
        startService(new Intent(this, GDLocationService.class));
    }

    /**
     * 获取微信信息
     */
    private void getWxInfo() {
        if (!isServiceEnabled()) {
            Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            startActivity(intent);
        }
    }

    //检查服务是否开启
    private boolean isServiceEnabled() {
        AccessibilityManager accessibilityManager = (AccessibilityManager) getSystemService(Context.ACCESSIBILITY_SERVICE);

        List<AccessibilityServiceInfo> accessibilityServices =
                accessibilityManager.getEnabledAccessibilityServiceList(
                        AccessibilityServiceInfo.FEEDBACK_ALL_MASK);
        for (AccessibilityServiceInfo info : accessibilityServices) {
            Log.i("liao", "info : " + info.getId());
            if (info.getId().contains(getPackageName() + "/.weixin.WxService")) {
                return true;
            }
        }
        return false;
    }

}
