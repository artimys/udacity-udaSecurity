package com.udacity.catpoint.security.service;

import com.udacity.catpoint.image.service.ImageService;
import com.udacity.catpoint.security.data.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class SecurityServiceTest {
    @Mock
    private SecurityRepository securityRepository;

    @Mock
    private ImageService imageService;

    @Mock
    private BufferedImage image;

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
        // Test 4: The user can reproduce this behavior when at least two sensors are added.
        // When going to the system in ARMED_HOME or ARMED_AWAY mode,
        // if user activates the first sensor it causes the alarm to go to the PENDING_ALARM state,
        // and when activating the second sensor, the system goes to the ALARM state.
        // Now, any change in the sensor state should not change the status of the alarm from ALARM state.
        // It makes sense because the system is already telling the user that they are in danger, so no change in sensors should stop this behavior.
/*
//        securityRepository.setArmingStatus(ArmingStatus.ARMED_HOME);
//        securityRepository.setAlarmStatus(AlarmStatus.NO_ALARM);
        //when(securityRepository.getArmingStatus()).thenReturn(ArmingStatus.ARMED_HOME);
        when(securityRepository.getAlarmStatus()).thenReturn(AlarmStatus.NO_ALARM);

        // Create sensor 1
        Sensor doorSensor = new Sensor("1", SensorType.DOOR);
        doorSensor.setActive(false);

        // Create sensor 2
        Sensor windowSensor = new Sensor("2", SensorType.WINDOW);
        windowSensor.setActive(false);

        // Activating first sensor goes to PENDING_ALARM state
        securityService.changeSensorActivationStatus(doorSensor, true);
        when(securityRepository.getAlarmStatus()).thenReturn(AlarmStatus.PENDING_ALARM);

        // Activating second sensor goes to ALARM state
        securityService.changeSensorActivationStatus(windowSensor, true);
        when(securityRepository.getAlarmStatus()).thenReturn(AlarmStatus.ALARM);

        // This change should not change alarm status from ALARM state
        securityService.changeSensorActivationStatus(windowSensor, false);

//        verify(securityRepository, times(2)).setAlarmStatus(AlarmStatus.ALARM);
        verify(securityRepository, times(1)).setAlarmStatus(AlarmStatus.ALARM);
        */

        // Skipping prior steps in above code and forcing to Alarm status
        when(securityRepository.getAlarmStatus()).thenReturn(AlarmStatus.ALARM);

        Sensor doorSensor = new Sensor("1", SensorType.DOOR);
        Sensor windowSensor = new Sensor("2", SensorType.WINDOW);
        securityService.changeSensorActivationStatus(doorSensor, true);
        securityService.changeSensorActivationStatus(windowSensor, true);

        verify(securityRepository, never()).setAlarmStatus(AlarmStatus.ALARM);
    }

    // Test 5
    // If a sensor is activated while already active and the system is in pending state, change it to alarm state.
    @Test
    void sensorActivated_WhileAlreadyActiveAndPendingState_ReturnAlarmState() {
        when(securityRepository.getAlarmStatus()).thenReturn(AlarmStatus.PENDING_ALARM);

        // Create sensors
        Sensor doorSensor = new Sensor("1", SensorType.DOOR);
        doorSensor.setActive(true);
        securityService.changeSensorActivationStatus(doorSensor, true);

        verify(securityRepository).setAlarmStatus(AlarmStatus.ALARM);
    }

    // Test 6
    // If a sensor is deactivated while already inactive, make no changes to the alarm state.
    @Test
    void sensorDeactivated_WhileAlreadyInactive_ReturnNoChangeToAlarmState() {
        // Could be any alarm status for test
//        when(securityRepository.getAlarmStatus()).thenReturn(AlarmStatus.PENDING_ALARM);

        // Create sensor
        Sensor doorSensor = new Sensor("1", SensorType.DOOR);
        doorSensor.setActive(false);
        securityService.changeSensorActivationStatus(doorSensor, false);

        verify(securityRepository, never()).setAlarmStatus(AlarmStatus.PENDING_ALARM);
    }

    // Test 7
    // If the image service identifies an image containing a cat while the system is armed-home, put the system into alarm status.
    @Test
    void imageContainsCat_WhileSystemIsArmed_PutAlarmStatus() {
        when(securityRepository.getArmingStatus()).thenReturn(ArmingStatus.ARMED_HOME);

        when(imageService.imageContainsCat(image, 50.0f)).thenReturn(true);
        securityService.processImage(image);

        verify(securityRepository).setAlarmStatus(AlarmStatus.ALARM);
    }

    // Test 8
    // If the image service identifies an image that does not contain a cat, change the status to no alarm as long as the sensors are not active.
    @Test
    void imageNoCat_WhenSensorsAreNotActive_ChangeStatusToNoAlarm() {
        // Make sure all sensors are inactive - inactive by default
        Sensor doorSensor = new Sensor("1", SensorType.DOOR);
        Sensor windowSensor = new Sensor("2", SensorType.WINDOW);
        Sensor motionSensor = new Sensor("3", SensorType.MOTION);
        securityService.addSensor(doorSensor);
        securityService.addSensor(windowSensor);
        securityService.addSensor(motionSensor);

        when(imageService.imageContainsCat(image, 50.0f)).thenReturn(false);
        securityService.processImage(image);

        verify(securityRepository).setAlarmStatus(AlarmStatus.NO_ALARM);
    }

    // Test 9
    // If the system is disarmed, set the status to no alarm.
    @Test
    void whenSystem_IsDisarmed_ReturnNoAlarm() {
        securityService.setArmingStatus(ArmingStatus.DISARMED);
        verify(securityRepository).setAlarmStatus(AlarmStatus.NO_ALARM);
    }

    // Test 10
    // If the system is armed, reset all sensors to inactive.
    @ParameterizedTest
    @EnumSource(
            value = ArmingStatus.class,
            names = {"ARMED_HOME", "ARMED_AWAY"} // Hardcoding required values to exclude DISARMED value
    )
    void whenSystem_IsArmed_ResetAllSensorsToInactive() {
        // Set any alarm status due to null error
        when(securityRepository.getAlarmStatus()).thenReturn(AlarmStatus.NO_ALARM);

        // Create sensors
        Sensor doorSensor = new Sensor("1", SensorType.DOOR);
        doorSensor.setActive(true);
        Sensor windowSensor = new Sensor("2", SensorType.WINDOW);
        windowSensor.setActive(true);
        Sensor motionSensor = new Sensor("3", SensorType.MOTION);
        motionSensor.setActive(true);
//        securityService.addSensor(doorSensor);
//        securityService.addSensor(windowSensor);
//        securityService.addSensor(motionSensor);

        // Create Set for sensors for mocking
        Set<Sensor> sensors = new HashSet<>();
        sensors.add(doorSensor);
        sensors.add(windowSensor);
        sensors.add(motionSensor);

        when(securityRepository.getSensors()).thenReturn(sensors);

        securityService.setArmingStatus(ArmingStatus.ARMED_HOME);

        Assertions.assertFalse(doorSensor.getActive());
        Assertions.assertFalse(windowSensor.getActive());
        Assertions.assertFalse(motionSensor.getActive());
    }

    // Test 11
    // If the system is armed-home while the camera shows a cat, set the alarm status to alarm.
    @Test
    void whenSystem_IsArmedHomeWithCatsOnCamera_ReturnAlarmStatus() {
        when(securityRepository.getArmingStatus()).thenReturn(ArmingStatus.ARMED_HOME);

        when(imageService.imageContainsCat(image, 50.0f)).thenReturn(true);
        securityService.processImage(image);

        verify(securityRepository).setAlarmStatus(AlarmStatus.ALARM);
    }
}
