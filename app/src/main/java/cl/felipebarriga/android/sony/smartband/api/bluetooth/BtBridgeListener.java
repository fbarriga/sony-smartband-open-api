package cl.felipebarriga.android.sony.smartband.api.bluetooth;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;

import java.util.List;


/**
 * Copyright (c) 2015 Felipe Barriga Richards. See Copyright Notice in LICENSE file.
 */
public interface BtBridgeListener {
    void onCharacteristicRead( BluetoothGattCharacteristic characteristic );
    void onConnectionStateChange( BtBridge.Status status );
    void onCharacteristicChanged( BluetoothGattCharacteristic characteristic );
    void onCharacteristicWrite( BluetoothGattCharacteristic characteristic );
    void onDescriptorRead( BluetoothGattCharacteristic characteristic, BluetoothGattDescriptor descriptor );
    void onDescriptorWrite( BluetoothGattCharacteristic characteristic, BluetoothGattDescriptor descriptor );
    void onServicesDiscovered( List<BluetoothGattService> services );

}
