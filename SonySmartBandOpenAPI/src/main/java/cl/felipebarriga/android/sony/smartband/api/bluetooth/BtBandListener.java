package cl.felipebarriga.android.sony.smartband.api.bluetooth;

import java.util.Date;

import cl.felipebarriga.android.sony.smartband.api.bluetooth.profiles.AHServiceProfile;

/**
 * Copyright (c) 2015 Felipe Barriga Richards. See Copyright Notice in LICENSE file.
 */
public interface BtBandListener {

    void onAHServiceReadAccData( int[] data );
    void onAHServiceReadCurrentTime( int bandSeconds, Date deviceDate, long deltaMs );
    void onAHServiceReadData( );
    void onAHServiceReadEvent( BtBandEvent event );
    void onAHServiceReadMode( BandMode.AccessoryMode mode );
    void onAHServiceReadProtocolVersion( int version );
    void onAHServiceReadUptime( int uptime, Date started );

    void onBatteryLevel( int level );

    void onFirmwareRevision( String rev );
    void onSoftwareRevision( String rev );
    void onHardwareRevision( String rev );
    void onManufacturerName( String name );

    void onAASDeviceVersion( int version );
    void onAASDeviceSmartLink( int version );
    void onAASDeviceProductId( String productId );
    void onAASDeviceData( String data );

    void onGADeviceName( String data );

    void onConnectionStateChange( BtBridge.Status status );
    void onServicesDiscovered();
}
