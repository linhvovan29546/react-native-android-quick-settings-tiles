package com.reactnativeandroidquicksettingstiles;

import android.annotation.SuppressLint;
import android.app.StatusBarManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.IconCompat;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.module.annotations.ReactModule;
import com.google.common.util.concurrent.MoreExecutors;

@ReactModule(name = AndroidQuickSettingsTilesModule.NAME)
public class AndroidQuickSettingsTilesModule extends ReactContextBaseJavaModule {
    public static final String NAME = "AndroidQuickSettingsTiles";
    public static ReactApplicationContext context;
    public AndroidQuickSettingsTilesModule(ReactApplicationContext reactContext) {
        super(reactContext);
        context = reactContext;
    }
    private Context getAppContext() {
      return this.context.getApplicationContext();
    }
    @Override
    @NonNull
    public String getName() {
        return NAME;
    }


    // Example method
    // See https://reactnative.dev/docs/native-modules-android
    @SuppressLint("LongLogTag")
    @ReactMethod
    public void requestPermission(Promise promise) {
      Log.d(NAME, "start native");
      WritableMap params = Arguments.createMap();
      StatusBarManager statusBarService = null;
      Log.d(NAME, "11111");
      if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
        Context context=getAppContext();
        statusBarService = context.getSystemService(StatusBarManager.class);
        if (Build.VERSION.SDK_INT == 33) {
          ComponentName componentName = new ComponentName(
            context,
            "Test App");
          IconCompat icon =
            IconCompat.createWithResource(context,
              this.getResourceIdForResourceName(context,"ic_launcher_round"));
          statusBarService.requestAddTileService(
            componentName, "Quick Settings",icon.toIcon(context) ,
            MoreExecutors.directExecutor(), integer -> {
              params.putInt("integer",integer);
              promise.resolve(params);
            });
        } else {
          params.putString("message","Request to add tile for user is not supported");
          promise.reject("error",params);
        }
      }else{
        params.putString("message","Request to add tile for user is not supported");
        promise.reject("error",params);
      }


    }

  private int getResourceIdForResourceName(Context context, String resourceName) {
    int resourceId = context.getResources().getIdentifier(resourceName, "drawable", context.getPackageName());
    if (resourceId == 0) {
      resourceId = context.getResources().getIdentifier(resourceName, "mipmap", context.getPackageName());
    }
    return resourceId;
  }
}
