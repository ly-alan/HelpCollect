package come.help.collect.weixin;

import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

public class NodeUtils {

    private static final String TAG = "NodeUtils";

    /**
     * 获取其子节点中的信息
     *
     * @param info，跟节点
     * @param list，需要的节点信息
     * @param className    节点类名称
     */
    public static void recycle(AccessibilityNodeInfo info, List<AccessibilityNodeInfo> list, String className) {
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
    public static void showLogNodeInfo(AccessibilityNodeInfo info) {
        Log.i(TAG, "控件名称:" + info.getClassName());
        Log.i(TAG, "控件描速:" + info.getContentDescription());
        Log.i(TAG, "控件中的值：" + info.getText());
        Log.i(TAG, "控件的ID:" + info.getViewIdResourceName());
        Log.i(TAG, "visiable = " + info.isVisibleToUser());
    }
}
