package come.help.collect;

import android.app.Application;
import android.content.Context;

public class HelpApplication extends Application {

    public static Context getInstence() {
        return instence;
    }

    private static HelpApplication instence;

    @Override
    public void onCreate() {
        super.onCreate();
        instence = this;
    }
}
