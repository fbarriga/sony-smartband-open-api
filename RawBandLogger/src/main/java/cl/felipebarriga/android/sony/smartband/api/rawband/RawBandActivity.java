package cl.felipebarriga.android.sony.smartband.api.rawband;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import java.util.Date;

import cl.felipebarriga.android.sony.smartband.api.bluetooth.BandMode;
import cl.felipebarriga.android.sony.smartband.api.bluetooth.BtBand;
import cl.felipebarriga.android.sony.smartband.api.bluetooth.BtBandEvent;
import cl.felipebarriga.android.sony.smartband.api.bluetooth.BtBandListener;
import cl.felipebarriga.android.sony.smartband.api.bluetooth.BtBridge;

/**
 * Copyright (c) 2015 Felipe Barriga Richards. See Copyright Notice in LICENSE file.
 */
public class RawBandActivity extends Activity implements BtBandListener {
    private final String CLASS = getClass().getSimpleName();

    @Override
    public void onAHServiceReadAccData( int[] data ) {
        Log.d( CLASS, "onAHServiceReadAccData: data=[" + data[0] + "," + data[1] + "," + data[2] + "]" );
    }

    @Override
    public void onAHServiceReadEvent( BtBandEvent event ) {
        Log.d( CLASS, "onAHServiceReadEvent: event=" + event );
    }

    @Override
    public void onAHServiceReadCurrentTime( int bandSeconds, Date deviceDate, long deltaMs ) {
        Log.d( CLASS, "onAHServiceReadCurrentTime: bandSeconds=" + bandSeconds
                +" deviceDate=" + deviceDate + " deltaMs=" + deltaMs );

//        SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
//        updateUILabel( RowId.CURRENT_TIME, sdf.format( deviceDate ) );
    }

    @Override
    public void onAHServiceReadProtocolVersion( int version ) {
        Log.d( CLASS, "onAHServiceReadProtocolVersion: version=" + version );
//        updateUILabel( RowId.PROTOCOL_VERSION, String.valueOf( version ) );
    }

    @Override
    public void onAHServiceReadData( ) {
        Log.d( CLASS, "onAHServiceReadData: called." );
    }

    @Override
    public void onAHServiceReadMode( BandMode.AccessoryMode mode ) {
        Log.d( CLASS, "onAHServiceReadMode: mode=" + mode.name() );
//        updateUILabel( RowId.CURRENT_MODE, mode.name() );
    }

    @Override
    public void onAHServiceReadUptime( int uptime, Date started ) {
        Log.d( CLASS, "onAHServiceReadUptime: started=" + started + " uptime=" + uptime );
//        updateUILabel( RowId.UPTIME, String.valueOf( uptime ) );
    }

    @Override
    public void onBatteryLevel( int level ) {
        Log.d( CLASS, "onBatteryLevel: level=" + level );
//        updateUILabel( RowId.BATTERY_LEVEL, String.valueOf( level ) );
    }

    @Override
    public void onFirmwareRevision( String rev ) {
//        updateUILabel( RowId.FIRMWARE_REVISION, rev );
    }

    @Override
    public void onSoftwareRevision( String rev ) {
//        updateUILabel( RowId.SOFTWARE_REVISION, rev );
    }

    @Override
    public void onHardwareRevision( String rev ) {
//        updateUILabel( RowId.HARDWARE_REVISION, rev );
    }

    @Override
    public void onManufacturerName( String name ) {
//        updateUILabel( RowId.MANUFACTURER_NAME, name );
    }

    @Override
    public void onAASDeviceVersion( int version ) {
//        updateUILabel( RowId.AAS_VERSION, String.valueOf( version ) );
    }

    @Override
    public void onAASDeviceSmartLink( int smartLink ) {
//        updateUILabel( RowId.AAS_SMART_LINK, String.valueOf( smartLink ) );
    }

    @Override
    public void onAASDeviceProductId( String productId ) {
//        updateUILabel( RowId.AAS_PRODUCT_ID, productId );
    }

    @Override
    public void onAASDeviceData( String data ) {
//        updateUILabel( RowId.AAS_DATA, data );
    }

    @Override
    public void onGADeviceName( String name ) {
//        updateUILabel( RowId.GA_DEVICE_NAME, name );
    }

    @Override
    public void onConnectionStateChange( final BtBridge.Status status ) {
        Log.i( CLASS, "onConnectionStateChange: status=" + status.name() );
        if( status == BtBridge.Status.CONNECTED ) {
//            updateUIButton( RowId.STATUS, true, "Disconnect" );
        } else if( status == BtBridge.Status.DISCONNECTED ) {
//            updateUIButton( RowId.STATUS, true, "Connect" );
        } else {
//            updateUIButton( RowId.STATUS, false, "Disconnect" );
        }
    }

    @Override
    public void onServicesDiscovered() {
        Log.i( CLASS, "onServicesDiscovered: called" );
    }

    private BtBand mBtBand;
    private Handler mHandler;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        Log.v( CLASS, "onCreate: called." );

//        mHandler = new Handler(  );
//        mBtBand = new BtBand( this );
//        mBtBand.addListener( this );
        setContentView( R.layout.activity_rawband );
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.v( CLASS, "onStart: called." );
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v( CLASS, "onResume: called." );
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v( CLASS, "onPause: called." );
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.v( CLASS, "onRestart: called." );
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.v( CLASS, "onStop: called." );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v( CLASS, "onDestroy: called." );
        mBtBand.disconnect();
    }


}
