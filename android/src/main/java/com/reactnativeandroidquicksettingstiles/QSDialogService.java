package com.reactnativeandroidquicksettingstiles;

import android.app.Service;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.IBinder;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;

@RequiresApi(api = Build.VERSION_CODES.N)
@SuppressLint("Override")
public class QSDialogService
  extends TileService {

  private boolean isTileActive;
  @Override
  public void onTileAdded() {
    super.onTileAdded();
    //set active when add tile
    Resources resources = getApplication().getResources();
    boolean initialValue=resources.getBoolean(R.bool.qs_dialog_default);
    Tile tile = super.getQsTile();
    int activeState = initialValue ?
      Tile.STATE_ACTIVE : Tile.STATE_INACTIVE;
    tile.setState(activeState);
    tile.updateTile();
  }
  @Override
  public void onClick(){
    // Get the tile's current state.
    Tile tile = null;
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
      tile = getQsTile();
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
      isTileActive = (tile.getState() == Tile.STATE_ACTIVE);
    }

    QSDialog.Builder dialogBuilder =
      new QSDialog.Builder(getApplicationContext());

    Tile finalTile = tile;
    QSDialog dialog = dialogBuilder
      .setClickListener(new QSDialog.QSDialogListener() {
        @Override
        public void onDialogPositiveClick(DialogFragment dialog) {
          Log.d("QS", "Positive registed");
          // The user wants to change the tile state.
          isTileActive = !isTileActive;
          updateTile();
          Intent intent=AndroidQuickSettingsTilesModule.convertTileToIntent(getApplicationContext(),finalTile,true);
          startActivityAndCollapse(intent);
        }

        @Override
        public void onDialogNegativeClick(DialogFragment dialog) {
          Log.d("QS", "Negative registered");

          // The user is cancelled the dialog box.
          // We can't do anything to the dialog box here,
          // but we can do any cleanup work.
        }
      })
      .create();

    // Pass the tile's current state to the dialog.
    Bundle args = new Bundle();
    args.putBoolean(QSDialog.TILE_STATE_KEY, isTileActive);

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
      this.showDialog(dialog.onCreateDialog(args));
    }
  }
  private void updateTile() {
    Tile tile = super.getQsTile();
    int activeState = isTileActive ?
      Tile.STATE_ACTIVE : Tile.STATE_INACTIVE;
    tile.setState(activeState);
    tile.updateTile();
  }
}
