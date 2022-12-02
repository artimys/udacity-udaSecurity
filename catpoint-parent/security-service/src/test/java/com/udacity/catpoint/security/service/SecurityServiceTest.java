package com.udacity.catpoint.security.service;

import com.udacity.catpoint.image.service.FakeImageService;
import com.udacity.catpoint.image.service.ImageService;
import com.udacity.catpoint.security.data.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class SecurityServiceTest {
    @Mock
    private SecurityRepository securityRepository;
    private ImageService imageService;
    private SecurityService securityService;


    @BeforeEach
    void init() {
        securityService = new SecurityService(securityRepository, imageService);
    }


    // Test 1
    // If alarm is armed and a sensor becomes activated, put the system into pending alarm status.
    @Test
    void alarmArmed_WithActivatedSensor_SetPendingStatus() {
        when(securityRepository.getArmingStatus()).thenReturn(ArmingStatus.ARMED_HOME);
        when(securityRepository.getAlarmStatus()).thenReturn(AlarmStatus.NO_ALARM);

        // `securityService.changeSensorActivationStatus()` will call private method
        // `securityRepository.handleSensorActivated()` to change status to pending alarm,
        // switch statement should choose `setAlarmStatus(AlarmStatus.PENDING_ALARM)`
        // which leads to calling securityRepository.setAlarmStatus(xxxx)

        // Create an active sensor
        Sensor doorSensor = new Sensor("12345", SensorType.DOOR);
        securityService.changeSensorActivationStatus(doorSensor, true);

//        Assertions.assertEquals(AlarmStatus.PENDING_ALARM, securityRepository.getAlarmStatus());
        verify(securityRepository).setAlarmStatus(AlarmStatus.PENDING_ALARM);
    }


    // Test 2
    // If alarm is armed and a sensor becomes activated and the system is already pending alarm, set the alarm status to alarm.
    @Test
    void alarmArmed_WhileSensorActivatedAndPendingStatus_SetAlarmStatus() {
        when(securityRepository.getArmingStatus()).thenReturn(ArmingStatus.ARMED_HOME);
        when(securityRepository.getAlarmStatus()).thenReturn(AlarmStatus.PENDING_ALARM);

        // Create an active sensor
        Sensor doorSensor = new Sensor("12345", SensorType.DOOR);
        securityService.changeSensorActivationStatus(doorSensor, true);

        verify(securityRepository).setAlarmStatus(AlarmStatus.ALARM);
    }


    // Test 3
    // If pending alarm and all sensors are inactive, return to no alarm state.
    @Test
    void pendingAlarm_AllSensorsInactive_ReturnNoAlarmStatus() {
        when(securityRepository.getAlarmStatus()).thenReturn(AlarmStatus.PENDING_ALARM);

        // Create deactivated sensors
        Sensor doorSensor = new Sensor("1", SensorType.DOOR);
        doorSensor.setActive(true);
//        Sensor motionSensor = new Sensor("2", SensorType.MOTION);
//        Sensor windowSensor = new Sensor("3", SensorType.WINDOW);
        securityService.changeSensorActivationStatus(doorSensor, false);
//        securityService.changeSensorActivationStatus(motionSensor, false);
//        securityService.changeSensorActivationStatus(windowSensor, true);
//        securityService.changeSensorActivationStatus(windowSensor, false);

//        assertTrue( true );
        verify(securityRepository).setAlarmStatus(AlarmStatus.NO_ALARM);
    }


    // Test 4
    // If alarm is active, change in sensor state should not affect the alarm state.
    @Test
    void alarmActive_ChangingSensorState_ReturnNoChangeInAlarmState() {
//        when(securityRepository.getArmingStatus()).thenReturn(ArmingStatus.ARMED_HOME);
        when(securityRepository.getAlarmStatus()).thenReturn(AlarmStatus.ALARM);

        // Create sensors
        Sensor doorSensor = new Sensor("1", SensorType.DOOR);
        doorSensor.setActive(false);
        securityService.changeSensorActivationStatus(doorSensor, false);

        verify(securityRepository).setAlarmStatus(AlarmStatus.ALARM);
    }




//    If a sensor is activated while already active and the system is in pending state, change it to alarm state.
//    If a sensor is deactivated while already inactive, make no changes to the alarm state.
//    If the image service identifies an image containing a cat while the system is armed-home, put the system into alarm status.
//    If the image service identifies an image that does not contain a cat, change the status to no alarm as long as the sensors are not active.
//    If the system is disarmed, set the status to no alarm.
//    If the system is armed, reset all sensors to inactive.
//    If the system is armed-home while the camera shows a cat, set the alarm status to alarm.
}
