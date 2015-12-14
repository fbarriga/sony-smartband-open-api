package cl.felipebarriga.android.sony.smartband.api.bluetooth;

/**
 * Copyright (c) 2015 Felipe Barriga Richards. See Copyright Notice in LICENSE file.
 */
public class DescriptorInfo {
    final String mServiceName;
    final String mCharacteristicName;
    final String mDescriptorName;

    DescriptorInfo( String serviceName, String characteristicName, String descriptorName ) {
        mServiceName = serviceName;
        mCharacteristicName = characteristicName;
        mDescriptorName = descriptorName;
    }

    public String toString() {
        return "service=" + mServiceName + " name=" + mCharacteristicName + " descriptor=" + mDescriptorName;
    }
}
