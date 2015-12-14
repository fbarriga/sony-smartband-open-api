package cl.felipebarriga.android.sony.smartband.api.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import cl.felipebarriga.android.sony.smartband.api.bluetooth.profiles.BaseProfile;

/**
 * Copyright (c) 2015 Felipe Barriga Richards. See Copyright Notice in LICENSE file.
 */

// TODO: how to handle when status != BluetoothGatt.GATT_SUCCESS
public class BtBridge extends BluetoothGattCallback {
    public final static String[] COMPATIBLE_DEVICES = { "SWR10" };
    public final String CLASS = getClass().getSimpleName();

    public enum Status {
        CONNECTED,
        DISCONNECTED,
        CONNECTING
    }

    private Context mContext;
    ArrayList<BtBridgeListener> mListeners = new ArrayList<>();

    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothGatt mGatt;

    private Status mStatus;

    private ArrayList<BluetoothDevice> mDevices = new ArrayList<>();

    BtBridge( Context context ) {
        mContext = context;
        BluetoothManager btManager = ( BluetoothManager ) mContext.getSystemService( Context.BLUETOOTH_SERVICE );
        mBluetoothAdapter = btManager.getAdapter();
        mStatus = Status.DISCONNECTED;
    }

    public Status getStatus() {
        return mStatus;
    }

    public void addListener( BtBridgeListener listener ) {
        if( mListeners.contains( listener ) ) {
            Log.w( CLASS, "addListener: Listener already registered" );
        } else {
            mListeners.add( listener );
        }
    }

    public boolean isDeviceCompatible( String deviceName ) {
        for( String device : COMPATIBLE_DEVICES ) {
            if( device.equals( deviceName ) ) {
                return true;
            }
        }
        return false;
    }

    public boolean connect() {
        Log.d( CLASS, "connect: called." );

        if( !mStatus.equals( Status.DISCONNECTED ) ) {
            Log.e( CLASS, "connect: Invalid status=" + mStatus.name() );
            return false;
        }

        populatePairedDevices();
        BluetoothDevice btDevice = getDevice();
        if( btDevice == null ) {
            Log.e( CLASS, "connect: Device not found." );
            return false;
        }

        mStatus = Status.CONNECTING;
        btDevice.connectGatt( mContext, true, this );
        return true;
    }


    public boolean disconnect() {
        Log.d( CLASS, "disconnect: called." );
        if( !mStatus.equals( Status.CONNECTED ) ) {
            Log.e( CLASS, "disconnect: Not connected: status=" + mStatus.name() );
            return false;
        }

        if( mGatt == null ) {
            Log.e( CLASS, "disconnect: mGatt not found" );
            return false;
        }

        Log.i( CLASS, "disconnect: disconnected." );
        mGatt.disconnect();
        return true;
    }

    public boolean isGattReady() {
        if( !mStatus.equals( Status.CONNECTED ) ) {
            Log.e( CLASS, "isGattReady: Not connected: status=" + mStatus.name() );
            return false;
        }

        if( mGatt == null ) {
            Log.e( CLASS, "isGattReady: mGatt not found" );
            return false;
        }

        return true;
    }

    public boolean discoverServices() {
        Log.d( CLASS, "discoverServices: called." );
        if( !isGattReady() ) {
            Log.e( CLASS, "discoverServices: Gatt not ready" );
            return false;
        }

        return true;
    }

    private BluetoothDevice getDevice() {
        for( BluetoothDevice btdev : mDevices ) {
            if( isDeviceCompatible( btdev.getName() ) ) {
                Log.d( CLASS, "getDevice: Device found: addr=" + btdev.getAddress() );
                return btdev;
            }
        }

        Log.w( CLASS, "getDevice: device not found." );
        return null;
    }

    private void populatePairedDevices() {
        Log.d( CLASS, "populatePairedDevices: called." );
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

        mDevices.clear();
        if( pairedDevices != null && pairedDevices.size() > 0 ) {
            mDevices.addAll( pairedDevices );
        }
        printPairedDevices();
    }

    private void printPairedDevices() {
        Log.i( CLASS, "printPairedDevices: mDevices=" + mDevices.size() );
        for( BluetoothDevice btdev : mDevices ) {
            Log.i( CLASS, "printPairedDevices: addr=" + btdev.getAddress() + " name=" + btdev.getName() );
        }
    }

    public BluetoothGattCharacteristic getCharacteristic( UUID serviceUuid, UUID characteristicUuid ) {
        if( !isGattReady() ) {
            Log.e( CLASS, "getCharacteristic: Gatt not ready" );
            return null;
        }

        BaseProfile serviceProfile = Profiles.getService( serviceUuid );
        if( serviceProfile == null ) {
            Log.e( CLASS, "getCharacteristic: serviceProfile not found. serviceUuid=" + serviceUuid );
        }

        BluetoothGattService service = mGatt.getService( serviceUuid );
        if( service == null ) {
            Log.e( CLASS, "getCharacteristic: service not found (on gatt). serviceUuid=" + serviceUuid );
            return null;
        }

        BluetoothGattCharacteristic characteristic = service.getCharacteristic( characteristicUuid );
        if( characteristic == null ) {
            Log.e( CLASS, "getCharacteristic: characteristic not found. uuid=" + characteristicUuid );
            return null;
        }

        return characteristic;
    }

    public boolean readCharacteristic( BluetoothGattCharacteristic characteristic ) {
        if( characteristic == null ) {
            Log.e( CLASS, "readCharacteristic: characteristic=null" );
            return false;
        }

        if( !isGattReady() ) {
            Log.e( CLASS, "readCharacteristic: Gatt not ready" );
            return false;
        }

        return mGatt.readCharacteristic( characteristic );
    }

