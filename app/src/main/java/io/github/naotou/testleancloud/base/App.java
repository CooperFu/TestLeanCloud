package io.github.naotou.testleancloud.base;

import android.app.Application;
import com.avos.avoscloud.AVOSCloud;

/**
 * Create by Android Studio
 * User: FuQiang(fuqiang@uniqueway.com)
 * Date: 2016-01-07
 * Time: 12-10
 */
public class App extends Application {
    @Override
    public void onCreate() {
        AVOSCloud.initialize(this, "6GyWqXLVxewcfydkTFn8MIG5-gzGzoHsz", "lR945peYEb9k8Bf6o1wzwQYx");
    }
}
