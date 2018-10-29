package com.fdv.bikesrental.service.impl;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fdv.bikesrental.Promotion;
import com.fdv.bikesrental.model.RentalItem;
import com.fdv.bikesrental.model.RentalPayload;
import com.fdv.bikesrental.service.RentalService;

/**
 * Implementation of RentalService with Intive FDV behaviour
 * 
 * @author mpielvitori
 *
 */
public class FDVBikesRentalService implements RentalService {
	
	private final static Logger logger = LoggerFactory.getLogger(FDVBikesRentalService.class);
	
	/**
	 * Charges amounts by rent type constants
	 */
	private final double BY_HOUR_CHARGE = 5;
	private final double BY_DAY_CHARGE = 20;
	private final double BY_WEEK_CHARGE = 60;
	
	/**
	 * Family promotion constants
	 */
	private final int PROMOTION_FAMILY_MIN = 3;
	private final int PROMOTION_FAMILY_MAX = 5;
	private final double PROMOTION_FAMILY_DISCOUNT = 30;

	/**
	 * Rental calculation per unit
	 * 
	 * @param charge(amount per unit)
	 * @param quantity(rental units)
	 * @return total result
	 */
	private double rent(double charge, int quantity) {		
		return quantity * charge;
	}
	
	@Override
	public Response rent(RentalPayload rentalModel) {
		logger.debug("Rental process init");
		if (rentalModel.getItems() == null || rentalModel.getItems().isEmpty()) {
			logger.error("No items to use");
			return Response.status(Status.NO_CONTENT).build();
		}
		double appliedDiscount = 0;
		double total = 0;
		if (Promotion.FAMILY.equals(rentalModel.getPromotion())) {
			if (rentalModel.getItems().size() < PROMOTION_FAMILY_MIN || rentalModel.getItems().size() > PROMOTION_FAMILY_MAX) {
				logger.error("Requirements for promotion type are not satisfied -> "+ rentalModel.getItems().size());
				return Response.status(Status.BAD_REQUEST.getStatusCode(), "Requirements for promotion type are not satisfied").build();
			} else {
				appliedDiscount = PROMOTION_FAMILY_DISCOUNT;
			}
		}	
		for(RentalItem item : rentalModel.getItems()) {
			double itemAmount;
			switch (item.getType()) {
			case BY_HOUR:
				itemAmount = rent(BY_HOUR_CHARGE, item.getQuantity());
				break;
			case BY_DAY:
				itemAmount = rent(BY_DAY_CHARGE, item.getQuantity());
				break;
			case BY_WEEK:
				itemAmount = rent(BY_WEEK_CHARGE, item.getQuantity());
				break;
			default:
				logger.error("Unknown rent type");
				return Response.status(Status.BAD_REQUEST.getStatusCode(), "Unknown rent type").build();
			}
			item.setAmount(itemAmount);
			total += itemAmount;			
		}
		if (appliedDiscount > 0) {
			rentalModel.setTotal(total - (total * appliedDiscount / 100));
		} else {
			rentalModel.setTotal(total);
		}		
		logger.debug("Rental process finish");
		return Response.ok(rentalModel).build();
	}

}
