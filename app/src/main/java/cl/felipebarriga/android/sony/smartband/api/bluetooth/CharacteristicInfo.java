package cl.felipebarriga.android.sony.smartband.api.bluetooth;

/**
 * Copyright (c) 2015 Felipe Barriga Richards. See Copyright Notice in LICENSE file.
 */
public class CharacteristicInfo {
    final String mServiceName;
    final String mCharacteristicName;

    CharacteristicInfo( String serviceName, String characteristicName ) {
        mServiceName = serviceName;
        mCharacteristicName = characteristicName;
    }

    public String toString() {
        return "service=" + mServiceName + " name=" + mCharacteristicName;
    }
}
