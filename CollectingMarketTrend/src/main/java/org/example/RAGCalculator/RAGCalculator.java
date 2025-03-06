package org.example.RAGCalculator;

public class RAGCalculator {
	private String item;
	private int price;
	
	public void RAGCalculator () {
	
	}
	public void RAGCalculator (String item, int price) {
		this.item = item;
		this.price = price;
	}
	
	String getItem() {
		return this.item;
	}
	void setItem (String item) {
		this.item = item;
	}
	
	int getPrice () {
		return this.price;
	}
	
}
