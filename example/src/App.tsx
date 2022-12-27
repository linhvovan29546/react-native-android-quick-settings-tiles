import * as React from 'react';

import { StyleSheet, View, Text, TouchableOpacity } from 'react-native';
import RNQuickSettings from '../../src/index';

export default function App() {
  React.useEffect(()=>{
    RNQuickSettings.addEventListener("onChange",(s)=>{
    console.log('ssssssss',s)
    })
  },[])
  return (
    <View style={styles.container}>
      <TouchableOpacity onPress={async ()=>{
        const result=await RNQuickSettings.request({
          isDialog:true,
          quickLabel:"QuicktestLabel",
          icon:"ic_launcher_round"
        })
        console.log('result',result)
      }}>
      <Text>Request</Text>
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
