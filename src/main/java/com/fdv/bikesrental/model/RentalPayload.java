package com.fdv.bikesrental.model;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.fdv.bikesrental.Promotion;

/**
 * Rental payload request/response
 * 
 * @author mpielvitori
 *
 */
@XmlRootElement
public class RentalPayload  implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	Promotion promotion;
	
	List<RentalItem> items;
	
	private double total;

	public Promotion getPromotion() {
		return promotion;
	}

	public void setPromotion(Promotion promotion) {
		this.promotion = promotion;
	}

	public List<RentalItem> getItems() {
		return items;
	}

	public void setItems(List<RentalItem> items) {
		this.items = items;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}
	
}
