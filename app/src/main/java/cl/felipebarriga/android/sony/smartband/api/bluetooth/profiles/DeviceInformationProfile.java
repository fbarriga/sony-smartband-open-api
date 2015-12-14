package cl.felipebarriga.android.sony.smartband.api.bluetooth.profiles;

import java.util.UUID;

/**
 * Copyright (c) 2015 Felipe Barriga Richards. See Copyright Notice in LICENSE file.
 */
public class DeviceInformationProfile extends BaseProfile {
    public static final String CLASS = "DeviceInformationProfile";

    public static final UUID SERVICE_UUID;
    public static final UUID FIRMWARE_REVISION_UUID;
    public static final UUID HARDWARE_REVISION_UUID;
    public static final UUID SOFTWARE_REVISION_UUID;
    public static final UUID MANUFACTURER_NAME_UUID;

    static {
        SERVICE_UUID           = UUID.fromString( "0000180A-0000-1000-8000-00805F9B34FB" );
        FIRMWARE_REVISION_UUID = UUID.fromString( "00002A26-0000-1000-8000-00805F9B34FB" );
        HARDWARE_REVISION_UUID = UUID.fromString( "00002A27-0000-1000-8000-00805F9B34FB" );
        SOFTWARE_REVISION_UUID = UUID.fromString( "00002A28-0000-1000-8000-00805F9B34FB" );
        MANUFACTURER_NAME_UUID = UUID.fromString( "00002A29-0000-1000-8000-00805F9B34FB" );

        sUUIDNameMap.put( SERVICE_UUID          , CLASS                    );
        sUUIDNameMap.put( FIRMWARE_REVISION_UUID, "FIRMWARE_REVISION_UUID" );
        sUUIDNameMap.put( HARDWARE_REVISION_UUID, "HARDWARE_REVISION_UUID" );
        sUUIDNameMap.put( SOFTWARE_REVISION_UUID, "SOFTWARE_REVISION_UUID" );
        sUUIDNameMap.put( MANUFACTURER_NAME_UUID, "MANUFACTURER_NAME_UUID" );
    }

}
