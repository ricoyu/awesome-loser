package com.loserico.orm.predicate;

public abstract class AbstractPredicate implements Predicate{
	
	private String propertyName;
	
	public AbstractPredicate() {
		
	}
	
	public AbstractPredicate(String propertyName) {
		this.propertyName = propertyName;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}
}
