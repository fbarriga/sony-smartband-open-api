package cl.felipebarriga.android.sony.smartband.api.bluetooth;

import android.util.Log;

/**
 * Copyright (c) 2015 Felipe Barriga Richards. See Copyright Notice in LICENSE file.
 */
public class BandMode {
    public final static String CLASS = "BandMode";

    public enum AccessoryMode {
        DAY,
        NIGHT,
        MEDIA,
        FIRMWARE_UPDATE,
        UNKNOWN
    }

    private interface ACCESSORY_MODE {
        int DAY = 0;
        int NIGHT = 1;
        int MEDIA = 2;
        int FIRMWARE_UPDATE = 3;
    }

    private final AccessoryMode mMode;
    public BandMode( int mode ) {
        mMode = getMode( mode );
    }

    public static AccessoryMode getMode( int _mode ) {
        AccessoryMode mode;
        if( _mode == ACCESSORY_MODE.DAY ) {
            mode = AccessoryMode.DAY;
        } else if( _mode == ACCESSORY_MODE.NIGHT ) {
            mode = AccessoryMode.NIGHT;
        } else if( _mode == ACCESSORY_MODE.MEDIA ) {
            mode = AccessoryMode.MEDIA;
        } else if ( _mode == ACCESSORY_MODE.FIRMWARE_UPDATE ) {
            mode = AccessoryMode.FIRMWARE_UPDATE;
        } else {
            mode = AccessoryMode.UNKNOWN;
            Log.e( CLASS, "getMode: unknown mode: mode=" + _mode );
        }
        return mode;
    }

    public AccessoryMode getMode() {
        return mMode;
    }

    static public int getInt( AccessoryMode mode ) {
        if( AccessoryMode.DAY.equals( mode ) ) {
            return ACCESSORY_MODE.DAY;
        } else if( AccessoryMode.NIGHT.equals( mode ) ) {
            return ACCESSORY_MODE.NIGHT;
        } else if( AccessoryMode.MEDIA.equals( mode ) ) {
            return ACCESSORY_MODE.MEDIA;
        } else if( AccessoryMode.FIRMWARE_UPDATE.equals( mode ) ) {
            return ACCESSORY_MODE.FIRMWARE_UPDATE;
        }

        Log.e( CLASS, "getInt: unknown mode: mode=" + mode );
        return -1;
    }

    public int getInt() {
        return getInt( mMode );
    }


}
