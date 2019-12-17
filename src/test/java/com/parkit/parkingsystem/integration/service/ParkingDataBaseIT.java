package com.parkit.parkingsystem.integration.service;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.nio.charset.Charset;
import java.util.Date;
import java.util.Random;

@ExtendWith(MockitoExtension.class)
public class ParkingDataBaseIT {

    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static ParkingSpotDAO parkingSpotDAO;
    private static TicketDAO ticketDAO;
    private static DataBasePrepareService dataBasePrepareService;
	private static String regNumberString;
	private static Ticket ticket;
	
    @Mock
    private static InputReaderUtil inputReaderUtil;

    @BeforeAll
    private static void setUp(){
        parkingSpotDAO = new ParkingSpotDAO();
        parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
        ticketDAO = new TicketDAO();
        ticketDAO.dataBaseConfig = dataBaseTestConfig;
        dataBasePrepareService = new DataBasePrepareService();
    }

    @BeforeEach
    private void setUpPerTest(){
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn(regNumberString = "ABCDEF");
        dataBasePrepareService.clearDataBaseEntries();
    }

    @AfterEach
    private void tearDown(){
    	dataBasePrepareService.clearDataBaseEntries();
    }

    @Test
    public void testParkingACar(){
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        //int parkingPlace = parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR);
        parkingService.processIncomingVehicle();
        ticket = ticketDAO.getTicket(regNumberString);
        System.out.println(ticketDAO.getTicket(regNumberString));
        assertEquals(ticket.getVehicleRegNumber(), regNumberString);
        assertEquals(ticket.getParkingSpot().isItAvailable(), false);
        //TODO: check that a ticket is actualy saved in DB and Parking table is updated with availability
    }


    @Test
    public void testParkingLotExit(){
        testParkingACar();
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);

		int randomplace = new Random().nextInt(100) + 1;
		ParkingSpot parkingSpot = new ParkingSpot(randomplace, ParkingType.CAR, false);
		ticket = new Ticket();
		ticket.setInTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000)));
		ticket.setParkingSpot(parkingSpot);
		ticket.setVehicleRegNumber(regNumberString);
        parkingService.processExitingVehicle();
        Ticket ticket2 = ticketDAO.getTicket(regNumberString);

        assertNotNull(ticket2.getPrice());
        assertNotNull(ticket2.getOutTime());
        //assertTrue(ticket2.getInTime().getTime() < ticket2.getOutTime().getTime());
        //assertTrue(ticket2.getOutTime().getTime() < new Date(System.currentTimeMillis()).getTime());
        //TODO: check that the fare generated and out time are populated correctly in the database
    }

}