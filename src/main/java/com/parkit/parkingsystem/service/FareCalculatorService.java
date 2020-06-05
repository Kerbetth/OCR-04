package com.parkit.parkingsystem.service;

import java.sql.SQLException;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

	/**
	 * This class calculate the fare price when the vehicule exit the parking
	 * The 2 functions demanded have been implemented in the following method
	 * Their was a bug in the method due to the using of hours in the current time as the computing value
	 * this was a problem, because if a car stay more than one day, the function cannot cumulate the hours
	 * in addition, this method was not just enough to knowing if the vehicule is available for free fare
	 * so, instead, the function now get the time in an absolute value in millisecond
	 * which allow a far more safe and just calculation of the time
	 *
	 */

	public void calculateFare(Ticket ticket, Boolean availableReduction) throws ClassNotFoundException, SQLException {
		if ((ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime()))) {
			throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());
		}
		System.out.println("Start calculation process.");
		long intime = ticket.getInTime().getTime();
		long outtime = ticket.getOutTime().getTime();
		// I apply an addition of absolute time in millisecands then divide with a float


		float duration = (float) ((outtime - intime) / 3600000.00);
		if (duration > 0.5) {
			System.out.println("\n//*************************************************//");
			System.out.println("Your vehicle has been parked for more than 30 minutes (" + Math.round(duration * 60) + " minutes), calculate price...");
			System.out.println("//*************************************************// \n");
			switch (ticket.getParkingSpot().getParkingType()) {
			case CAR: {
				ticket.setPrice(duration * Fare.CAR_RATE_PER_HOUR);
				break;
			}
			case BIKE: {
				ticket.setPrice(duration * Fare.BIKE_RATE_PER_HOUR);
				break;
			}
			default:
				throw new IllegalArgumentException("Unkown Parking Type");
			}
			if (availableReduction == true) {
				System.out.println("\n//*************************************************//");
				System.out.println("Your are a recurent user, for your confidence in our services we offer you a 5% reduction on the whole price! ("+ ticket.getPrice() + "$)");
				System.out.println("//*************************************************// \n");
				ticket.setPrice(ticket.getPrice() * 0.95);
			} else {
				System.out.println("\n//*************************************************//");
				System.out.println("Parking fare:" + ticket.getPrice()+ "$, next time we offer you 5% reduction, as a recurent user!");
				System.out.println("//*************************************************// \n");
			}
		} else {
			System.out.println("\n//*************************************************//");
			System.out.println("Your vehicle has been parked for less than 30 minutes(" + Math.round(duration * 60) + " minutes), nothing to pay.");
			System.out.println("//*************************************************// \n");
			ticket.setPrice(0.00);
		}
	}


}