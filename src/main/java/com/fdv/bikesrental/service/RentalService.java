package com.fdv.bikesrental.service;

import javax.ws.rs.core.Response;

import com.fdv.bikesrental.model.RentalPayload;

/**
 * Interface with rental contract
 * 
 * @author mpielvitori
 *
 */
public interface RentalService {

	/**
	 * Apply rent calculations in the items received
	 * 
	 * @param rentalModel
	 * @return RESTful Response(RFC 2616) with RentalModel entity
	 */
	Response rent(RentalPayload rentalModel);
	
}
