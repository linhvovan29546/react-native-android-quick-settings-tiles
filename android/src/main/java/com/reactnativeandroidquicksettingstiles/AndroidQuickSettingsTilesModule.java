package com.reactnativeandroidquicksettingstiles;

import android.annotation.SuppressLint;
import android.app.StatusBarManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.IconCompat;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.module.annotations.ReactModule;
import com.google.common.util.concurrent.MoreExecutors;

import javax.annotation.Nonnull;

@ReactModule(name = AndroidQuickSettingsTilesModule.NAME)
public class AndroidQuickSettingsTilesModule extends ReactContextBaseJavaModule {
    public static final String NAME = "AndroidQuickSettingsTiles";
    public static final String RESULT_ACTIVITY_INFO_KEY = "resultActivityInfo";
    public static final String RESULT_ACTIVITY_NAME_KEY = "resultActivityName";
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
    public void request(ReadableMap options, Promise promise) {
      WritableMap params = Arguments.createMap();
      String quickLabel=options.getString("quickLabel");
      String iconName=options.getString("icon");
      boolean isDialog=false;
      if(options.hasKey("isDialog")){
        isDialog=options.getBoolean("isDialog");
      }
      StatusBarManager statusBarService = null;
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.TIRAMISU) {
          Context context=getAppContext();
          statusBarService = context.getSystemService(StatusBarManager.class);
          ComponentName componentName = new ComponentName(
            context,
            isDialog ? QSDialogService.class.getName(): QSIntentService.class.getName());
          IconCompat icon =
            IconCompat.createWithResource(context,
              this.getResourceIdForResourceName(context,iconName));
          statusBarService.requestAddTileService(
            componentName, quickLabel,icon.toIcon(context),
            MoreExecutors.directExecutor(), integer -> {
              params.putInt("integer",integer);
              promise.resolve(params);
            });
        } else {
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
  public static void onNewIntent(@Nonnull Intent intent) {

  }
}
