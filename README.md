# react-native-android-quick-settings-tiles
# Library only work for android 13 or latest
Create custom Quick Settings tiles for your app [info](https://developer.android.com/develop/ui/views/quicksettings-tiles)

## Screenshot

<table>
  <tr>
    <td><p align="center"><img src="/docs/1.JPEG" height="500"></p></td>
 <td><p align="center"><img src="/docs/2.JPEG" height="500"></p></td>
  <td><p align="center"><img src="/docs/3.JPEG" height="500"></p></td>

  </tr>
</table>
<p align="center" >
  <kbd>
    <img
      src="./docs/intent.gif"
      title="Intent Demo"
      float="left"
    width="350" height="700"
    >
  </kbd>
  <kbd>
    <img
      src="/docs/dialog.gif"
      title="Dialog Demo"
      float="left"
       width="350" height="700"
    >
  </kbd>
  <br>
</p>

## Installation

```sh
npm install react-native-android-quick-settings-tiles
```
### Addition installation step
In `MainActivity.java`
```java
  @Override
  protected void onStart() {
    super.onStart();
    AndroidQuickSettingsTilesModule.startSession(getIntent()); //add line
  }
  @Override
  public void onNewIntent(Intent intent) {
    super.onNewIntent(intent);
    AndroidQuickSettingsTilesModule.onNewIntent(intent);  //add line
  }
```
In `AndroidManifest.xml`:
```java
// ...
  <!-- TileService for "Launch a dialog from a tile" section -->
      <service
        android:name="com.reactnativeandroidquicksettingstiles.QSDialogService"
        android:icon="@drawable/more"
        android:label="@string/qs_dialog_tile_label"
        android:exported="true"
        android:permission="android.permission.BIND_QUICK_SETTINGS_TILE">
        <intent-filter>
          <action android:name="android.service.quicksettings.action.QS_TILE" />
        </intent-filter>
      </service>
      <!-- TileService for "Launch an activity from a tile" section -->
      <service
        android:name="com.reactnativeandroidquicksettingstiles.QSIntentService"
         android:icon="@drawable/other"
        android:label="@string/qs_intent_tile_label"
        android:permission="android.permission.BIND_QUICK_SETTINGS_TILE">
        <intent-filter>
          <action android:name="android.service.quicksettings.action.QS_TILE" />
        </intent-filter>
      </service>
     .....
      </application>
```
In `res/values/strings`:(optional)

```java
    <string name="app_name">AndroidQuickSettingsTiles Example</string>
  <string name="tile_label">Quick Settings Sample</string>

  <string name="qs_dialog_tile_label">QS Dialog Launcher</string>
  <string name="qs_dialog_prompt">Change the tile state?</string>
  <string name="qs_dialog_active">Turn off</string>
  <string name="qs_dialog_inactive">Turn on</string>
  <string name="qs_dialog_cancel">Cancel</string>

  <string name="qs_intent_tile_label">QS Intent Launcher</string>
<!--  value init  -->
  <bool name="qs_dialog_default">true</bool>
  <bool name="qs_intent_default">true</bool>
```

In `res/values/color`: (optional)
```java
  <color name="colorPrimaryDialog">#0F9D58</color>
  <color name="colorPrimaryDarkDialog">#0B8043</color>
```
In `res/values/styles`:(optional)
```java
 <style name="HeadingFont" parent="@android:style/TextAppearance.Medium">
    <item name="android:layout_width">match_parent</item>
    <item name="android:layout_height">wrap_content</item>
    <item name="android:textSize">24sp</item>
    <item name="android:layout_marginTop">16dp</item>
    <item name="android:paddingLeft">16dp</item>
    <item name="android:textColor">#FFFFFF</item>
    <item name="android:layout_gravity">center</item>
  </style>

  <style name="BodyFont" parent="@style/HeadingFont">
    <item name="android:textSize">18sp</item>
    <item name="android:layout_marginTop">8sp</item>
    <item name="android:textColor">@color/colorPrimaryDark</item>
  </style>
```
## Usage

```js
import RNQuickSettings from "react-native-android-quick-settings-";

  React.useEffect(()=>{
    const get=async ()=>{
     const data=await RNQuickSettings.getLastChanged()
     console.log('data',data)
     if(data){
      Alert.alert("Alert","Get latest")
     }
     RNQuickSettings.addEventListener("onChange",(payload)=>{
      console.log('payload',payload)
      Alert.alert("Alert","Tile changed")
    })
    }
    get()
  },[])

```
## Request to add tile
```ts
function request(option:optionRequest): Promise<resultRequest>;
```
```js
        const result=await RNQuickSettings.request({
          isDialog:true,
          quickLabel:"QS Dialog Launcher", //same with label in manifest
          icon:"more"
        })
```
## getLastChanged 
   When change a tile from quicksettings has triggered the application to open from a quit state,
     * this method will return a `resultChanged` containing the tile data, or `null` if
     * the app was opened via another method.
```ts
function getLastChanged(option:optionRequest): Promise<resultChanged>;
```
```js
   const data=await RNQuickSettings.getLastChanged()
```

## event listener
```ts
function addEventListener(option:optionRequest,handler(payload:resultChanged): =>void);
```
```js
    RNQuickSettings.addEventListener("onChange",(payload)=>{
    })
```
### resultRequest type
```ts
type resultRequest={
  type:"UNAVAILABLE"|"GRANTED";
  code?:number;
}
```
| type value          | Notes                                                             |
| --------------------- | ----------------------------------------------------------------- |
| `UNAVAILABLE` | This feature is not available (only work on android 13 and later)  |
| `GRANTED`      |   The request is successfully    |

| code value          | Notes                                                             |
| --------------------- | ----------------------------------------------------------------- |
| 0 |  |Quick setting is denied
| 1 |  |Quick setting already exists
| 2 |  |Add Quick setting success

### optionRequest type
```ts
interface optionRequest{
  quickLabel:string;
  isDialog?:boolean; // default is false
  icon:string;
}
```
- quickLabel: label of the tile to show to the user. This value cannot be null.
- icon: to use in the tile shown to the user. This value cannot be null.
- isDialog: flag to display dialog when change status of tile


## Troubleshooting
- [How do I convert pngs directly to android vector drawables?](https://stackoverflow.com/questions/52670937/how-do-i-convert-pngs-directly-to-android-vector-drawables)
## Contributing

See the [contributing guide](CONTRIBUTING.md) to learn how to contribute to the repository and the development workflow.

## License

MIT

---

Made with [create-react-native-library](https://github.com/callstack/react-native-builder-bob)
