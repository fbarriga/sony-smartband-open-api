package cl.felipebarriga.android.sony.smartband.api.bluetooth.profiles;

import android.bluetooth.BluetoothGattCharacteristic;
import java.util.UUID;

/**
 * Copyright (c) 2015 Felipe Barriga Richards. See Copyright Notice in LICENSE file.
 */
public class AADeviceServiceProfile extends BaseProfile {
    public static final String CLASS = "AADeviceServiceProfile";

    public static final UUID SERVICE_UUID;
    public static final UUID VERSION_UUID;
    public static final UUID DATA_UUID;
    public static final UUID PRODUCT_ID_UUID;
    public static final UUID SMART_LINK_SERVICE_UUID;

    static {
        SERVICE_UUID            = UUID.fromString( "00000101-37cb-11e3-8682-0002a5d5c51b" );
        VERSION_UUID            = UUID.fromString( "00000110-37cb-11e3-8682-0002a5d5c51b" );
        SMART_LINK_SERVICE_UUID = UUID.fromString( "00000111-37cb-11e3-8682-0002a5d5c51b" );
        PRODUCT_ID_UUID         = UUID.fromString( "00000112-37cb-11e3-8682-0002a5d5c51b" );
        DATA_UUID               = UUID.fromString( "00000113-37cb-11e3-8682-0002a5d5c51b" );

        sUUIDNameMap.put( SERVICE_UUID           , CLASS                     );
        sUUIDNameMap.put( VERSION_UUID           , "VERSION_UUID"            );
        sUUIDNameMap.put( SMART_LINK_SERVICE_UUID, "SMART_LINK_SERVICE_UUID" );
        sUUIDNameMap.put( PRODUCT_ID_UUID        , "PRODUCT_ID_UUID"         );
        sUUIDNameMap.put( DATA_UUID              , "DATA_UUID"               );
    }

    public static int readDeviceAASVersion( BluetoothGattCharacteristic characteristic ) {
        return characteristic.getIntValue( BluetoothGattCharacteristic.FORMAT_UINT16, 0 );
    }

    public static int readDeviceSmartLinkService( BluetoothGattCharacteristic characteristic ) {
        return characteristic.getIntValue( BluetoothGattCharacteristic.FORMAT_UINT8, 0 );
    }

}
