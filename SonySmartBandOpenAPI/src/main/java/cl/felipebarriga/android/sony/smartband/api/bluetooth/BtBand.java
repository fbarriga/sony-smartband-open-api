package cl.felipebarriga.android.sony.smartband.api.bluetooth;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.LinkedBlockingQueue;

import cl.felipebarriga.android.sony.smartband.api.bluetooth.profiles.AADeviceServiceProfile;
import cl.felipebarriga.android.sony.smartband.api.bluetooth.profiles.AHServiceProfile;
import cl.felipebarriga.android.sony.smartband.api.bluetooth.profiles.BaseProfile;
import cl.felipebarriga.android.sony.smartband.api.bluetooth.profiles.BatteryProfile;
import cl.felipebarriga.android.sony.smartband.api.bluetooth.profiles.BleConstants;
import cl.felipebarriga.android.sony.smartband.api.bluetooth.profiles.DeviceInformationProfile;
import cl.felipebarriga.android.sony.smartband.api.bluetooth.profiles.GenericAccessProfile;

/**
 * Copyright (c) 2015 Felipe Barriga Richards. See Copyright Notice in LICENSE file.
 */
public class BtBand implements BtBridgeListener {

    private final String CLASS = getClass().getSimpleName();

    private BtBridge mBtBridge;
    private Context mContext;

    ArrayList<BtBandListener> mListeners = new ArrayList<>();

    private boolean mEventNotificationsEnabled;
    private boolean mDataNotificationsEnabled;
    private boolean mDebugNotificationsEnabled;
    private boolean mAccNotificationsEnabled;

    public boolean isEventNotificationsEnabled() {
        return mEventNotificationsEnabled;
    }

    public boolean isDataNotificationsEnabled() {
        return mDataNotificationsEnabled;
    }

    public boolean isDebugNotificationsEnabled() {
        return mDebugNotificationsEnabled;
    }

    public boolean isAccNotificationsEnabled() {
        return mAccNotificationsEnabled;
    }

    public BtBridge.Status getStatus() {
        return mBtBridge.getStatus();
    }

    public void addListener( BtBandListener listener ) {
        if( mListeners.contains( listener ) ) {
            Log.w( CLASS, "setOnBtBandListener: Listener already registered" );
        } else {
            mListeners.add( listener );
        }
    }

    public BtBand( Context context ) {
        mEventNotificationsEnabled = false;
        mDataNotificationsEnabled  = false;
        mDebugNotificationsEnabled = false;
        mAccNotificationsEnabled   = false;

        mContext = context;
        mBtBridge = new BtBridge( mContext );
        mBtBridge.addListener( this );
    }

    private DescriptorInfo getDescriptorInfo( BluetoothGattDescriptor descriptor ) {
        BluetoothGattCharacteristic characteristic = descriptor.getCharacteristic();
        return getDescriptorInfo( characteristic.getService().getUuid(), characteristic.getUuid(), descriptor.getUuid() );
    }

    private DescriptorInfo getDescriptorInfo( UUID serviceUuid, UUID characteristicUuid, UUID descriptorUuid ) {
        BaseProfile serviceProfile = Profiles.getService( serviceUuid );
        return new DescriptorInfo(
                serviceProfile.getClass().getSimpleName(),
                serviceProfile.getUuidName( characteristicUuid ),
                BleConstants.getUuidName( descriptorUuid )
        );
    }

    private CharacteristicInfo getCharacteristicInfo( BluetoothGattCharacteristic characteristic ) {
        return getCharacteristicInfo( characteristic.getService().getUuid(), characteristic.getUuid() );
    }

    private CharacteristicInfo getCharacteristicInfo( UUID serviceUuid, UUID characteristicUuid ) {
        BaseProfile serviceProfile = Profiles.getService( serviceUuid );
        if( serviceProfile == null ) {
            Log.e( CLASS, "getCharacteristicInfo: service not found. serviceUuid=" + serviceUuid );
            return null;
        }

        return new CharacteristicInfo( serviceProfile.getClass().getSimpleName(), serviceProfile.getUuidName( characteristicUuid ) );
    }

