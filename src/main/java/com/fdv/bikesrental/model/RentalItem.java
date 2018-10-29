package com.fdv.bikesrental.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import com.fdv.bikesrental.RentType;

/**
 * Rental item entity
 * 
 * @author mpielvitori
 *
 */
@XmlRootElement
public class RentalItem  implements Serializable {
	
	private static final long serialVersionUID = 1L;

	int quantity;
	
	RentType type;
	
	double amount;
	
	private RentalItem() {};

	public int getQuantity() {
		return quantity;
	}

	public RentType getType() {
		return type;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}	
	
	public static class Builder {
		int quantity;		
		RentType type;		
		
		public Builder quantity(int quantity){
			this.quantity = quantity;
            return this;
        }
		
		public Builder type(RentType type){
			this.type = type;
            return this;
        }
		
		public RentalItem build(){
			RentalItem item = new RentalItem();
			item.quantity = this.quantity;
			item.type = this.type;
			
            return item;
        }
	}
	
}
