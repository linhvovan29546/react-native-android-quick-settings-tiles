package com.reactnativeandroidquicksettingstiles;

import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;

import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.N)
@SuppressLint("Override")
public class QSIntentService extends TileService {
  // Called when the user adds your tile.
  @Override
  public void onTileAdded() {
    super.onTileAdded();
    //set active when add tile
    Resources resources = getApplication().getResources();
    boolean initialValue=resources.getBoolean(R.bool.qs_intent_default);
    updateTile(initialValue);
  }

  // Called when your app can update your tile.
  @Override
  public void onStartListening() {
    super.onStartListening();
  }

  // Called when your app can no longer update your tile.
  @Override
  public void onStopListening() {
    super.onStopListening();
  }

  // Called when the user taps on your tile in an active or inactive state.
  @Override
  public void onTileRemoved() {
    super.onTileRemoved();
  }
  @Override
  public void onClick() {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {return;}
    // Check to see if the device is currently locked.
    boolean isCurrentlyLocked = false;
      isCurrentlyLocked = this.isLocked();
    if (!isCurrentlyLocked) {
      Tile  tile = getQsTile();
        if(tile.getState() == Tile.STATE_ACTIVE){
          updateTile(false);
        }else{
          updateTile(true);
        }
      Intent intent=AndroidQuickSettingsTilesModule.convertTileToIntent(getApplicationContext(),tile,false);
      startActivityAndCollapse(intent);
    }
  }
  private void updateTile(boolean isActive) {
    Tile tile = super.getQsTile();
    int activeState = isActive ?
      Tile.STATE_ACTIVE : Tile.STATE_INACTIVE;
    tile.setState(activeState);
    tile.updateTile();
  }
}