    private BluetoothGattCharacteristic readGatt( UUID serviceUuid, UUID characteristicUuid ) {
        Log.d( CLASS, "readGatt: " + getCharacteristicInfo( serviceUuid, characteristicUuid ) );

        BluetoothGattCharacteristic characteristic = mBtBridge.getCharacteristic( serviceUuid, characteristicUuid );
        if( characteristic == null ) {
            Log.e( CLASS, "readGatt: characteristic not found" );
            return null;
        }
        return characteristic;
    }

    private void updateNotification( BtAction action, boolean enable ) {
        UUID serviceUuid = action.getServiceUUID();
        UUID characteristicUuid = action.getCharacteristicUUID();
        UUID descriptorUuid = action.getDescriptorUUID();
        Log.d( CLASS, "updateNotification: enable=" + enable + " " + getCharacteristicInfo( serviceUuid, characteristicUuid ) );

        BluetoothGattCharacteristic characteristic = mBtBridge.getCharacteristic( serviceUuid, characteristicUuid );
        if( characteristic == null ) {
            Log.e( CLASS, "updateNotification: characteristic not found" );
            return;
        }

        BluetoothGattDescriptor descriptor = characteristic.getDescriptor( descriptorUuid );
        if( descriptor == null ) {
            Log.e( CLASS, "updateNotification: descriptor not found" );
            return;
        }

        mBtBridge.setCharacteristicNotification( characteristic, enable );
        if( enable ) {
            descriptor.setValue( BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE );
        } else {
            descriptor.setValue( BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE );
        }
        Log.d( CLASS, "updateNotification: " + getDescriptorInfo( descriptor ) );

        if( !mBtBridge.writeDescriptor( descriptor ) ) {
            Log.e( CLASS, "updateNotification: success = false " + getDescriptorInfo( descriptor ) );
        }
    }

    private BluetoothGattCharacteristic setValue( UUID serviceUuid, UUID characteristicUuid, int value, int format, int offset ) {
        BluetoothGattCharacteristic characteristic = mBtBridge.getCharacteristic( serviceUuid, characteristicUuid );
        if( characteristic == null ) {
            Log.e( CLASS, "setValue: characteristic not found" );
            return null;
        }

        if( !characteristic.setValue( value, format, offset ) ) {
            Log.e( CLASS, "setValue: error setting value" );
            return null;
        }

        return characteristic;
    }

    private Queue<BtAction> mRequestQueue = new LinkedBlockingQueue<>();

    public void addRequest( BtAction.Action action ) {
        mRequestQueue.add( new BtAction( action ) );
        processQueue();
    }

    public void addRequest( BtAction.Action action, int parameter ) {
        mRequestQueue.add( new BtAction( action ).setParameter( parameter ) );
        processQueue();
    }

    public void syncDeviceTime() {
        long now = Calendar.getInstance().getTimeInMillis();
        setDeviceTime( AHServiceProfile.convertTimestampToBandSeconds( now ) );
    }

    /**
     * Set the device time.
     * @param bandTimestamp Number of seconds since 2013
     */
    public void setDeviceTime( int bandTimestamp ) {
        Date date = AHServiceProfile.convertBandSecondsToDate( bandTimestamp );
        Log.d( CLASS, "sendCurrentTime: called. bandTimestamp=" + bandTimestamp + " date=" + date );
        mRequestQueue.add( new BtAction( BtAction.Action.SET_CURRENT_TIME ).setParameter( bandTimestamp ) );
    }

    private void processQueue() {
        Log.d( CLASS, "processQueue: called." );
        BtAction action = mRequestQueue.poll();
        if( action == null ) {
            Log.d( CLASS, "processQueue: the queue is empty" );
            return;
        }

        executeAction( action );
    }

