package com.srinnix.kindergarten;

import android.app.Application;

import com.srinnix.kindergarten.constant.AppConstant;
import com.srinnix.kindergarten.util.SharedPreUtils;
import com.srinnix.kindergarten.util.SocketUtil;

import io.realm.Realm;
import io.realm.RealmConfiguration;


/**
 * Created by anhtu on 2/17/2017.
 */

public class KinderApplication extends Application {

    public static final String TAG = "KinderApplication";

    private static KinderApplication mInstance;
    private SocketUtil socketUtil;
    private Realm realm;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        connect();
    }

    private void connect() {
        if (SharedPreUtils.getInstance(this).isUserSignedIn()) {
            getSocketUtil().connect(this);
        }
    }

    public static synchronized KinderApplication getInstance() {
        return mInstance;
    }

    public SocketUtil getSocketUtil() {
        if (socketUtil == null) {
            socketUtil = new SocketUtil();
        }
        return socketUtil;
    }

    public Realm getRealm() {
        if (realm == null) {
            Realm.init(this);
            RealmConfiguration config2 = new RealmConfiguration.Builder()
                    .name(AppConstant.APP_NAME)
                    .schemaVersion(3)
                    .deleteRealmIfMigrationNeeded()
                    .build();
            realm = Realm.getInstance(config2);
        }
        return realm;
    }
}
