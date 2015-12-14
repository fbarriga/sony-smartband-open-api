package cl.felipebarriga.android.sony.smartband.api.bluetooth;

import android.bluetooth.BluetoothGattCharacteristic;

import java.util.HashMap;
import java.util.UUID;

import cl.felipebarriga.android.sony.smartband.api.bluetooth.profiles.AADeviceServiceProfile;
import cl.felipebarriga.android.sony.smartband.api.bluetooth.profiles.AHServiceProfile;
import cl.felipebarriga.android.sony.smartband.api.bluetooth.profiles.BatteryProfile;
import cl.felipebarriga.android.sony.smartband.api.bluetooth.profiles.BleConstants;
import cl.felipebarriga.android.sony.smartband.api.bluetooth.profiles.DeviceInformationProfile;
import cl.felipebarriga.android.sony.smartband.api.bluetooth.profiles.GenericAccessProfile;

/**
 * Copyright (c) 2015 Felipe Barriga Richards. See Copyright Notice in LICENSE file.
 */
public class BtAction {
    public static class ActionDetails {
        public final Action mAction;

        public final int mFormat;
        public final Type mType;

        public final UUID mService;
        public final UUID mCharacteristic;
        public final UUID mDescriptor;

        public ActionDetails( Action action, Type type, UUID service, UUID characteristic, UUID descriptor, int format ) {
            mAction = action;
            mType = type;
            mService = service;
            mCharacteristic = characteristic;
            mDescriptor = descriptor;
            mFormat = format;
        }

        public ActionDetails( Action action, Type type, UUID service, UUID characteristic, UUID descriptor ) {
            this( action, type, service, characteristic, descriptor, 0 );
        }

        public ActionDetails( Action action, Type type, UUID service, UUID characteristic, int format ) {
            this( action, type, service, characteristic, null, format );
        }

        public ActionDetails( Action action, Type type, UUID service, UUID characteristic ) {
            this( action, type, service, characteristic, null, 0 );
        }
    }

    enum Type {
        READ_CHAR,
        WRITE_CHAR,
        ENABLE_NOTIFICATION,
        DISABLE_NOTIFICATION
    }

    public enum Action {
        REQUEST_BATTERY_LEVEL,
        REQUEST_PROTOCOL_VERSION,
        REQUEST_CURRENT_TIME,
        REQUEST_UPTIME,
        REQUEST_MODE,
        REQUEST_FIRMWARE_REVISION,
        REQUEST_HARDWARE_REVISION,
        REQUEST_SOFTWARE_REVISION,
        REQUEST_MANUFACTURER_NAME,
        REQUEST_AAS_VERSION,
        REQUEST_AAS_SMART_LINK,
        REQUEST_AAS_PRODUCT_ID,
        REQUEST_AAS_DEVICE_DATA,
        REQUEST_GA_DEVICE_NAME,
        SET_MODE,
        SET_ACC_INTERVAL,
        SET_CURRENT_TIME,
        ENABLE_DEBUG_NOTIFICATIONS,
        DISABLE_DEBUG_NOTIFICATIONS,
        ENABLE_EVENT_NOTIFICATIONS,
        DISABLE_EVENT_NOTIFICATIONS,
        ENABLE_DATA_NOTIFICATIONS,
        DISABLE_DATA_NOTIFICATIONS,
        ENABLE_ACC_NOTIFICATIONS,
        DISABLE_ACC_NOTIFICATIONS,

    }

    private final static HashMap<Action, ActionDetails> ACTION_MAP = new HashMap<>(  );

    private static void mapAction( Action action, Type type, UUID service, UUID characteristic ) {
        ACTION_MAP.put( action, new ActionDetails( action, type, service, characteristic ) );
    }

    private static void mapAction( Action action, Type type, UUID service, UUID characteristic, int format ) {
        ACTION_MAP.put( action, new ActionDetails( action, type, service, characteristic, format ) );
    }

    private static void mapAction( Action action, Type type, UUID service, UUID characteristic, UUID descriptor ) {
        ACTION_MAP.put( action, new ActionDetails( action, type, service, characteristic, descriptor ) );
    }

