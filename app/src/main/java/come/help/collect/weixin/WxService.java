package come.help.collect.weixin;

import android.accessibilityservice.AccessibilityService;
import android.graphics.Rect;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

public class WxService extends AccessibilityService {

    private static final String TAG = "liao";
    //测试的微信版本
    private static final String TEST_WX_VERSION = "6.6.7";

    /**
     * 时间间隔,每5s记录页面数据
     */
    private static final int TIME_DISTENCE = 3000;
    private long last_collect_msg_time;
    //用于获取空间位置
    Rect mRect = new Rect();

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        Log.e("liao", "触发回调");
        if (System.currentTimeMillis() - last_collect_msg_time > TIME_DISTENCE) {
            last_collect_msg_time = System.currentTimeMillis();
//            recycleAllInfo();
            ChatRoomListUtils.saveChatRoomInfo(getRootInActiveWindow());
            ChatDetailUtils.saveChatDetailInfo(getRootInActiveWindow());
        }
    }

    @Override
    public void onInterrupt() {

    }


    /**
     * 获取页面所有的信息
     */
    private void recycleAllInfo() {
        recycle(getRootInActiveWindow());
//        getNodeById(getRootInActiveWindow(), "com.tencent.mm:id/as6");
    }

    public void recycle(AccessibilityNodeInfo info) {
        if (info != null) {

            //聊天窗口信息
            ChatRoomListUtils.saveChatRoomInfo(info);
            NodeUtils.showLogNodeInfo(info);

            if (info.getChildCount() == 0) {
//                showLogNodeInfo(info);
            } else {
//                showLogNodeInfo(info);
                for (int i = 0; i < info.getChildCount(); i++) {
                    if (info.getChild(i) != null) {
                        recycle(info.getChild(i));
                    }
                }
            }
        }
    }


    /**
     * 根据id获取节点
     *
     * @param info
     * @param id
     * @return
     */
    private List<AccessibilityNodeInfo> getNodeById(AccessibilityNodeInfo info, String id) {
        if (info != null) {
            return info.findAccessibilityNodeInfosByViewId(id);
        }
        return null;

    }


}
