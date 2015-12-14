package cl.felipebarriga.android.sony.smartband.api.bluetooth.profiles;

import android.bluetooth.BluetoothGattCharacteristic;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import cl.felipebarriga.android.sony.smartband.api.bluetooth.BandMode;
import cl.felipebarriga.android.sony.smartband.api.bluetooth.BtBandEvent;

/**
 * Copyright (c) 2015 Felipe Barriga Richards. See Copyright Notice in LICENSE file.
 */
public class AHServiceProfile extends BaseProfile {
    public static final String CLASS = "AHServiceProfile";

    public static final UUID SERVICE_UUID;
    public static final UUID ACCEL_DATA_UUID;
    public static final UUID AUTO_NIGHT_MODE_UUID;
    public static final UUID CONTROL_POINT_UUID;
    public static final UUID ALARM_UUID;
    public static final UUID CURRENT_TIME_UUID;
    public static final UUID DATA_UUID;
    public static final UUID DEBUG_UUID;
    public static final UUID MODE_UUID;
    public static final UUID EVENT_UUID;
    public static final UUID NOTIFICATION_UUID;
    public static final UUID PROTOCOL_VERSION_UUID;
    public static final UUID USER_TARGET_UUID;

    public static final long TIMESTAMP_OF_2013 = 1356998400000L;

    public interface TAP_EVENT_TYPE {
        int SINGLE = 0;
        int DOUBLE = 1;
        int TRIPLE = 2;
    }

    static {
        SERVICE_UUID          = UUID.fromString( "00000200-37cb-11e3-8682-0002a5d5c51b" );
        PROTOCOL_VERSION_UUID = UUID.fromString( "00000201-37cb-11e3-8682-0002a5d5c51b" );
        MODE_UUID             = UUID.fromString( "00000202-37cb-11e3-8682-0002a5d5c51b" );
        DATA_UUID             = UUID.fromString( "00000203-37cb-11e3-8682-0002a5d5c51b" );
        NOTIFICATION_UUID     = UUID.fromString( "00000204-37cb-11e3-8682-0002a5d5c51b" );
        USER_TARGET_UUID      = UUID.fromString( "00000205-37cb-11e3-8682-0002a5d5c51b" );
        ALARM_UUID            = UUID.fromString( "00000206-37cb-11e3-8682-0002a5d5c51b" );
        ACCEL_DATA_UUID       = UUID.fromString( "00000207-37cb-11e3-8682-0002a5d5c51b" );
        CURRENT_TIME_UUID     = UUID.fromString( "00000208-37cb-11e3-8682-0002a5d5c51b" );
        EVENT_UUID            = UUID.fromString( "00000209-37cb-11e3-8682-0002a5d5c51b" );
        CONTROL_POINT_UUID    = UUID.fromString( "00000210-37cb-11e3-8682-0002a5d5c51b" );
        DEBUG_UUID            = UUID.fromString( "00000211-37cb-11e3-8682-0002a5d5c51b" );
        AUTO_NIGHT_MODE_UUID  = UUID.fromString( "00000212-37cb-11e3-8682-0002a5d5c51b" );

        sUUIDNameMap.put( SERVICE_UUID         , CLASS                   );
        sUUIDNameMap.put( PROTOCOL_VERSION_UUID, "PROTOCOL_VERSION_UUID" );
        sUUIDNameMap.put( MODE_UUID            , "MODE_UUID"             );
        sUUIDNameMap.put( DATA_UUID            , "DATA_UUID"             );
        sUUIDNameMap.put( NOTIFICATION_UUID    , "NOTIFICATION_UUID"     );
        sUUIDNameMap.put( USER_TARGET_UUID     , "USER_TARGET_UUID"      );
        sUUIDNameMap.put( ALARM_UUID           , "ALARM_UUID"            );
        sUUIDNameMap.put( ACCEL_DATA_UUID      , "ACCEL_DATA_UUID"       );
        sUUIDNameMap.put( CURRENT_TIME_UUID    , "CURRENT_TIME_UUID"     );
        sUUIDNameMap.put( EVENT_UUID           , "EVENT_UUID"            );
        sUUIDNameMap.put( CONTROL_POINT_UUID   , "CONTROL_POINT_UUID"    );
        sUUIDNameMap.put( DEBUG_UUID           , "DEBUG_UUID"            );
        sUUIDNameMap.put( AUTO_NIGHT_MODE_UUID , "AUTO_NIGHT_MODE_UUID"  );
    }

    public static int readProtocolVersion( BluetoothGattCharacteristic characteristic ) {
        return characteristic.getIntValue( BluetoothGattCharacteristic.FORMAT_UINT8, 0 );
    }

    // return uptime in seconds
    public static int readUptime( BluetoothGattCharacteristic characteristic ) {
        return characteristic.getIntValue( BluetoothGattCharacteristic.FORMAT_UINT32, 0 );
    }



    public static BandMode.AccessoryMode readMode( BluetoothGattCharacteristic characteristic ) {
        int mode = characteristic.getIntValue( BluetoothGattCharacteristic.FORMAT_UINT8, 0 );
        return new BandMode( mode ).getMode();
    }

    public static long convertBandSecondsToTimestamp( int seconds ) {
        return TIMESTAMP_OF_2013 + ( (long) seconds ) * 1000;
    }

    public static Date convertBandSecondsToDate( int seconds ) {
        long utcTime = convertBandSecondsToTimestamp( seconds );
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis( utcTime );
        return calendar.getTime();
    }

    public static int convertTimestampToBandSeconds( long timestamp ) {
        long bandMilliseconds = timestamp - TIMESTAMP_OF_2013;
        int bandSeconds = (int) (bandMilliseconds / 1000);

        return bandSeconds;
    }

    public static void readData( BluetoothGattCharacteristic characteristic ) {
        // TODO
    }

    public static BtBandEvent readEvent( BluetoothGattCharacteristic characteristic ) {
        int PAYLOAD_SIZE = 4;
        int payload = 0;
        int offset = 0;

        if( characteristic.getValue().length > PAYLOAD_SIZE ) {
            payload = characteristic.getIntValue( BluetoothGattCharacteristic.FORMAT_UINT32, 0 );
            offset = PAYLOAD_SIZE;
        }
        int eventData = characteristic.getIntValue( BluetoothGattCharacteristic.FORMAT_UINT32, offset );

        int code  = ( 0b11111111000000000000000000000000 & eventData ) >> 24;
        int value = ( 0b00000000111111111111111111111111 & eventData );
        Log.v( CLASS, "readEvent: code=" + code + " value=" + value + " payload=" + payload );

        return new BtBandEvent( code, value, payload );
    }

    /**
     * Retrieve acceleration data from characteristic with 10bits of resolution
     * @param characteristic
     * @return int[] array with raw values
     */
    public static int[] readAccData( BluetoothGattCharacteristic characteristic ) {
        int accData = characteristic.getIntValue( BluetoothGattCharacteristic.FORMAT_UINT32, 0 );
        int x = ( 0b111111111100000000000000000000 & accData ) >> 20;
        int y = ( 0b000000000011111111110000000000 & accData ) >> 10;
        int z = ( 0b000000000000000000001111111111 & accData );

        return new int[]{ x, y, z };
    }

    public static int readCurrentTime( BluetoothGattCharacteristic characteristic ) {
        return characteristic.getIntValue( BluetoothGattCharacteristic.FORMAT_UINT32, 0 );
    }

}
