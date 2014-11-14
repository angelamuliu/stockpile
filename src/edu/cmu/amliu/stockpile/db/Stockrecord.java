package edu.cmu.amliu.stockpile.db;

public class Stockrecord {
	
	private int _id;
	private String date_made;
	
	// Setters
	public void set_ID(int _id) {
		this._id = _id;
	}
	public void set_date_made(String date) {
		this.date_made = date;
	}
	
	// Getters
	public int get_id() {
		return this._id;
	}
	public String get_date_made() {
		return this.date_made;
	}
	
	// Other
	public String format_Str() {
		String finalstr = this._id + " / " + this.date_made;
		return finalstr;
	}

}
