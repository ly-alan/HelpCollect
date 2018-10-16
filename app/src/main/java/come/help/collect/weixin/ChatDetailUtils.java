package come.help.collect.weixin;

import android.graphics.Rect;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 聊天房间详情
 */
public class ChatDetailUtils {
    private static final String TAG = "liao";

    /**
     * @param rootInfo 跟节点
     */
    public static void saveChatDetailInfo(AccessibilityNodeInfo rootInfo) {
        String name = getChatRoomName(rootInfo);
        if (TextUtils.isEmpty(name)) {
            return;
        }
        AccessibilityNodeInfo chatNameNode = getChatNameNode(rootInfo, name);
        if (chatNameNode == null) {
            return;
        }
        AccessibilityNodeInfo listViewNode = getChatListView(chatNameNode);
        if (listViewNode == null) {
            return;
        }
        List<ChatRoom> msgList = getChatMessageList(listViewNode);
        Log.i("liao", "size = " + (msgList == null ? "null" : msgList.size()));
    }


    /**
     * 聊天列表
     *
     * @param chatNameNode 聊天窗口顶部的名称
     * @return
     */
    private static AccessibilityNodeInfo getChatListView(AccessibilityNodeInfo chatNameNode) {
        List<AccessibilityNodeInfo> ListViewNodeList = new ArrayList<>();
        //根据该节点逆推父节点下的其他
        while (chatNameNode != null && chatNameNode.getParent() != null) {
            for (int i = 0; i < chatNameNode.getParent().getChildCount(); i++) {
                if (chatNameNode.getParent().getChild(i) != chatNameNode) {
                    NodeUtils.recycle(chatNameNode.getParent().getChild(i), ListViewNodeList, "android.widget.ListView");
                    if (ListViewNodeList.size() > 0) {
                        return ListViewNodeList.get(0);
                    }
                }
            }
            chatNameNode = chatNameNode.getParent();
        }
        return null;
    }

    /**
     * 聊天界面标题
     *
     * @return
     */
    private static AccessibilityNodeInfo getChatNameNode(AccessibilityNodeInfo rootInfo, String name) {
        List<AccessibilityNodeInfo> nodeInfoList = rootInfo.findAccessibilityNodeInfosByText(name);
        if (nodeInfoList != null && nodeInfoList.size() > 0) {
            Rect bounds = new Rect();
            for (AccessibilityNodeInfo node : nodeInfoList) {
                node.getBoundsInScreen(bounds);
                if (bounds.left > 100 && bounds.bottom < 200) {
                    Log.d(TAG, bounds.toString());
                    //左上角房间名称
                    return node;
                }
            }
        }
        return null;
    }


    /**
     * 聊天房间名称，“当前所在页面，与**的聊天”。为最后一次打开的聊天窗口
     *
     * @param rootInfo
     * @return
     */
    private static String getChatRoomName(AccessibilityNodeInfo rootInfo) {
        String description = "";
        if (rootInfo != null && !TextUtils.isEmpty(rootInfo.getContentDescription())) {
            description = rootInfo.getContentDescription().toString();
            Log.d(TAG, "description = " + description);
            if (description.indexOf(",与") > 0) {
                description = description.substring(description.indexOf(",与") + 2, description.length() - 3);
                Log.d(TAG, "name = " + description);
            } else {
                return "";
            }
        }
        return description;
    }

    /**
     * 获取聊天信息
     *
     * @param node listView
     */
    private static List<ChatRoom> getChatMessageList(AccessibilityNodeInfo node) {
        if (node != null && node.getChildCount() > 0) {
            List<ChatRoom> list = new ArrayList<>();
            for (int i = 0; i < node.getChildCount(); i++) {
                ChatRoom room = nodeToBean(node.getChild(i));
                if (room != null && !room.isEmpty()) {
                    Log.i("liao", "msg = " + room.toString());
                    list.add(room);
                }
            }
            return list;
        }
        return null;
    }

    private static ChatRoom nodeToBean(AccessibilityNodeInfo node) {
        if (node == null) {
            return null;
        }
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setName(getPerson(node));
        chatRoom.setMessage(getMessage(node));
        return chatRoom;
    }

    /**
     * 消息发送人
     *
     * @param node
     * @return
     */
    private static String getPerson(AccessibilityNodeInfo node) {
        List<AccessibilityNodeInfo> list = new ArrayList<>();
        NodeUtils.recycle(node, list, "android.widget.ImageView");
        for (AccessibilityNodeInfo nodeInfo : list) {
            if (!TextUtils.isEmpty(nodeInfo.getContentDescription()) &&
                    nodeInfo.getContentDescription().toString().contains("头像")) {
                return nodeInfo.getContentDescription().toString();
            }
        }
        return "";
    }

    /**
     * 聊天窗口中的文字信息获取不到
     * @param node
     * @return
     */
    private static String getMessage(AccessibilityNodeInfo node) {
        List<AccessibilityNodeInfo> list = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        NodeUtils.recycle(node, list, "android.view.View");
        NodeUtils.recycle(node, list, "android.widget.TextView");
        for (AccessibilityNodeInfo nodeInfo : list) {
            sb.append(nodeInfo.getText());
        }
        return sb.toString();
    }


}
