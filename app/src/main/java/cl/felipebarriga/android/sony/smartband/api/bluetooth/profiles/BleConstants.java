package cl.felipebarriga.android.sony.smartband.api.bluetooth.profiles;

import java.util.HashMap;
import java.util.UUID;

/**
 * Copyright (c) 2015 Felipe Barriga Richards. See Copyright Notice in LICENSE file.
 */
public class BleConstants {
    public static final UUID CLIENT_CHARACTERISTIC_CONFIGURATION;

    private static HashMap<UUID, String> sUUIDNameMap;

    static {
        CLIENT_CHARACTERISTIC_CONFIGURATION = UUID.fromString( "00002902-0000-1000-8000-00805F9B34FB" );

        sUUIDNameMap = new HashMap();
        sUUIDNameMap.put( CLIENT_CHARACTERISTIC_CONFIGURATION, "CLIENT_CHARACTERISTIC_CONFIGURATION" );
    }

    public static String getUuidName( UUID uuid ) {
        return sUUIDNameMap.get( uuid );
    }

}
