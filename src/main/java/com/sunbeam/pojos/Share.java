package com.sunbeam.pojos;

public class Share {
	private int review_id;
	private int user_id;

	public Share() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Share(int review_id, int user_id) {
		super();
		this.review_id = review_id;
		this.user_id = user_id;
	}

	public int getReview_id() {
		return review_id;
	}

	public void setReview_id(int review_id) {
		this.review_id = review_id;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	@Override
	public String toString() {
		return "Share [review_id=" + review_id + ", user_id=" + user_id + "]";
	}

}
