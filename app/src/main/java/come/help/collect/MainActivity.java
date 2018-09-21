package come.help.collect;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

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
        startService(new Intent(this, WxService.class));
    }
}
