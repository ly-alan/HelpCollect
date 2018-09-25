package come.help.collect.weixin;

import android.accessibilityservice.AccessibilityService;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.ArrayList;
import java.util.List;

public class WxService extends AccessibilityService {

    private static final String TAG = "liao";
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
            recycleAllInfo();
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
            saveChatRoomInfo(info);

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
     * 保存首页列表的值
     *
     * @param info
     */
    private void saveChatRoomInfo(AccessibilityNodeInfo info) {
//        AccessibilityNodeInfo listViewNode = getChatListView(info);
        List<ChatRoom> chatRoomList = getChatRoomInfo(getChatListView(info));
        if (chatRoomList != null) {
            Log.i("liao", "chatlist size = " + chatRoomList.size());
        }
    }

    /**
     * 聊天列表
     */
    private AccessibilityNodeInfo getChatListView(AccessibilityNodeInfo info) {
        if (info != null) {
            if (TextUtils.equals("com.tencent.mm.ui.mogic.WxViewPager", info.getClassName())) {
                Log.i("liao", "viewpage  = " + info.getChildCount());
                if (info.getChildCount() > 0) {
                    AccessibilityNodeInfo homeContentNode = info.getChild(0);
                    if (homeContentNode != null && homeContentNode.isVisibleToUser()) {
                        List<AccessibilityNodeInfo> listViews = new ArrayList<>();
                        recycle(homeContentNode, listViews, "android.widget.ListView");
                        if (listViews.size() == 1) {
                            return listViews.get(0);
                        }
                    }
                }
            }
        }
        return null;
    }


    /**
     * 获取聊天房间信息
     *
     * @param info
     * @return
     */
    private List<ChatRoom> getChatRoomInfo(AccessibilityNodeInfo info) {
        if (info != null && info.getChildCount() > 0) {
            Log.i("liao", "listViewNode  = " + info.getChildCount());
            showLogNodeInfo(info);
            //当前显示的是首页
            List<ChatRoom> list = new ArrayList<>();
            //获取首页的数据
            for (int i = 0; i < info.getChildCount(); i++) {
                if (info.getChild(i) != null && info.getChild(i).isVisibleToUser()) {
                    ChatRoom room = nodeToBean(info.getChild(i));
                    if (room != null && !room.isEmpty()) {
                        list.add(room);
                    }
                }
            }
            return list;
        }
        return null;
    }

    /**
     * listview item 转 bean
     *
     * @param info
     * @return
     */
    private ChatRoom nodeToBean(AccessibilityNodeInfo info) {
        if (info == null) {
            return null;
        }
        ChatRoom room = new ChatRoom();
        List<AccessibilityNodeInfo> list = new ArrayList<>();

        recycle(info, list, "android.view.View");

        room.setName(list.size() > 0 ? list.get(0).getText().toString() : "");
        room.setDate(list.size() > 1 ? list.get(1).getText().toString() : "");
        room.setMessage(list.size() > 2 ? list.get(2).getText().toString() : "");

        Log.i("liao", "room = " + room.toString());
        return room;
    }


    /**
     * 获取节点中的信息
     *
     * @param info，跟节点
     * @param list，需要的节点信息
     * @param className    节点类名称
     */
    private void recycle(AccessibilityNodeInfo info, List<AccessibilityNodeInfo> list, String className) {
        if (info != null) {
            if (TextUtils.equals(info.getClassName(), className)) {
                list.add(info);
            }
            if (info.getChildCount() > 0) {
                for (int i = 0; i < info.getChildCount(); i++) {
                    if (info.getChild(i) != null) {
                        recycle(info.getChild(i), list, className);
                    }
                }
            }
        }
    }

    /**
     * 显示节点信息
     *
     * @param info
     */
    private void showLogNodeInfo(AccessibilityNodeInfo info) {
        Log.i(TAG, "控件名称:" + info.getClassName());
        Log.i(TAG, "控件描速:" + info.getContentDescription());
        Log.i(TAG, "控件中的值：" + info.getText());
        Log.i(TAG, "控件的ID:" + info.getViewIdResourceName());
        Log.i(TAG, "visiable = " + info.isVisibleToUser());
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
