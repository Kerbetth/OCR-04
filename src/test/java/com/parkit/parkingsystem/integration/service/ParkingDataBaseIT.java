package com.parkit.parkingsystem.integration.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;

@ExtendWith(MockitoExtension.class)
public class ParkingDataBaseIT {

<<<<<<< HEAD
    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static ParkingSpotDAO parkingSpotDAO;
    private static TicketDAO ticketDAO;
    private static DataBasePrepareService dataBasePrepareService;
	
    @Mock
    private static InputReaderUtil inputReaderUtil;
=======
	private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
	private static ParkingSpotDAO parkingSpotDAO;
	private static TicketDAO ticketDAO;
	private static DataBasePrepareService dataBasePrepareService;
	private static String regNumberString;
	private static Ticket ticket;
>>>>>>> featureJunitTest

	@Mock
	private static InputReaderUtil inputReaderUtil;

<<<<<<< HEAD
    @BeforeEach
    private void setUpPerTest(){
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        dataBasePrepareService.clearDataBaseEntries();
    }
=======
	@BeforeAll
	private static void setUp() {
		parkingSpotDAO = new ParkingSpotDAO();
		parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
		ticketDAO = new TicketDAO();
		ticketDAO.dataBaseConfig = dataBaseTestConfig;
		dataBasePrepareService = new DataBasePrepareService();
	}
>>>>>>> featureJunitTest

	@BeforeEach
	private void setUpPerTest() {
		when(inputReaderUtil.readSelection()).thenReturn(1);
		when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn(regNumberString = "ABCDEF");
		dataBasePrepareService.clearDataBaseEntries();
	}

<<<<<<< HEAD
    @Test
    public void testParkingACar(){
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        //TODO: check that a ticket is actualy saved in DB and Parking table is updated with availability
    }
=======
	@AfterEach
	private void tearDown() {
		dataBasePrepareService.clearDataBaseEntries();
	}
>>>>>>> featureJunitTest

	@Test
	public void testParkingACar() {
		ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
		// int parkingPlace = parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR);
		parkingService.processIncomingVehicle();
		ticket = ticketDAO.getTicket(regNumberString);
		System.out.println(ticketDAO.getTicket(regNumberString));
		assertEquals(ticket.getVehicleRegNumber(), regNumberString);
		assertEquals(ticket.getParkingSpot().isItAvailable(), false);
		// TODO: check that a ticket is actualy saved in DB and Parking table is updated
		// with availability
	}

<<<<<<< HEAD
    @Test
    public void testParkingLotExit() throws InterruptedException{

        //TODO: check that the fare generated and out time are populated correctly in the database
    }
=======
	@Test
	public void testParkingLotExit() throws InterruptedException {
		ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
		when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn(regNumberString);
		parkingService.processIncomingVehicle();
		ticket = ticketDAO.getTicket(regNumberString);
		Thread.sleep(1000);
		parkingService.processExitingVehicle();
		Ticket ticket2 = ticketDAO.getTicket(regNumberString);

		assertNotNull(ticket2.getPrice());
		assertNotNull(ticket2.getOutTime());
		assertEquals(ticket2.getInTime(), ticket.getInTime());
		assertEquals(ticket2.getVehicleRegNumber(), ticket.getVehicleRegNumber());
		// TODO: check that the fare generated and out time are populated correctly in
		// the database
	}
>>>>>>> featureJunitTest

}
