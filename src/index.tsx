import { NativeEventEmitter, NativeModules, Platform } from 'react-native';
const LINKING_ERROR =
  `The package 'react-native-android-quick-settings-tiles' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo managed workflow\n';
const { AndroidQuickSettingsTiles } = NativeModules;
const quickSettings = AndroidQuickSettingsTiles
  ? AndroidQuickSettingsTiles
  : new Proxy(
    {},
    {
      get() {
        throw new Error(LINKING_ERROR);
      },
    }
  );
  const isAndroid = Platform.OS === 'android';
  let eventEmitter: any
  if (isAndroid) {
    eventEmitter = new NativeEventEmitter(AndroidQuickSettingsTiles);
  }
interface optionRequest{
  quickLabel:string;
  isDialog?:boolean;
  icon:string;
}
enum resultType{
  UNAVAILABLE="UNAVAILABLE",
  GRANTED="GRANTED"
}
type resultRequest={
  type:resultType;
  code?:number;
}
class RNQuickSettings {
  private _quickSettingsEventHandlers;
  constructor() {
    this._quickSettingsEventHandlers = new Map();
  }

  request = (option:optionRequest): Promise<resultRequest> => {
    return quickSettings.request(option)
  }
  
  addEventListener = (type: string, handler: any) => {
    if (!isAndroid) return
    let listener;
    if (type === 'onChange') {
      listener = eventEmitter.addListener(
        "onChange",
        (eventPayload: any) => {
          handler(eventPayload);
        }
      );
    } 
    else {
      return;
    }
    this._quickSettingsEventHandlers.set(type, listener);
  };
  removeEventListener = (type: any) => {
    if (!isAndroid) return
    const listener = this._quickSettingsEventHandlers.get(type);
    if (!listener) {
      return;
    }

    listener.remove();
    this._quickSettingsEventHandlers.delete(type);
  };
}
export default new RNQuickSettings();