    private void executeAction( BtAction action ) {
        BtAction.Type type = action.getType();
        Log.i( CLASS, "executeAction: action=" + action.mAction.name() + " type=" + type.name() );

        if( type.equals( BtAction.Type.READ_CHAR ) ) {
            mBtBridge.readCharacteristic( readGatt( action.getServiceUUID(), action.getCharacteristicUUID() ) );
        } else if( type.equals( BtAction.Type.WRITE_CHAR ) ) {
            mBtBridge.writeCharacteristic(
                    setValue(
                            action.getServiceUUID(),
                            action.getCharacteristicUUID(),
                            action.mParam,
                            action.getFormat(),
                            0
                    )
            );
        } else if( type.equals( BtAction.Type.ENABLE_NOTIFICATION ) ) {
            updateNotification( action, true );
        } else if( type.equals( BtAction.Type.DISABLE_NOTIFICATION ) ) {
            updateNotification( action, false );
        } else {
            Log.e( CLASS, "executeAction: don't know how to handle: action=" + action.mAction.name() );
        }
    }

    public void onConnectionStateChange( BtBridge.Status newStatus ) {
        if( newStatus.equals( BtBridge.Status.CONNECTED ) ) {
            mBtBridge.discoverServices();
        }

        for( BtBandListener listener : mListeners ) {
            listener.onConnectionStateChange( newStatus );
        }
    }

    public void onServicesDiscovered( List<BluetoothGattService> services ) {
        Log.d( CLASS, "onServicesDiscovered: services=" + services.size() );

        for( BluetoothGattService service : services ) {
            BaseProfile serviceProfile = Profiles.getService( service.getUuid() );
            if( serviceProfile == null ) {
                Log.w( CLASS, "- service=unknown uuid=" + service.getUuid() );
                continue;
            }
            Log.d( CLASS, "-> service name=" + serviceProfile.getClass().getSimpleName() + " (" + service.getUuid() + ")" );
            List<BluetoothGattCharacteristic> chars = service.getCharacteristics();
            for( BluetoothGattCharacteristic c : chars ) {
                Log.d( CLASS, "--> characteristic name=" + serviceProfile.getUuidName( c.getUuid() ) + " (" + c.getUuid() + ")" );
                List<BluetoothGattDescriptor> descriptors = c.getDescriptors();
                for( BluetoothGattDescriptor d : descriptors ) {
                    Log.d( CLASS, "---> descriptor name=" + BleConstants.getUuidName( d.getUuid() ) + " (" + d.getUuid() + ")" );
                }
            }
        }

        for( BtBandListener listener : mListeners ) {
            listener.onServicesDiscovered( );
        }
    }

    private void handleDeviceInfoFirmwareRevision( BluetoothGattCharacteristic characteristic ) {
        String revision = characteristic.getStringValue( 0 );
        for( BtBandListener listener : mListeners ) {
            listener.onFirmwareRevision( revision );
        }
    }

    private void handleDeviceInfoSoftwareRevision( BluetoothGattCharacteristic characteristic ) {
        String revision = characteristic.getStringValue( 0 );
        for( BtBandListener listener : mListeners ) {
            listener.onSoftwareRevision( revision );
        }
    }

    private void handleDeviceInfoHardwareRevision( BluetoothGattCharacteristic characteristic ) {
        String revision = characteristic.getStringValue( 0 );
        for( BtBandListener listener : mListeners ) {
            listener.onHardwareRevision( revision );
        }
    }

    private void handleDeviceInfoManufacturerName( BluetoothGattCharacteristic characteristic ) {
        String name = characteristic.getStringValue( 0 );
        for( BtBandListener listener : mListeners ) {
            listener.onManufacturerName( name );
        }
    }

