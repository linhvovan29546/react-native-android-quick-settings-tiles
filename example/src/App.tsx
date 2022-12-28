import * as React from 'react';

import { StyleSheet, View, Text, TouchableOpacity, Alert } from 'react-native';
import RNQuickSettings from '../../src/index';

export default function App() {
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
