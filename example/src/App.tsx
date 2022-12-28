import * as React from 'react';

import { StyleSheet, View, Text, TouchableOpacity } from 'react-native';
import RNQuickSettings from '../../src/index';

export default function App() {
  React.useEffect(()=>{
    RNQuickSettings.addEventListener("onChange",(payload)=>{
    })
  },[])
  return (
    <View style={styles.container}>
      <TouchableOpacity onPress={async ()=>{
          const result=await RNQuickSettings.request({
            isDialog:true,
            quickLabel:"QS Dialog Launcher",
            icon:"more"
          })
          console.log('result',result)
      }}>
      <Text>Request QS Dialog Launcher</Text>
      </TouchableOpacity>
      <TouchableOpacity onPress={async ()=>{
          const result=await RNQuickSettings.request({
            isDialog:false,
            quickLabel:"QS Intent Launcher",
            icon:"other"
          })
          console.log('result',result)
      }}>
      <Text>Request QS Intent Launcher</Text>
      </TouchableOpacity>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  box: {
    width: 60,
    height: 60,
    marginVertical: 20,
  },
});