    private void handleDeviceInfoCallback( BluetoothGattCharacteristic characteristic ) {
        UUID uuid = characteristic.getUuid();

        if( uuid.equals( DeviceInformationProfile.FIRMWARE_REVISION_UUID ) ) {
            handleDeviceInfoFirmwareRevision( characteristic );
        } else if( uuid.equals( DeviceInformationProfile.SOFTWARE_REVISION_UUID) ) {
            handleDeviceInfoSoftwareRevision( characteristic );
        } else if( uuid.equals( DeviceInformationProfile.HARDWARE_REVISION_UUID) ) {
            handleDeviceInfoHardwareRevision( characteristic );
        } else if( uuid.equals( DeviceInformationProfile.MANUFACTURER_NAME_UUID) ) {
            handleDeviceInfoManufacturerName( characteristic );
        } else {
            Log.e( CLASS, "handleDeviceInfoCallback: unknown response" );
        }
    }

    private void handleAASDeviceVersion( BluetoothGattCharacteristic characteristic ) {
        int version = AADeviceServiceProfile.readDeviceAASVersion( characteristic );
        for( BtBandListener listener : mListeners ) {
            listener.onAASDeviceVersion( version );
        }
    }

    private void handleAASDeviceSmartLink( BluetoothGattCharacteristic characteristic ) {
        int smartLink = AADeviceServiceProfile.readDeviceSmartLinkService( characteristic );
        for( BtBandListener listener : mListeners ) {
            listener.onAASDeviceSmartLink( smartLink );
        }
    }

    private void handleAASDeviceProductId( BluetoothGattCharacteristic characteristic ) {
        String productId = characteristic.getStringValue( 0 );
        for( BtBandListener listener : mListeners ) {
            listener.onAASDeviceProductId( productId );
        }
    }

    private void handleAASDeviceData( BluetoothGattCharacteristic characteristic ) {
        String data = characteristic.getStringValue( 0 );
        for( BtBandListener listener : mListeners ) {
            listener.onAASDeviceData( data );
        }
    }

    private void handleAADeviceServiceCallback( BluetoothGattCharacteristic characteristic ) {
        UUID uuid = characteristic.getUuid();

        if( uuid.equals( AADeviceServiceProfile.VERSION_UUID ) ) {
            handleAASDeviceVersion( characteristic );
        } else if( uuid.equals( AADeviceServiceProfile.SMART_LINK_SERVICE_UUID ) ) {
            handleAASDeviceSmartLink( characteristic );
        } else if( uuid.equals( AADeviceServiceProfile.PRODUCT_ID_UUID ) ) {
            handleAASDeviceProductId( characteristic );
        } else if( uuid.equals( AADeviceServiceProfile.DATA_UUID ) ) {
            handleAASDeviceData( characteristic );
        } else {
            Log.e( CLASS, "handleAADeviceServiceCallback: unknown response" );
        }
    }

    private void handleGADeviceName( BluetoothGattCharacteristic characteristic ) {
        String name = characteristic.getStringValue( 0 );
        for( BtBandListener listener : mListeners ) {
            listener.onGADeviceName( name );
        }
    }

    private void handleGenericAccessCallback( BluetoothGattCharacteristic characteristic ) {
        UUID uuid = characteristic.getUuid();

        if( uuid.equals( GenericAccessProfile.DEVICE_NAME_UUID ) ) {
            handleGADeviceName( characteristic );
        } else {
            Log.e( CLASS, "handleGenericAccessCallback: unknown response" );
        }
    }

    private void handleBatteryReadLevel( BluetoothGattCharacteristic characteristic ) {
        int level = BatteryProfile.readBatteryLevel( characteristic );
        for( BtBandListener listener : mListeners ) {
            listener.onBatteryLevel( level );
        }
    }

    private void handleBatteryCallback( BluetoothGattCharacteristic characteristic ) {
        UUID uuid = characteristic.getUuid();
        if( uuid .equals( BatteryProfile.BATTERY_LEVEL_UUID ) ) {
            handleBatteryReadLevel( characteristic );
        }
    }

    private void handleAHServiceReadMode( BluetoothGattCharacteristic characteristic ) {
        BandMode.AccessoryMode mode = AHServiceProfile.readMode( characteristic );
        for( BtBandListener listener : mListeners ) {
            listener.onAHServiceReadMode( mode );
        }
    }

