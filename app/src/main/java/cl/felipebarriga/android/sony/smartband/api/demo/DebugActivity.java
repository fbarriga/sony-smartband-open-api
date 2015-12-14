package cl.felipebarriga.android.sony.smartband.api.demo;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import cl.felipebarriga.android.sony.smartband.api.bluetooth.BandMode;
import cl.felipebarriga.android.sony.smartband.api.bluetooth.BtAction;
import cl.felipebarriga.android.sony.smartband.api.bluetooth.BtBand;
import cl.felipebarriga.android.sony.smartband.api.bluetooth.BtBandEvent;
import cl.felipebarriga.android.sony.smartband.api.bluetooth.BtBandListener;
import cl.felipebarriga.android.sony.smartband.api.bluetooth.BtBridge;
import cl.felipebarriga.android.sony.smartband.api.rawband.R;

/**
 * Copyright (c) 2015 Felipe Barriga Richards. See Copyright Notice in LICENSE file.
 */
public class DebugActivity extends Activity implements BtBandListener {
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

        SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
        updateUILabel( RowId.CURRENT_TIME, sdf.format( deviceDate ) );
    }

    @Override
    public void onAHServiceReadProtocolVersion( int version ) {
        Log.d( CLASS, "onAHServiceReadProtocolVersion: version=" + version );
        updateUILabel( RowId.PROTOCOL_VERSION, String.valueOf( version ) );
    }

    @Override
    public void onAHServiceReadData( ) {
        Log.d( CLASS, "onAHServiceReadData: called." );
    }

    @Override
    public void onAHServiceReadMode( BandMode.AccessoryMode mode ) {
        Log.d( CLASS, "onAHServiceReadMode: mode=" + mode.name() );
        updateUILabel( RowId.CURRENT_MODE, mode.name() );
    }

    @Override
    public void onAHServiceReadUptime( int uptime, Date started ) {
        Log.d( CLASS, "onAHServiceReadUptime: started=" + started + " uptime=" + uptime );
        updateUILabel( RowId.UPTIME, String.valueOf( uptime ) );
    }

    @Override
    public void onBatteryLevel( int level ) {
        Log.d( CLASS, "onBatteryLevel: level=" + level );
        updateUILabel( RowId.BATTERY_LEVEL, String.valueOf( level ) );
    }

    @Override
    public void onFirmwareRevision( String rev ) {
        updateUILabel( RowId.FIRMWARE_REVISION, rev );
    }

    @Override
    public void onSoftwareRevision( String rev ) {
        updateUILabel( RowId.SOFTWARE_REVISION, rev );
    }

    @Override
    public void onHardwareRevision( String rev ) {
        updateUILabel( RowId.HARDWARE_REVISION, rev );
    }

    @Override
    public void onManufacturerName( String name ) {
        updateUILabel( RowId.MANUFACTURER_NAME, name );
    }

    @Override
    public void onAASDeviceVersion( int version ) {
        updateUILabel( RowId.AAS_VERSION, String.valueOf( version ) );
    }

    @Override
    public void onAASDeviceSmartLink( int smartLink ) {
        updateUILabel( RowId.AAS_SMART_LINK, String.valueOf( smartLink ) );
    }

    @Override
    public void onAASDeviceProductId( String productId ) {
        updateUILabel( RowId.AAS_PRODUCT_ID, productId );
    }

    @Override
    public void onAASDeviceData( String data ) {
        updateUILabel( RowId.AAS_DATA, data );
    }

    @Override
    public void onGADeviceName( String name ) {
        updateUILabel( RowId.GA_DEVICE_NAME, name );
    }

    @Override
    public void onConnectionStateChange( final BtBridge.Status status ) {
        Log.i( CLASS, "onConnectionStateChange: status=" + status.name() );
        updateUILabel( RowId.STATUS, status.name() );
        if( status == BtBridge.Status.CONNECTED ) {
            updateUIButton( RowId.STATUS, true, "Disconnect" );
        } else if( status == BtBridge.Status.DISCONNECTED ) {
            updateUIButton( RowId.STATUS, true, "Connect" );
        } else {
            updateUIButton( RowId.STATUS, false, "Disconnect" );
        }
    }

    @Override
    public void onServicesDiscovered() {
        Log.i( CLASS, "onServicesDiscovered: called" );
    }

    public enum RowId {
        ENABLE_EVENT_NOTIFICATIONS,
        ENABLE_DATA_NOTIFICATIONS,
        ENABLE_ACC_NOTIFICATIONS,
        ENABLE_DEBUG_NOTIFICATIONS,
        SEND_CURRENT_TIME,
        STATUS,
        UPTIME,
        CURRENT_MODE,
        BATTERY_LEVEL,
        CURRENT_TIME,
        PROTOCOL_VERSION,
        FIRMWARE_REVISION,
        SOFTWARE_REVISION,
        HARDWARE_REVISION,
        MANUFACTURER_NAME,
        AAS_VERSION,
        AAS_SMART_LINK,
        AAS_PRODUCT_ID,
        AAS_DATA,
        GA_DEVICE_NAME
    }

    public static final HashMap<RowId, BtAction.Action> mRowActionMap = new HashMap<>(  );

    static {
        mRowActionMap.put( RowId.BATTERY_LEVEL    , BtAction.Action.REQUEST_BATTERY_LEVEL     );

        mRowActionMap.put( RowId.UPTIME           , BtAction.Action.REQUEST_UPTIME            );
        mRowActionMap.put( RowId.CURRENT_MODE     , BtAction.Action.REQUEST_MODE              );
        mRowActionMap.put( RowId.CURRENT_TIME     , BtAction.Action.REQUEST_CURRENT_TIME      );
        mRowActionMap.put( RowId.PROTOCOL_VERSION , BtAction.Action.REQUEST_PROTOCOL_VERSION  );

        mRowActionMap.put( RowId.FIRMWARE_REVISION, BtAction.Action.REQUEST_FIRMWARE_REVISION );
        mRowActionMap.put( RowId.SOFTWARE_REVISION, BtAction.Action.REQUEST_SOFTWARE_REVISION );
        mRowActionMap.put( RowId.HARDWARE_REVISION, BtAction.Action.REQUEST_HARDWARE_REVISION );
        mRowActionMap.put( RowId.MANUFACTURER_NAME, BtAction.Action.REQUEST_MANUFACTURER_NAME );

        mRowActionMap.put( RowId.AAS_VERSION      , BtAction.Action.REQUEST_AAS_VERSION       );
        mRowActionMap.put( RowId.AAS_SMART_LINK   , BtAction.Action.REQUEST_AAS_SMART_LINK    );
        mRowActionMap.put( RowId.AAS_PRODUCT_ID   , BtAction.Action.REQUEST_AAS_PRODUCT_ID    );
        mRowActionMap.put( RowId.AAS_DATA         , BtAction.Action.REQUEST_AAS_DEVICE_DATA   );

        mRowActionMap.put( RowId.GA_DEVICE_NAME   , BtAction.Action.REQUEST_GA_DEVICE_NAME    );
    }

    private void updateUILabel( final RowId rowId, final String textValue ) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                TextView textView = mRowIdValueTextViewMap.get( rowId );
                if( textView == null ) {
                    Log.e( CLASS, "updateUILabel: Error, view not found. rowId=" + rowId.name() );
                    return;
                }

                textView.setText( textValue );
            }
        });
    }

    private void updateUIButton( final RowId rowId, final boolean enabled, final String textValue ) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Button btn = mRowIdButtonMap.get( rowId );
                if( btn == null ) {
                    Log.e( CLASS, "updateUIButton: Error, btn not found. rowId=" + rowId.name() );
                    return;
                }
                btn.setEnabled( enabled );
                btn.setText( textValue );
            }
        });
    }

    private static HashMap<RowId,Button> mRowIdButtonMap = new HashMap<>( );
    private static HashMap<RowId,TextView> mRowIdValueTextViewMap = new HashMap<>( );
    private static HashMap<View,RowId> mViewRowIdMap = new HashMap<>(  );

    private TableRow createRow( RowId rowId, String label, String defaultValue,
                                String btnText, View.OnClickListener btnListener ) {

        TableRow tb = new TableRow( this );
        TextView labelText = new TextView( this );
        TextView valueText = new TextView( this );
        Button btn = new Button( this );

        labelText.setText( label );
        valueText.setText( defaultValue );
        btn.setText( btnText );

        btn.setOnClickListener( btnListener );

        tb.addView( labelText );
        tb.addView( valueText );
        tb.addView( btn );

        mRowIdButtonMap.put( rowId, btn );
        mRowIdValueTextViewMap.put( rowId, valueText );
        mViewRowIdMap.put( btn, rowId );

        return tb;
    }

    private View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick( View v ) {
            RowId rowId = mViewRowIdMap.get( v );
            TextView valueTextView = mRowIdValueTextViewMap.get( rowId );
            if( rowId == RowId.STATUS ) {
                switch( mBtBand.getStatus() ) {
                    case DISCONNECTED:
                        try {
                            mBtBand.connect();
                        } catch( Exception e ) {
                            e.printStackTrace();
                        }
                        break;

                    case CONNECTED:
                        mBtBand.disconnect();
                        break;

                    case CONNECTING:
                        break;
                }
                valueTextView.setText( mBtBand.getStatus().name() );
                return;
            }

            if( !mBtBand.getStatus().equals( BtBridge.Status.CONNECTED ) ) {
                Log.e( CLASS, "onClick: " + rowId.name() + " not connected" );
                return;
            }

            if( rowId == RowId.SEND_CURRENT_TIME ) {
                mBtBand.syncDeviceTime();
                return;
            }

            if( rowId == RowId.ENABLE_EVENT_NOTIFICATIONS ) {
                if( mBtBand.isEventNotificationsEnabled() ) {
                    mBtBand.addRequest( BtAction.Action.DISABLE_EVENT_NOTIFICATIONS );
                    valueTextView.setText( "Disable" );
                } else {
                    mBtBand.addRequest( BtAction.Action.ENABLE_EVENT_NOTIFICATIONS );
                    valueTextView.setText( "Enable" );
                }
                return;
            }

            if( rowId == RowId.ENABLE_DATA_NOTIFICATIONS ) {
                if( mBtBand.isDataNotificationsEnabled() ) {
                    mBtBand.addRequest( BtAction.Action.DISABLE_DATA_NOTIFICATIONS );
                    valueTextView.setText( "Disable" );
                } else {
                    mBtBand.addRequest( BtAction.Action.ENABLE_DATA_NOTIFICATIONS );
                    valueTextView.setText( "Enable" );
                }
                return;
            }

            if( rowId == RowId.ENABLE_ACC_NOTIFICATIONS ) {
                if( mBtBand.isAccNotificationsEnabled() ) {
                    mBtBand.addRequest( BtAction.Action.DISABLE_ACC_NOTIFICATIONS );
                    valueTextView.setText( "Disable" );
                } else {
                    mBtBand.addRequest( BtAction.Action.ENABLE_ACC_NOTIFICATIONS );
                    valueTextView.setText( "Enable" );
                }
                return;
            }

            if( rowId == RowId.ENABLE_DEBUG_NOTIFICATIONS ) {
                if( mBtBand.isDebugNotificationsEnabled() ) {
                    mBtBand.addRequest( BtAction.Action.DISABLE_DEBUG_NOTIFICATIONS );
                    valueTextView.setText( "Disable" );
                } else {
                    mBtBand.addRequest( BtAction.Action.ENABLE_DEBUG_NOTIFICATIONS );
                    valueTextView.setText( "Enable" );
                }
                return;
            }

            BtAction.Action action = mRowActionMap.get( rowId );
            if( rowId != null ) {
                mBtBand.addRequest( action );
                return;
            }

            switch( rowId ) {

                default:
                    Log.e( CLASS, "onClick: unknown rowId: " + rowId.name() );
                    break;
            }

        }
    };

    private BtBand mBtBand;
    private Handler mHandler;

    private void uiPopulateRows( TableLayout tl ) {
        tl.addView( createRow( RowId.STATUS          , "Status:"          , "-", "Connect", mClickListener ) );
        tl.addView( createRow( RowId.UPTIME          , "Uptime:"          , "-", "Update" , mClickListener ) );
        tl.addView( createRow( RowId.CURRENT_MODE    , "Current Mode:"    , "-", "Update" , mClickListener ) );
        tl.addView( createRow( RowId.PROTOCOL_VERSION, "Protocol Version:", "-", "Update" , mClickListener ) );
        tl.addView( createRow( RowId.CURRENT_TIME    , "Current Time:"    , "-", "Update" , mClickListener ) );

        tl.addView( createRow( RowId.BATTERY_LEVEL, "Battery Level:", "-", "Update" , mClickListener ) );

        tl.addView( createRow( RowId.FIRMWARE_REVISION, "Firmware Rev.:", "-", "Update", mClickListener ) );
        tl.addView( createRow( RowId.HARDWARE_REVISION, "Hardware Rev.:", "-", "Update", mClickListener ) );
        tl.addView( createRow( RowId.SOFTWARE_REVISION, "Software Rev.:", "-", "Update", mClickListener ) );
        tl.addView( createRow( RowId.MANUFACTURER_NAME, "Manufacturer:" , "-", "Update", mClickListener ) );

        tl.addView( createRow( RowId.AAS_VERSION   , "AAS Version:"  , "-", "Update", mClickListener ) );
        tl.addView( createRow( RowId.AAS_SMART_LINK, "AAS SmartLink:", "-", "Update", mClickListener ) );
        tl.addView( createRow( RowId.AAS_PRODUCT_ID, "AAS ProductId:", "-", "Update", mClickListener ) );
        tl.addView( createRow( RowId.AAS_DATA      , "AAS Data:"     , "-", "Update", mClickListener ) );

        tl.addView( createRow( RowId.GA_DEVICE_NAME, "GA Device Name:", "-", "Update", mClickListener ) );

        tl.addView( createRow( RowId.SEND_CURRENT_TIME, "Send Current Time:", "-", "Update", mClickListener ) );

        tl.addView( createRow( RowId.ENABLE_EVENT_NOTIFICATIONS, "Event Notifications:" , "-", "Enable", mClickListener ) );
        tl.addView( createRow( RowId.ENABLE_DATA_NOTIFICATIONS , "Data Notifications:"  , "-", "Enable", mClickListener ) );
        tl.addView( createRow( RowId.ENABLE_ACC_NOTIFICATIONS  , "Accelerometer Notif.:", "-", "Enable", mClickListener ) );
        tl.addView( createRow( RowId.ENABLE_DEBUG_NOTIFICATIONS, "Debug Notifications:" , "-", "Enable", mClickListener ) );
    }

    private void uiAddModeRow(TableLayout tl ) {
        TableRow tb = new TableRow( this );
        TextView labelText = new TextView( this );

        Button btnDay = new Button( this );
        Button btnNight = new Button( this );
        Button btnMedia = new Button( this );
        Button btnFirmware = new Button( this );

        labelText.setText( "Mode:" );

        btnDay.setText( "Day" );
        btnNight.setText( "Night" );
        btnMedia.setText( "Media" );
        btnFirmware.setText( "Firmware" );

        btnDay.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                mBtBand.addRequest( BtAction.Action.SET_MODE, BandMode.getInt( BandMode.AccessoryMode.DAY ) );
            }
        } );

        btnNight.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                mBtBand.addRequest( BtAction.Action.SET_MODE, BandMode.getInt( BandMode.AccessoryMode.NIGHT ) );
            }
        } );

        btnMedia.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                mBtBand.addRequest( BtAction.Action.SET_MODE, BandMode.getInt( BandMode.AccessoryMode.MEDIA ) );
            }
        } );

        btnFirmware.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                mBtBand.addRequest( BtAction.Action.SET_MODE, BandMode.getInt( BandMode.AccessoryMode.FIRMWARE_UPDATE ) );
            }
        } );

        tb.addView( labelText );
        tb.addView( btnDay );
        tb.addView( btnNight );
        tb.addView( btnMedia );
        tb.addView( btnFirmware );
        tl.addView( tb );
    }

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        Log.v( CLASS, "onCreate: called." );

        mHandler = new Handler(  );
        mBtBand = new BtBand( this );
        mBtBand.addListener( this );
        setContentView( R.layout.activity_debug );

        TableLayout tl = ( TableLayout ) findViewById( R.id.debug_table_layout );
        uiPopulateRows( tl );
        uiAddModeRow( tl );
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