    static {
        mapAction( Action.REQUEST_BATTERY_LEVEL     , Type.READ_CHAR, BatteryProfile.SERVICE_UUID          , BatteryProfile.BATTERY_LEVEL_UUID               );

        mapAction( Action.REQUEST_PROTOCOL_VERSION  , Type.READ_CHAR, AHServiceProfile.SERVICE_UUID        , AHServiceProfile.PROTOCOL_VERSION_UUID          );
        mapAction( Action.REQUEST_CURRENT_TIME      , Type.READ_CHAR, AHServiceProfile.SERVICE_UUID        , AHServiceProfile.CURRENT_TIME_UUID              );
        mapAction( Action.REQUEST_MODE              , Type.READ_CHAR, AHServiceProfile.SERVICE_UUID        , AHServiceProfile.MODE_UUID                      );
        mapAction( Action.REQUEST_UPTIME            , Type.READ_CHAR, AHServiceProfile.SERVICE_UUID        , AHServiceProfile.DEBUG_UUID                     );

        mapAction( Action.REQUEST_FIRMWARE_REVISION , Type.READ_CHAR, DeviceInformationProfile.SERVICE_UUID, DeviceInformationProfile.FIRMWARE_REVISION_UUID );
        mapAction( Action.REQUEST_HARDWARE_REVISION , Type.READ_CHAR, DeviceInformationProfile.SERVICE_UUID, DeviceInformationProfile.HARDWARE_REVISION_UUID );
        mapAction( Action.REQUEST_SOFTWARE_REVISION , Type.READ_CHAR, DeviceInformationProfile.SERVICE_UUID, DeviceInformationProfile.SOFTWARE_REVISION_UUID );
        mapAction( Action.REQUEST_MANUFACTURER_NAME , Type.READ_CHAR, DeviceInformationProfile.SERVICE_UUID, DeviceInformationProfile.MANUFACTURER_NAME_UUID );

        mapAction( Action.REQUEST_AAS_VERSION       , Type.READ_CHAR, AADeviceServiceProfile.SERVICE_UUID  , AADeviceServiceProfile.VERSION_UUID             );
        mapAction( Action.REQUEST_AAS_SMART_LINK    , Type.READ_CHAR, AADeviceServiceProfile.SERVICE_UUID  , AADeviceServiceProfile.SMART_LINK_SERVICE_UUID  );
        mapAction( Action.REQUEST_AAS_PRODUCT_ID    , Type.READ_CHAR, AADeviceServiceProfile.SERVICE_UUID  , AADeviceServiceProfile.PRODUCT_ID_UUID          );
        mapAction( Action.REQUEST_AAS_DEVICE_DATA   , Type.READ_CHAR, AADeviceServiceProfile.SERVICE_UUID  , AADeviceServiceProfile.DATA_UUID                );

        mapAction( Action.REQUEST_GA_DEVICE_NAME    , Type.READ_CHAR, GenericAccessProfile.SERVICE_UUID    , GenericAccessProfile.DEVICE_NAME_UUID           );

        mapAction( Action.SET_CURRENT_TIME, Type.WRITE_CHAR, AHServiceProfile.SERVICE_UUID, AHServiceProfile.CURRENT_TIME_UUID, BluetoothGattCharacteristic.FORMAT_UINT32 );
        mapAction( Action.SET_MODE        , Type.WRITE_CHAR, AHServiceProfile.SERVICE_UUID, AHServiceProfile.MODE_UUID        , BluetoothGattCharacteristic.FORMAT_UINT8  );
        mapAction( Action.SET_ACC_INTERVAL, Type.WRITE_CHAR, AHServiceProfile.SERVICE_UUID, AHServiceProfile.ACCEL_DATA_UUID  , BluetoothGattCharacteristic.FORMAT_UINT32 );

        mapAction( Action.ENABLE_EVENT_NOTIFICATIONS , Type.ENABLE_NOTIFICATION , AHServiceProfile.SERVICE_UUID, AHServiceProfile.EVENT_UUID, BleConstants.CLIENT_CHARACTERISTIC_CONFIGURATION );
        mapAction( Action.DISABLE_EVENT_NOTIFICATIONS, Type.DISABLE_NOTIFICATION, AHServiceProfile.SERVICE_UUID, AHServiceProfile.EVENT_UUID, BleConstants.CLIENT_CHARACTERISTIC_CONFIGURATION );
        mapAction( Action.ENABLE_DATA_NOTIFICATIONS  , Type.ENABLE_NOTIFICATION , AHServiceProfile.SERVICE_UUID, AHServiceProfile.DATA_UUID, BleConstants.CLIENT_CHARACTERISTIC_CONFIGURATION );
        mapAction( Action.DISABLE_DATA_NOTIFICATIONS , Type.DISABLE_NOTIFICATION, AHServiceProfile.SERVICE_UUID, AHServiceProfile.DATA_UUID, BleConstants.CLIENT_CHARACTERISTIC_CONFIGURATION );
        mapAction( Action.ENABLE_ACC_NOTIFICATIONS   , Type.ENABLE_NOTIFICATION , AHServiceProfile.SERVICE_UUID, AHServiceProfile.ACCEL_DATA_UUID, BleConstants.CLIENT_CHARACTERISTIC_CONFIGURATION );
        mapAction( Action.DISABLE_ACC_NOTIFICATIONS  , Type.DISABLE_NOTIFICATION, AHServiceProfile.SERVICE_UUID, AHServiceProfile.ACCEL_DATA_UUID, BleConstants.CLIENT_CHARACTERISTIC_CONFIGURATION );
        mapAction( Action.ENABLE_DEBUG_NOTIFICATIONS , Type.ENABLE_NOTIFICATION , AHServiceProfile.SERVICE_UUID, AHServiceProfile.DEBUG_UUID, BleConstants.CLIENT_CHARACTERISTIC_CONFIGURATION );
        mapAction( Action.DISABLE_DEBUG_NOTIFICATIONS, Type.DISABLE_NOTIFICATION, AHServiceProfile.SERVICE_UUID, AHServiceProfile.DEBUG_UUID, BleConstants.CLIENT_CHARACTERISTIC_CONFIGURATION );
    }

    public int mParam;

    public final Action mAction;
    BtAction( Action action ) {
        mAction = action;
    }

    public BtAction setParameter( int param ) {
        mParam = param;
        return this;
    }

    public UUID getServiceUUID() {
        ActionDetails details = ACTION_MAP.get( mAction );
        return details.mService;
    }

    public UUID getCharacteristicUUID() {
        ActionDetails details = ACTION_MAP.get( mAction );
        return details.mCharacteristic;
    }

    public UUID getDescriptorUUID() {
        ActionDetails details = ACTION_MAP.get( mAction );
        return details.mDescriptor;
    }

    public Type getType() {
        ActionDetails details = ACTION_MAP.get( mAction );
        return details.mType;
    }

    public int getFormat() {
        ActionDetails details = ACTION_MAP.get( mAction );
        return details.mFormat;
    }


}
