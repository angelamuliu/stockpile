package edu.cmu.amliu.stockpile.db;


public class Food {
	
	private int _id;
	private int stockrecord_id;
	private String location;
	private String name;
	
	// Setters
	public void set_ID(int _id) {
		this._id = _id;
	}
	public void set_stockrecord_id(int stockrecord_id) {
		this.stockrecord_id = stockrecord_id;
	}
	public void set_location(String location) {
		this.location = location;
	}
	public void set_name(String name) {
		this.name = name;
	}
	
	// Getters
	public int get_id() {
		return this._id;
	}
	public int get_stockrecord_id() {
		return this.stockrecord_id;
	}
	public String get_location() {
		return this.location;
	}
	public String get_name() {
		return this.name;
	}
	
	// Other methods
	public String format_Str() {
		String finalstr = this._id + " / " + this.stockrecord_id + " / " +this.name + " / "+this.location;
		return finalstr;
	}

}

