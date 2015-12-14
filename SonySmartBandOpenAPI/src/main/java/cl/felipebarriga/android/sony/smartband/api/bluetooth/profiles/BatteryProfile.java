package cl.felipebarriga.android.sony.smartband.api.bluetooth.profiles;

import android.bluetooth.BluetoothGattCharacteristic;
import java.util.UUID;

/**
 * Copyright (c) 2015 Felipe Barriga Richards. See Copyright Notice in LICENSE file.
 */
public class BatteryProfile extends BaseProfile {
    public static final String CLASS = "BatteryProfile";

    public static final UUID SERVICE_UUID;
    public static final UUID BATTERY_LEVEL_UUID;

    static {
        SERVICE_UUID       = UUID.fromString( "0000180F-0000-1000-8000-00805F9B34FB" );
        BATTERY_LEVEL_UUID = UUID.fromString( "00002A19-0000-1000-8000-00805F9B34FB" );

        sUUIDNameMap.put( SERVICE_UUID, CLASS );
        sUUIDNameMap.put( BATTERY_LEVEL_UUID, "BATTERY_LEVEL_UUID" );
    }

    public static int readBatteryLevel( BluetoothGattCharacteristic characteristic ) {
        return characteristic.getIntValue( BluetoothGattCharacteristic.FORMAT_UINT8, 0 );
    }
}
