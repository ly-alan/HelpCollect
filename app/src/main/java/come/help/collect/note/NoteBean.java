package come.help.collect.note;

import com.business.utillibrary.util.TimeUtils;

public class NoteBean {
    long date;
    String address;
    String message;

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "NoteBean{" +
                "date=" + TimeUtils.millis2String(date) +
                ", address='" + address + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
