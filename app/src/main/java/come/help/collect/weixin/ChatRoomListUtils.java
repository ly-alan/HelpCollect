package come.help.collect.weixin;

import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 聊天房间信息梳理
 */
public class ChatRoomListUtils {

    private static final String TAG = "liao";

    /**
     * 保存首页列表的值
     *
     * @param info
     */
    public static void saveChatRoomInfo(AccessibilityNodeInfo info) {
//        AccessibilityNodeInfo listViewNode = getChatListView(info);
        List<ChatRoom> chatRoomList = getChatRoomInfo(getChatListView(info));
        if (chatRoomList != null) {
            Log.i("liao", "chatlist size = " + chatRoomList.size());
        }
    }

    /**
     * 聊天列表
     */
    private static AccessibilityNodeInfo getChatListView(AccessibilityNodeInfo info) {
        if (info != null) {
            List<AccessibilityNodeInfo> viewPageNodeList = new ArrayList<>();
            NodeUtils.recycle(info, viewPageNodeList, "com.tencent.mm.ui.mogic.WxViewPager");
            if (viewPageNodeList.size() > 0) {
                Log.i("liao", "viewpage  = " + info.getChildCount());
                //获取首页viewpage
                AccessibilityNodeInfo viewPageNode = viewPageNodeList.get(0);
                if (viewPageNode.getChildCount() > 0) {
                    //聊天列表位于第一个
                    AccessibilityNodeInfo homeContentNode = viewPageNode.getChild(0);
                    if (homeContentNode != null && homeContentNode.isVisibleToUser()) {
                        List<AccessibilityNodeInfo> listViews = new ArrayList<>();
                        NodeUtils.recycle(homeContentNode, listViews, "android.widget.ListView");
                        if (listViews.size() == 1) {
                            return listViews.get(0);
                        }
                    }
                }
            }
//            if (TextUtils.equals("com.tencent.mm.ui.mogic.WxViewPager", info.getClassName())) {
//                Log.i("liao", "viewpage  = " + info.getChildCount());
//                if (info.getChildCount() > 0) {
//                    AccessibilityNodeInfo homeContentNode = info.getChild(0);
//                    if (homeContentNode != null && homeContentNode.isVisibleToUser()) {
//                        List<AccessibilityNodeInfo> listViews = new ArrayList<>();
//                        NodeUtils.recycle(homeContentNode, listViews, "android.widget.ListView");
//                        if (listViews.size() == 1) {
//                            return listViews.get(0);
//                        }
//                    }
//                }
//            }
        }
        return null;
    }


    /**
     * 获取聊天房间信息
     *
     * @param info
     * @return
     */
    private static List<ChatRoom> getChatRoomInfo(AccessibilityNodeInfo info) {
        if (info != null && info.getChildCount() > 0) {
            Log.i("liao", "listViewNode  = " + info.getChildCount());
//            NodeUtils.showLogNodeInfo(info);
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
    private static ChatRoom nodeToBean(AccessibilityNodeInfo info) {
        if (info == null) {
            return null;
        }
        ChatRoom room = new ChatRoom();
        List<AccessibilityNodeInfo> list = new ArrayList<>();

        NodeUtils.recycle(info, list, "android.view.View");

        room.setName(list.size() > 0 ? list.get(0).getText().toString() : "");
        room.setDate(list.size() > 1 ? list.get(1).getText().toString() : "");
        room.setMessage(list.size() > 2 ? list.get(2).getText().toString() : "");

        Log.i("liao", "room = " + room.toString());
        return room;
    }

}
