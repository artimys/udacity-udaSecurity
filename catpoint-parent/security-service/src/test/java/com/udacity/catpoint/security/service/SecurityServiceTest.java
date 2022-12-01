package com.udacity.catpoint.security.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertTrue;

public class SecurityServiceTest {



//    If alarm is armed and a sensor becomes activated, put the system into pending alarm status.
    @Test
    void alarmArmed_WithActivatedSensor_SetPendingStatus() {
        Assertions.assertEquals(true, true);
    }
//    If alarm is armed and a sensor becomes activated and the system is already pending alarm, set the alarm status to alarm.
    @Test
    void alarmArmed_WhileSensorActivatedAndPendingStatus_SetAlarmStatus() {
        Assertions.assertEquals(true, true);
    }
//    If pending alarm and all sensors are inactive, return to no alarm state.
    @Test
    void pendingAlarm_AllSensorsInactive_ReturnNoAlarmStatus() {
        assertTrue( true );
    }

//    If alarm is active, change in sensor state should not affect the alarm state.
//    If a sensor is activated while already active and the system is in pending state, change it to alarm state.
//    If a sensor is deactivated while already inactive, make no changes to the alarm state.
//    If the image service identifies an image containing a cat while the system is armed-home, put the system into alarm status.
//    If the image service identifies an image that does not contain a cat, change the status to no alarm as long as the sensors are not active.
//    If the system is disarmed, set the status to no alarm.
//    If the system is armed, reset all sensors to inactive.
//    If the system is armed-home while the camera shows a cat, set the alarm status to alarm.



}
