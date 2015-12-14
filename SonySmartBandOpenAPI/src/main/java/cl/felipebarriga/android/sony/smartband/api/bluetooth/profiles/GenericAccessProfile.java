package cl.felipebarriga.android.sony.smartband.api.bluetooth.profiles;

import java.util.UUID;

/**
 * Copyright (c) 2015 Felipe Barriga Richards. See Copyright Notice in LICENSE file.
 */
public class GenericAccessProfile extends BaseProfile {
    public static final String CLASS = "GenericAccessProfile";

    public static final UUID SERVICE_UUID;
    public static final UUID DEVICE_NAME_UUID;

    static {
        SERVICE_UUID     = UUID.fromString( "00001800-0000-1000-8000-00805F9B34FB" );
        DEVICE_NAME_UUID = UUID.fromString( "00002A00-0000-1000-8000-00805F9B34FB" );

        sUUIDNameMap.put( SERVICE_UUID, CLASS );
        sUUIDNameMap.put( DEVICE_NAME_UUID, "DEVICE_NAME_UUID" );
    }
}
