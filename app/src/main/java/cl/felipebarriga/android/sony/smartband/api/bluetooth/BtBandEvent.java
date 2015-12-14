package cl.felipebarriga.android.sony.smartband.api.bluetooth;

import android.util.Log;

import cl.felipebarriga.android.sony.smartband.api.bluetooth.profiles.AHServiceProfile;

/**
 * Copyright (c) 2015 Felipe Barriga Richards. See Copyright Notice in LICENSE file.
 */

public class BtBandEvent {
    public static final String CLASS = "BtBandEvent";

    private interface EVENT_TYPE_CODE {
        int TAP = 0;
        int BUTTON = 1;
        int LIFE_BOOKMARK = 2;
        int MODE_SWITCH = 3;
        int HARDWARE_EVENT = 4;
        int UPDATE_TIME = 5;
    }

    enum EventType {
        UNKNOWN,
        TAP,
        BUTTON,
        LIFE_BOOKMARK,
        MODE_SWITCH,
        HARDWARE_EVENT,
        UPDATE_TIME
    }

    enum Event {
        UNKNOWN,
        LOW_MEMORY,
        LOW_BATTERY,
        TAP_SINGLE,
        TAP_DOUBLE,
        TAP_TRIPLE,
        BUTTON,
        LIFE_BOOKMARK,
        MODE_SWITCH_DAY,
        MODE_SWITCH_NIGHT,
        MODE_SWITCH_MEDIA,
        MODE_SWITCH_FIRMWARE,
        UPDATE_TIME
    }

    private final EventType mType;
    private final Event mEvent;
    private final long mTimestamp;

    public EventType getType() {
        return mType;
    }

    public Event getEvent() {
        return mEvent;
    }

    public long getTimestamp() {
        return mTimestamp;
    }

    public BtBandEvent( int code, int value, int payload ) {

        if( code == EVENT_TYPE_CODE.LIFE_BOOKMARK ) {
            mType = EventType.LIFE_BOOKMARK;
            mEvent = Event.LIFE_BOOKMARK;
            mTimestamp = AHServiceProfile.convertBandSecondsToTimestamp( payload );
            return;
        }

        mTimestamp = 0;

        // Button + TAP will enable media mode
        // On media mode taps are detected without problems
        if( code == EVENT_TYPE_CODE.TAP ) {
            mType = EventType.TAP;
            if( value == AHServiceProfile.TAP_EVENT_TYPE.SINGLE ) {
                mEvent = Event.TAP_SINGLE;
            } else if( value == AHServiceProfile.TAP_EVENT_TYPE.DOUBLE ) {
                mEvent = Event.TAP_DOUBLE;
            } else if( value == AHServiceProfile.TAP_EVENT_TYPE.TRIPLE ) {
                mEvent = Event.TAP_TRIPLE;
            } else {
                mEvent = Event.UNKNOWN;
                Log.e( CLASS, "BtBandEvent: Unknown kind of TAP. value: " + value );
            }

            return;
        }

        if( code == EVENT_TYPE_CODE.BUTTON ) {
            mType = EventType.BUTTON;
            mEvent = Event.BUTTON;
            return;
        }

        if( code == EVENT_TYPE_CODE.MODE_SWITCH ) {
            mType = EventType.MODE_SWITCH;
            BandMode.AccessoryMode mode = new BandMode( value ).getMode();
            switch( mode ) {
                case DAY:
                    mEvent = Event.MODE_SWITCH_DAY;
                    break;

                case NIGHT:
                    mEvent = Event.MODE_SWITCH_NIGHT;
                    break;

                case MEDIA:
                    mEvent = Event.MODE_SWITCH_MEDIA;
                    break;

                case FIRMWARE_UPDATE:
                    mEvent = Event.MODE_SWITCH_FIRMWARE;
                    break;

                case UNKNOWN:
                default:
                    Log.e( CLASS, "BtBandEvent: Switched to unknown mode: " + value );
                    mEvent = Event.UNKNOWN;
                    break;
            }
            return;
        }

        byte LOW_MEMORY_MASK  = 0b00000001;
        byte LOW_BATTERY_MASK = 0b00000010;
        if( code == EVENT_TYPE_CODE.HARDWARE_EVENT ) {
            mType = EventType.HARDWARE_EVENT;
            if( ( value & LOW_BATTERY_MASK ) > 0 ) {
                mEvent = Event.LOW_BATTERY;
            } else if( ( value & LOW_MEMORY_MASK ) <= 0 ) {
                mEvent = Event.LOW_MEMORY;
            } else {
                mEvent = Event.UNKNOWN;
                Log.e( CLASS, "BtBandEvent: Unknown hardware event: " + value );
            }
            return;
        }

        if( code == EVENT_TYPE_CODE.UPDATE_TIME ) {
            mType = EventType.UPDATE_TIME;
            mEvent = Event.UPDATE_TIME;
            Log.d( CLASS, "readEvent: The device is requesting that we send a time update" );
            return;
        }

        mType = EventType.UNKNOWN;
        mEvent = Event.UNKNOWN;

        Log.e( CLASS, "BtBandEvent: Unknown mode: " + code );
    }
}
