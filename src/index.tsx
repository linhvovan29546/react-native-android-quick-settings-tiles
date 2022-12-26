import { NativeModules } from 'react-native';

type AndroidQuickSettingsTilesType = {
  multiply(a: number, b: number): Promise<number>;
};

const { AndroidQuickSettingsTiles } = NativeModules;

export default AndroidQuickSettingsTiles as AndroidQuickSettingsTilesType;
