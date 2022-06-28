package it.polito.tdp.yelp.model;

public class ReviewArchi {
	
	private Review review;
	private int archiOut;
	
	public ReviewArchi(Review review, int archiOut) {
		super();
		this.review = review;
		this.archiOut = archiOut;
	}

	public Review getReview() {
		return review;
	}

	public void setReview(Review review) {
		this.review = review;
	}

	public int getArchiOut() {
		return archiOut;
	}

	public void setArchiOut(int archiOut) {
		this.archiOut = archiOut;
	}

}