    private void handleAHServiceReadProtocolVersion( BluetoothGattCharacteristic characteristic ) {
        int version = AHServiceProfile.readProtocolVersion( characteristic );
        for( BtBandListener listener : mListeners ) {
            listener.onAHServiceReadProtocolVersion( version );
        }
    }

    private void handleAHServiceReadData( BluetoothGattCharacteristic characteristic ) {
        for( BtBandListener listener : mListeners ) {
            listener.onAHServiceReadData( );
        }
    }

    private void handleAHServiceReadCurrentTime( BluetoothGattCharacteristic characteristic ) {
        int bandSeconds = AHServiceProfile.readCurrentTime( characteristic );
        Date deviceDate = AHServiceProfile.convertBandSecondsToDate( bandSeconds );
        long deltaMs = deviceDate.getTime() - System.currentTimeMillis();
        for( BtBandListener listener : mListeners ) {
            listener.onAHServiceReadCurrentTime( bandSeconds, deviceDate, deltaMs );
        }
    }

    private void handleAHServiceReadUptime( BluetoothGattCharacteristic characteristic ) {
        int uptime = AHServiceProfile.readUptime( characteristic );
        Date started = new Date( System.currentTimeMillis() - uptime * 1000 );
        for( BtBandListener listener : mListeners ) {
            listener.onAHServiceReadUptime( uptime, started );
        }
    }

    private void handleAHServiceReadEvent( BluetoothGattCharacteristic characteristic ) {
        BtBandEvent btBandEvent = AHServiceProfile.readEvent( characteristic );
        for( BtBandListener listener : mListeners ) {
            listener.onAHServiceReadEvent( btBandEvent );
        }
    }

    private void handleAHServiceReadAccData( BluetoothGattCharacteristic characteristic ) {
        int[] data = AHServiceProfile.readAccData( characteristic );
        for( BtBandListener listener : mListeners ) {
            listener.onAHServiceReadAccData( data );
        }
    }

    private void handleAHServiceCallback( BluetoothGattCharacteristic characteristic ) {
        UUID uuid = characteristic.getUuid();

        if( uuid.equals( AHServiceProfile.MODE_UUID ) ) {
            handleAHServiceReadMode( characteristic );
        } else if( uuid.equals( AHServiceProfile.PROTOCOL_VERSION_UUID ) ) {
            handleAHServiceReadProtocolVersion( characteristic );
        } else if( uuid.equals( AHServiceProfile.DATA_UUID ) ) {
            handleAHServiceReadData( characteristic );
        } else if( uuid.equals( AHServiceProfile.CURRENT_TIME_UUID ) ) {
            handleAHServiceReadCurrentTime( characteristic );
        } else if( uuid.equals( AHServiceProfile.DEBUG_UUID ) ) {
            handleAHServiceReadUptime( characteristic );
        } else if( uuid.equals( AHServiceProfile.EVENT_UUID ) ) {
            handleAHServiceReadEvent( characteristic );
        } else if( uuid.equals( AHServiceProfile.ACCEL_DATA_UUID ) ) {
            handleAHServiceReadAccData( characteristic );
        } else {
            Log.e( CLASS, "handleOnCharacteristicRead: unknown response" );
        }
    }

    public void onCharacteristicRead( BluetoothGattCharacteristic characteristic ) {
        BluetoothGattService service = characteristic.getService();
        BaseProfile serviceProfile = Profiles.getService( service.getUuid() );

        Log.d( CLASS, "onCharacteristicRead: " + getCharacteristicInfo( characteristic ) );
        if( serviceProfile.getClass().equals( BatteryProfile.class ) ) {
            handleBatteryCallback( characteristic );
        } else if( serviceProfile.getClass().equals( AHServiceProfile.class ) ) {
            handleAHServiceCallback( characteristic );
        } else if( serviceProfile.getClass().equals( DeviceInformationProfile.class ) ) {
            handleDeviceInfoCallback( characteristic );
        } else if( serviceProfile.getClass().equals( AADeviceServiceProfile.class ) ) {
            handleAADeviceServiceCallback( characteristic );
        } else if( serviceProfile.getClass().equals( GenericAccessProfile.class ) ) {
            handleGenericAccessCallback( characteristic );
        } else {
            Log.w( CLASS, "onCharacteristicRead: unhandled event. class=" + serviceProfile.getClass() );
        }
        processQueue();
    }