    public boolean writeCharacteristic( BluetoothGattCharacteristic characteristic ) {
        if( !isGattReady() ) {
            Log.e( CLASS, "writeCharacteristic: Gatt not ready" );
            return false;
        }

        return mGatt.writeCharacteristic( characteristic );
    }

    public boolean writeDescriptor( BluetoothGattDescriptor descriptor ) {
        if( !isGattReady() ) {
            Log.e( CLASS, "writeDescriptor: Gatt not ready" );
            return false;
        }

        return mGatt.writeDescriptor( descriptor );
    }

    public boolean setCharacteristicNotification( BluetoothGattCharacteristic characteristic, boolean enabled ) {
        if( !isGattReady() ) {
            Log.e( CLASS, "setCharacteristicNotification: Gatt not ready" );
            return false;
        }

        return mGatt.setCharacteristicNotification( characteristic, enabled );
    }

    @Override
    public void onConnectionStateChange( BluetoothGatt gatt, int status, int newState ) {
        if( status != BluetoothGatt.GATT_SUCCESS ) {
            Log.e( CLASS, "onConnectionStateChange: Status != SUCCESS: status=" + status );
            return;
        }

        Log.d( CLASS, "onConnectionStateChange: Status=" + status );

        switch( newState ) {
            case BluetoothProfile.STATE_CONNECTED:
                mStatus = Status.CONNECTED;
                mGatt = gatt;
                Log.d( CLASS, "onConnectionStateChange: newState=STATE_CONNECTED" );
                mGatt.discoverServices();
                break;
            case BluetoothProfile.STATE_DISCONNECTED:
                mStatus = Status.DISCONNECTED;
                Log.d( CLASS, "onConnectionStateChange: newState=STATE_DISCONNECTED" );
                Log.e( "gattCallback", "STATE_DISCONNECTED" );
                break;
            default:
                Log.w( CLASS, "onConnectionStateChange: newState=" + newState );
        }

        for( BtBridgeListener listener : mListeners ) {
            listener.onConnectionStateChange( mStatus );
        }
    }

    @Override
    // TODO: maybe here we should set status == CONNECTED
    public void onServicesDiscovered( BluetoothGatt gatt, int status ) {
        if( status != BluetoothGatt.GATT_SUCCESS ) {
            Log.e( CLASS, "onServicesDiscovered: Status != SUCCESS: status=" + status );
            return;
        }
        mGatt = gatt;

        List<BluetoothGattService> services = gatt.getServices();
        Log.d( CLASS, "onServicesDiscovered: services=" + services.size() );

        for( BtBridgeListener listener : mListeners ) {
            listener.onServicesDiscovered( services );
        }
    }

    @Override
    public void onCharacteristicRead( BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status ) {
        if( status != BluetoothGatt.GATT_SUCCESS ) {
            Log.e( CLASS, "onCharacteristicRead: Status != SUCCESS: status=" + status );
            return;
        }
        mGatt = gatt;

        if( characteristic == null ) {
            Log.e( CLASS, "onCharacteristicRead: null characteristic" );
            return;
        }

        for( BtBridgeListener listener : mListeners ) {
            listener.onCharacteristicRead( characteristic );
        }
    }

    @Override
    public void onCharacteristicChanged( BluetoothGatt gatt, BluetoothGattCharacteristic characteristic ) {
        if( characteristic == null ) {
            Log.e( CLASS, "onCharacteristicChanged: null characteristic" );
            return;
        }
        mGatt = gatt;

        for( BtBridgeListener listener : mListeners ) {
            listener.onCharacteristicChanged( characteristic );
        }
    }

    @Override
    public void onCharacteristicWrite( BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status ) {
        if( status != BluetoothGatt.GATT_SUCCESS ) {
            Log.e( CLASS, "onCharacteristicWrite: Status != SUCCESS: status=" + status );
            return;
        }
        mGatt = gatt;

        if( characteristic == null ) {
            Log.e( CLASS, "onCharacteristicWrite: null characteristic" );
            return;
        }

        for( BtBridgeListener listener : mListeners ) {
            listener.onCharacteristicWrite( characteristic );
        }
    }


    @Override
    public void onDescriptorRead( BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status ) {
        if( status != BluetoothGatt.GATT_SUCCESS ) {
            Log.e( CLASS, "onDescriptorRead: Status != SUCCESS: status=" + status );
            return;
        }
        mGatt = gatt;

        if( descriptor == null ) {
            Log.e( CLASS, "onDescriptorRead: null descriptor" );
            return;
        }

        BluetoothGattCharacteristic characteristic = descriptor.getCharacteristic();
        for( BtBridgeListener listener : mListeners ) {
            listener.onDescriptorRead( characteristic, descriptor );
        }
    }

    @Override
    public void onDescriptorWrite( BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status ) {
        if( status != BluetoothGatt.GATT_SUCCESS ) {
            Log.e( CLASS, "onDescriptorWrite: Status != SUCCESS: status=" + status );
            return;
        }
        mGatt = gatt;

        if( descriptor == null ) {
            Log.e( CLASS, "onDescriptorWrite: null descriptor" );
            return;
        }

        BluetoothGattCharacteristic characteristic = descriptor.getCharacteristic();
        for( BtBridgeListener listener : mListeners ) {
            listener.onDescriptorWrite( characteristic, descriptor );
        }
    }
}
