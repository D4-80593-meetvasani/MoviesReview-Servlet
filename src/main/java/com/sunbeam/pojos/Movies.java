package com.sunbeam.pojos;

public class Movies {
	private int id;
	private String title;
	private String releaseDate;
	public Movies() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Movies(int id, String title, String releaseDate) {
		super();
		this.id = id;
		this.title = title;
		this.releaseDate = releaseDate;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getReleaseDate() {
		return releaseDate;
	}
	public void setReleaseDate(String releaseDate) {
		this.releaseDate = releaseDate;
	}
	@Override
	public String toString() {
		return "Movies [id=" + id + ", title=" + title + ", releaseDate=" + releaseDate + "]";
	}
	
	
	
}