    public void onCharacteristicChanged( BluetoothGattCharacteristic characteristic ) {
        BluetoothGattService service = characteristic.getService();
        BaseProfile serviceProfile = Profiles.getService( service.getUuid() );

        Log.d( CLASS, "onCharacteristicChanged: " + getCharacteristicInfo( characteristic ) );
        if( serviceProfile.getClass().equals( BatteryProfile.class ) ) {
            handleBatteryCallback( characteristic );
        } else if( serviceProfile.getClass().equals( AHServiceProfile.class ) ) {
            handleAHServiceCallback( characteristic );
        } else if( serviceProfile.getClass().equals( DeviceInformationProfile.class ) ) {
            handleDeviceInfoCallback( characteristic );
        } else if( serviceProfile.getClass().equals( AADeviceServiceProfile.class ) ) {
            handleAADeviceServiceCallback( characteristic );
        } else if( serviceProfile.getClass().equals( GenericAccessProfile.class ) ) {
            handleGenericAccessCallback( characteristic );
        } else {
            Log.w( CLASS, "onCharacteristicChanged: unhandled event. class=" + serviceProfile.getClass() );
        }
        processQueue();
    }

    public void onCharacteristicWrite( BluetoothGattCharacteristic characteristic ) {
        Log.d( CLASS, "onCharacteristicWrite: " + getCharacteristicInfo( characteristic ) );
        processQueue();
    }

    public void onDescriptorRead( BluetoothGattCharacteristic characteristic, BluetoothGattDescriptor descriptor ) {
        Log.d( CLASS, "onDescriptorRead: " + getDescriptorInfo( descriptor ) );
        processQueue();
    }

    // TODO: Add callbacks
    // TODO: track in a better way if a notification is enabled
    public void onDescriptorWrite( BluetoothGattCharacteristic characteristic, BluetoothGattDescriptor descriptor ) {
        BluetoothGattService service = characteristic.getService();
        BaseProfile serviceProfile = Profiles.getService( service.getUuid() );

        Log.d( CLASS, "onDescriptorWrite: " + getDescriptorInfo( descriptor ) );

        UUID uuid = characteristic.getUuid();

        if( serviceProfile.getClass().equals( AHServiceProfile.class ) ) {
            if( uuid.equals( AHServiceProfile.DATA_UUID ) ) {
                mDataNotificationsEnabled = !mDataNotificationsEnabled;
            } else if( uuid.equals( AHServiceProfile.DEBUG_UUID ) ) {
                mDebugNotificationsEnabled = !mDebugNotificationsEnabled;
            } else if( uuid.equals( AHServiceProfile.EVENT_UUID ) ) {
                mEventNotificationsEnabled = !mEventNotificationsEnabled;
            } else if( uuid.equals( AHServiceProfile.ACCEL_DATA_UUID ) ) {
                mAccNotificationsEnabled = !mAccNotificationsEnabled ;
            } else {
                Log.w( CLASS, "onCharacteristicChanged: unhandled event. uuid=" + uuid
                        + " class=" + serviceProfile.getClass() );
            }
        } else {
            Log.w( CLASS, "onCharacteristicChanged: unhandled event. class=" + serviceProfile.getClass() );
        }

        processQueue();
    }

    public boolean connect() {
        return mBtBridge.connect();
    }

    public boolean disconnect() {
        return mBtBridge.disconnect();
    }

}
