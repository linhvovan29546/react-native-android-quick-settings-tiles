import { NativeModules, Platform } from 'react-native';
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
interface optionRequest{
  quickLabel:string;
  isDialog?:boolean;
  icon:string;
}
class RNQuickSettings {
  request = (option:optionRequest): Promise<any> => {
    console.log('start js native module0')
    return quickSettings.request(option)
  }
}
export default new RNQuickSettings();