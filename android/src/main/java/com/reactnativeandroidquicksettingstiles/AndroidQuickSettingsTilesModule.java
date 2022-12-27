package com.reactnativeandroidquicksettingstiles;

import android.annotation.SuppressLint;
import android.app.StatusBarManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.service.quicksettings.Tile;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.graphics.drawable.IconCompat;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.module.annotations.ReactModule;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.google.common.util.concurrent.MoreExecutors;

import javax.annotation.Nonnull;

@ReactModule(name = AndroidQuickSettingsTilesModule.NAME)
public class AndroidQuickSettingsTilesModule extends ReactContextBaseJavaModule {
    public static final String NAME = "AndroidQuickSettingsTiles";
    public static final String RESULT_ACTIVITY_INFO_KEY = "resultActivityInfo";
    public static final String RESULT_ACTIVITY_NAME_KEY = "resultActivityName";
    private static Bundle bundleData=null;
  public static ReactApplicationContext context;
    public AndroidQuickSettingsTilesModule(ReactApplicationContext reactContext) {
        super(reactContext);
        context = reactContext;
    }
    private static Context getAppContext() {
      return context.getApplicationContext();
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
  @ReactMethod
  public void getLastChanged(Promise promise) {
    WritableMap params = Arguments.createMap();
      if(bundleData!= null){
        params.putString(AndroidQuickSettingsTilesModule.RESULT_ACTIVITY_NAME_KEY, bundleData.getString(AndroidQuickSettingsTilesModule.RESULT_ACTIVITY_NAME_KEY,""));
        params.putString(AndroidQuickSettingsTilesModule.RESULT_ACTIVITY_INFO_KEY,  bundleData.getString(AndroidQuickSettingsTilesModule.RESULT_ACTIVITY_INFO_KEY,""));
        params.putBoolean("isDialog", bundleData.getBoolean("isDialog",false));
      }
    promise.resolve(params);
  }
  private int getResourceIdForResourceName(Context context, String resourceName) {
    int resourceId = context.getResources().getIdentifier(resourceName, "drawable", context.getPackageName());
    if (resourceId == 0) {
      resourceId = context.getResources().getIdentifier(resourceName, "mipmap", context.getPackageName());
    }
    return resourceId;
  }
  public static void onNewIntent(@Nonnull Intent intent) {
    Bundle bundle = intent.getExtras();
    bundleData=bundle;
    WritableMap params = Arguments.createMap();
    if(bundleData!= null){
      params.putString(AndroidQuickSettingsTilesModule.RESULT_ACTIVITY_NAME_KEY, bundleData.getString(AndroidQuickSettingsTilesModule.RESULT_ACTIVITY_NAME_KEY,""));
      params.putString(AndroidQuickSettingsTilesModule.RESULT_ACTIVITY_INFO_KEY,  bundleData.getString(AndroidQuickSettingsTilesModule.RESULT_ACTIVITY_INFO_KEY,""));
      params.putBoolean("isDialog", bundleData.getBoolean("isDialog",false));
    }
    sendEventToJs("onChange",params);
  }
  @RequiresApi(api = Build.VERSION_CODES.N)
  public static Intent convertTileToIntent(Tile tile,boolean isDialog){
    Context context=getAppContext();
    Resources resources = context.getResources();
    String tileLabel = tile.getLabel().toString();
    String tileState = null;
      if(tile.getState() == Tile.STATE_ACTIVE){
        tileState=resources.getString(R.string.service_active);
      }else{
        tileState=  resources.getString(R.string.service_inactive);
      }
    String packageName = context.getPackageName();
    Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName).cloneFilter();
    intent.putExtra(AndroidQuickSettingsTilesModule.RESULT_ACTIVITY_NAME_KEY,
      tileLabel);
    intent.putExtra(AndroidQuickSettingsTilesModule.RESULT_ACTIVITY_INFO_KEY,
      tileState);
    intent.putExtra("isDialog",
      isDialog);
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    return intent;
  }

  @ReactMethod
  public static void sendEventToJs(String eventName,@Nullable WritableMap params) {
    context.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(eventName, params);
  }
  @ReactMethod
  public void addListener(String eventName) {
    // Keep: Required for RN built in Event Emitter Calls.
  }

  @ReactMethod
  public void removeListeners(Integer count) {
    // Keep: Required for RN built in Event Emitter Calls.
  }
}
