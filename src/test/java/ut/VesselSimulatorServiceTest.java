package ut;

import java.util.List;

import org.gavaghan.geodesy.GlobalCoordinates;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import ibm.gse.eda.kc.vessel.domain.PositionCalculator;
import ibm.gse.eda.kc.vessel.domain.SimulationControl;
import ibm.gse.eda.kc.vessel.domain.VesselPosition;
import ibm.gse.eda.kc.vessel.domain.VesselSimulatorService;
import ibm.gse.eda.kc.vessel.infra.RouteRepository;
import ibm.gse.eda.kc.vessel.infra.VesselRepository;

public class VesselSimulatorServiceTest {

    private static VesselSimulatorService service;
    private static 	VesselRepository vesselRepository = new VesselRepository();
    private static 	 RouteRepository routeRepository = new RouteRepository();
    @BeforeClass
    public static void setup() {
        service = new VesselSimulatorService(vesselRepository,routeRepository);
    }

    @Test
    public void testOneStepSimulation(){
        VesselPosition position = service.getStartingPosition("Jiminy Cricket",1);
        Assert.assertNotNull(position);
        Assert.assertNotNull(position.getTimestamp());
        Assert.assertTrue("Jiminy Cricket".equals(position.getVesselID()));
    }

    @Test
    public void shouldBeClose(){
        GlobalCoordinates p1 = new GlobalCoordinates(37.815142,-122.477726);
        GlobalCoordinates p2 = new GlobalCoordinates(37.849000, -122.980806);
        double d = PositionCalculator.getDistance(p1, p2);
        double b = PositionCalculator.getBearing(p1, p2);
        System.out.println(b);
        Assert.assertTrue( b >= 270 && b <=280);
        Assert.assertFalse(service.assessIfArrived(p1,p2));
        p1 = new GlobalCoordinates(37.697624, -123.005941);
        Assert.assertTrue(service.assessIfArrived(p1,p2));
    }

    @Test
    public void shouldReturnAlistOfPositions(){
        SimulationControl simulationControl = new SimulationControl();
        simulationControl.setVesselID("Jiminy Cricket");
        simulationControl.setSimulDuration(60);
        simulationControl.setRouteID(1);
        simulationControl.setHourStep(1);
        List<VesselPosition> positions = service.startSimulation(simulationControl);
        Assert.assertNotNull(positions);
        Assert.assertTrue(positions.size() > 1);
        for (VesselPosition v : positions) {
            System.out.println(v.toString());
        }
    }
}