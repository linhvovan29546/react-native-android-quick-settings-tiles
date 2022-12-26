package com.reactnativeandroidquicksettingstiles;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import java.util.Locale;

public class ResultActivity extends AppCompatActivity {

  public static final String RESULT_ACTIVITY_INFO_KEY = "resultActivityInfo";
  public static final String RESULT_ACTIVITY_NAME_KEY = "resultActivityName";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_result);

    if (getIntent() != null) {
      Bundle extras = getIntent().getExtras();

      String tileState = extras.getString(RESULT_ACTIVITY_INFO_KEY);
      String tileName = extras.getString(RESULT_ACTIVITY_NAME_KEY);

      TextView outputText = findViewById(R.id.result_info);
      outputText.setText(getString(R.string.result_output, tileName, tileState));
      String packageName = this.getApplicationContext().getPackageName();
      Intent focusIntent = this.getPackageManager().getLaunchIntentForPackage(packageName).cloneFilter();
      TextView returnHome = findViewById(R.id.result_return_main);
      returnHome.setOnClickListener(view -> {
        Intent goHome = new Intent(focusIntent);
        startActivity(goHome);
      });
    }
  }
}
