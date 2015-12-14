package cl.felipebarriga.android.sony.smartband.api.bluetooth;

import android.util.Log;

import java.util.HashMap;
import java.util.UUID;

import cl.felipebarriga.android.sony.smartband.api.bluetooth.profiles.AADeviceServiceProfile;
import cl.felipebarriga.android.sony.smartband.api.bluetooth.profiles.AHServiceProfile;
import cl.felipebarriga.android.sony.smartband.api.bluetooth.profiles.BaseProfile;
import cl.felipebarriga.android.sony.smartband.api.bluetooth.profiles.BatteryProfile;
import cl.felipebarriga.android.sony.smartband.api.bluetooth.profiles.DeviceInformationProfile;
import cl.felipebarriga.android.sony.smartband.api.bluetooth.profiles.GenericAccessProfile;

/**
 * Copyright (c) 2015 Felipe Barriga Richards. See Copyright Notice in LICENSE file.
 */
public class Profiles {
    private static final String CLASS = "Profiles";
    private static HashMap<UUID, BaseProfile> sUUIDProfileMap;

    static {
        sUUIDProfileMap = new HashMap();
        sUUIDProfileMap.put( BatteryProfile.SERVICE_UUID          , new BatteryProfile()           );
        sUUIDProfileMap.put( AHServiceProfile.SERVICE_UUID        , new AHServiceProfile()         );
        sUUIDProfileMap.put( AADeviceServiceProfile.SERVICE_UUID  , new AADeviceServiceProfile()   );
        sUUIDProfileMap.put( DeviceInformationProfile.SERVICE_UUID, new DeviceInformationProfile() );
        sUUIDProfileMap.put( GenericAccessProfile.SERVICE_UUID    , new GenericAccessProfile()     );
    }

    public static BaseProfile getService( UUID uuid ) {
        Log.v( CLASS, "getService: uuid=" + uuid.toString() );
        Object profile = sUUIDProfileMap.get( uuid );
        if( profile == null ) {
            Log.w( CLASS, "getService: Profile not found for uuid=" + uuid.toString() );
            return null;
        }

        return ( BaseProfile ) profile;
    }
}
