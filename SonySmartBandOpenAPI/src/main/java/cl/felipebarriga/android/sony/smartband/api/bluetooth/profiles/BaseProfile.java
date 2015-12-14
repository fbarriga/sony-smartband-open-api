package cl.felipebarriga.android.sony.smartband.api.bluetooth.profiles;

import java.util.HashMap;
import java.util.UUID;

/**
 * Copyright (c) 2015 Felipe Barriga Richards. See Copyright Notice in LICENSE file.
 */
public abstract class BaseProfile {
    protected final static HashMap<UUID, String> sUUIDNameMap;

    static {
        sUUIDNameMap = new HashMap<>(  );
    }

    public final HashMap<UUID, String> getsUUIDNameMap() {
        return sUUIDNameMap;
    }

    public final String getUuidName( UUID uuid ) {
        return sUUIDNameMap.get( uuid );
    }

}
