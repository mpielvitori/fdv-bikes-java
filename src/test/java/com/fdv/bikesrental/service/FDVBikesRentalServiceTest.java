package com.fdv.bikesrental.service;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.fdv.bikesrental.Promotion;
import com.fdv.bikesrental.RentType;
import com.fdv.bikesrental.model.RentalItem;
import com.fdv.bikesrental.model.RentalPayload;
import com.fdv.bikesrental.service.impl.FDVBikesRentalService;

@RunWith(MockitoJUnitRunner.class)
public class FDVBikesRentalServiceTest {

	@InjectMocks
	FDVBikesRentalService service;
	
	@InjectMocks
	RentalPayload rental;
	
	/**
	 * 1. Rental by hour, charging $5 per hour
	 */
	@Test
	public void testRentByHour() {
		// Given
		List<RentalItem> items = new ArrayList<RentalItem>();
		RentalItem item = new RentalItem.Builder()
				.quantity(2)
				.type(RentType.BY_HOUR)
				.build();
		items.add(item);
		// Set 1 bike for 2 hours
		rental.setItems(items);
		
		// When
		Response response = service.rent(rental);
		
		// Then
		assertEquals(response.getStatus(), Response.Status.OK.getStatusCode());		
		RentalPayload responseEntity = (RentalPayload) response.getEntity();
		assertEquals(responseEntity.getTotal(),10,0);
		assertEquals(responseEntity.getItems().get(0).getAmount(),10,0);
	}
	
	/**
	 * 2. Rental by day, charging $20 a day
	 */
	@Test
	public void testRentByDay() {
		List<RentalItem> items = new ArrayList<RentalItem>();
		RentalItem item = new RentalItem.Builder()
				.quantity(2)
				.type(RentType.BY_DAY)
				.build();
		items.add(item);
		// Set 1 bike for 2 days
		rental.setItems(items);
		Response response = service.rent(rental);
		assertEquals(response.getStatus(), Response.Status.OK.getStatusCode());
		
		RentalPayload responseEntity = (RentalPayload) response.getEntity();
		assertEquals(responseEntity.getTotal(),40,0);
		assertEquals(responseEntity.getItems().get(0).getAmount(),40,0);
	}

	/**
	 * 3. Rental by week, changing $60 a week
	 */
	@Test
	public void testRentByWeek() {
		List<RentalItem> items = new ArrayList<RentalItem>();
		RentalItem item = new RentalItem.Builder()
				.quantity(1)
				.type(RentType.BY_WEEK)
				.build();
		items.add(item);
		items.add(item);
		// Set 2 bikes for 1 week each one
		rental.setItems(items);
		Response response = service.rent(rental);
		assertEquals(response.getStatus(), Response.Status.OK.getStatusCode());
		
		RentalPayload responseEntity = (RentalPayload) response.getEntity();
		assertEquals(responseEntity.getTotal(),120,0);
		assertEquals(responseEntity.getItems().get(0).getAmount(),60,0);
	}
	
	/**
	 * 4. Family Rental, is a promotion that can include from 3 to 5 Rentals (of any type) with a discount of 30% of the total price
	 */
	@Test
	public void testFamilyRent() {
		List<RentalItem> items = new ArrayList<RentalItem>();
		RentalItem item;
		item = new RentalItem.Builder()
				.quantity(1)
				.type(RentType.BY_HOUR)
				.build();
		items.add(item);
		item = new RentalItem.Builder()
				.quantity(1)
				.type(RentType.BY_DAY)
				.build();
		items.add(item);
		item = new RentalItem.Builder()
				.quantity(1)
				.type(RentType.BY_WEEK)
				.build();
		items.add(item);
		// Set 1 bike for 1 hour, 1 bike for 1 day and 1 bike for 1 week
		rental.setItems(items);
		rental.setPromotion(Promotion.FAMILY);
		Response response = service.rent(rental);
		assertEquals(response.getStatus(), Response.Status.OK.getStatusCode());
		
		RentalPayload responseEntity = (RentalPayload) response.getEntity();

		assertEquals(responseEntity.getTotal(),59.5,0);
	}
	
	@Test
	public void testFamilyRentMinItems() {
		List<RentalItem> items = new ArrayList<RentalItem>();
		RentalItem item;
		item = new RentalItem.Builder()
				.quantity(1)
				.type(RentType.BY_HOUR)
				.build();
		items.add(item);
		// Set 1 bike for 1 hour
		rental.setItems(items);
		rental.setPromotion(Promotion.FAMILY);
		Response response = service.rent(rental);
		assertEquals(response.getStatus(), Response.Status.BAD_REQUEST.getStatusCode());
	}
	
	@Test
	public void testFamilyRentMaxItems() {
		List<RentalItem> items = new ArrayList<RentalItem>();
		RentalItem item;
		item = new RentalItem.Builder()
				.quantity(1)
				.type(RentType.BY_HOUR)
				.build();
		items.add(item);
		items.add(item);
		items.add(item);
		items.add(item);
		items.add(item);
		items.add(item);
		// Set 6 bikes for 1 hour each one
		rental.setItems(items);
		rental.setPromotion(Promotion.FAMILY);
		Response response = service.rent(rental);
		assertEquals(response.getStatus(), Response.Status.BAD_REQUEST.getStatusCode());
	}
	
	@Test
	public void testNoItems() {
		// Given
		rental.setItems(new ArrayList<RentalItem>());
		// When
		Response response = service.rent(rental);
		// Then
		assertEquals(response.getStatus(), Response.Status.NO_CONTENT.getStatusCode());
	}

	@Test
	public void testNullItems() {
		Response response = service.rent(rental);
		assertEquals(response.getStatus(), Response.Status.NO_CONTENT.getStatusCode());
	}
	
	@Test
	public void testUnknownRentType() {
		List<RentalItem> items = new ArrayList<RentalItem>();
		RentalItem item = new RentalItem.Builder()
				.quantity(1)
				.type(RentType.UNKNOWN)
				.build();
		items.add(item);
		rental.setItems(items);
		Response response = service.rent(rental);
		assertEquals(response.getStatus(), Response.Status.BAD_REQUEST.getStatusCode());
	}

}
