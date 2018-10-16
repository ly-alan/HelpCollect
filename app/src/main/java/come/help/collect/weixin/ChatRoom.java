package come.help.collect.weixin;

import android.text.TextUtils;

/**
 * 聊天房间信息
 */
public class ChatRoom {
    /**
     * 房间名
     */
    String name;
    /**
     * 最新消息
     */
    String message;
    /**
     * 时间
     */
    String date;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isEmpty() {
        if (TextUtils.isEmpty(name) && TextUtils.isEmpty(message)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof ChatRoom) {
            ChatRoom room = (ChatRoom) obj;
            if (TextUtils.equals(name, room.getName()) && TextUtils.equals(message, room.getMessage())) {
                return true;
            }
        }
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return "ChatRoom{" +
                "name='" + name + '\'' +
                ", date='" + date + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
