package com.openxc;

import com.openxc.measurements.SteeringWheelAngle;
import com.openxc.measurements.VehicleMeasurement;
import com.openxc.measurements.VehicleSpeed;
import com.openxc.measurements.UnrecognizedMeasurementTypeException;

import com.openxc.VehicleService;

import android.content.Intent;

import android.os.RemoteException;

import android.test.ServiceTestCase;

import android.test.suitebuilder.annotation.MediumTest;
import android.test.suitebuilder.annotation.SmallTest;

public class BoundVehicleServiceTest extends ServiceTestCase<VehicleService> {
    VehicleService service;
    VehicleSpeed.Listener speedListener = new VehicleSpeed.Listener() {
        public void receive(VehicleMeasurement measurement) {
            // TODO it would be great if this was given a VehicleSpeed
            // directly, but I don't want to have to implement an interface
            // for every measurement...
            VehicleSpeed speedMeasurement = (VehicleSpeed) measurement;
            assertTrue(false);
        }
    };
    SteeringWheelAngle.Listener steeringWheelListener =
            new SteeringWheelAngle.Listener() {
        public void receive(VehicleMeasurement measurement) {
            // TODO it would be great if this was given a VehicleSpeed
            // directly, but I don't want to have to implement an interface
            // for every measurement...
            SteeringWheelAngle angelMeasurement = (SteeringWheelAngle) measurement;
            assertTrue(false);
        }
    };

    public BoundVehicleServiceTest() {
        super(VehicleService.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Intent startIntent = new Intent();
        startIntent.setClass(getContext(), VehicleService.class);
        service = ((VehicleService.VehicleServiceBinder)
                bindService(startIntent)).getService();
    }

    @SmallTest
    public void testPreconditions() {
    }

    @MediumTest
    public void testGetUnbound() throws UnrecognizedMeasurementTypeException {
        service.unbindRemote();
        VehicleMeasurement measurement = service.get(VehicleSpeed.class);
        assertNotNull(measurement);
        assertFalse(measurement.hasValue());
    }

    @MediumTest
    public void testGetBound() throws UnrecognizedMeasurementTypeException {
        VehicleMeasurement measurement = service.get(VehicleSpeed.class);
        assertNotNull(measurement);
        assertFalse(measurement.hasValue());
    }

    @MediumTest
    public void testGetMocked() {
        assertTrue(false);
    }

    @MediumTest
    public void testAddListener() {
        service.addListener(VehicleSpeed.class, speedListener);
        // TODO trigger a callback make sure the listener is called
    }

    @MediumTest
    public void testAddListenerTwice() {
        service.addListener(VehicleSpeed.class, speedListener);
        service.addListener(VehicleSpeed.class, speedListener);
        // TODO trigger a callback make sure the listener is called only once
    }

    @MediumTest
    public void testAddListenersTwoMeasurements() {
        service.addListener(VehicleSpeed.class, speedListener);
        service.addListener(SteeringWheelAngle.class, steeringWheelListener);
        // TODO trigger callbacks for each, make sure they both get it once
    }

    @MediumTest
    public void testRemoveListener() {
        service.addListener(VehicleSpeed.class, speedListener);
        service.removeListener(VehicleSpeed.class, speedListener);
        // TODO test that we don't recive an update
    }

    @MediumTest
    public void testRemoveTwoListeners() {
        service.addListener(VehicleSpeed.class, speedListener);
        service.addListener(SteeringWheelAngle.class, steeringWheelListener);
        service.removeListener(VehicleSpeed.class, speedListener);
        service.removeListener(SteeringWheelAngle.class, speedListener);
        // TODO test that we don't recive an update in either
    }

    @MediumTest
    public void testRemoveWithoutListening() {
        service.removeListener(VehicleSpeed.class, speedListener);
    }

    @MediumTest
    public void testRemoveOneMeasurementListener() {
        service.addListener(VehicleSpeed.class, speedListener);
        service.addListener(SteeringWheelAngle.class, steeringWheelListener);
        service.removeListener(VehicleSpeed.class, speedListener);
        // TODO test that only the steering wheel stil receives an update
    }
}
