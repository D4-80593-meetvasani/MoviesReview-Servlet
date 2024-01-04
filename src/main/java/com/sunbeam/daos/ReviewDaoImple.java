package com.sunbeam.daos;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.sunbeam.pojos.Review;

public class ReviewDaoImple extends Dao implements ReviewDao {

	private PreparedStatement stmtFindAll;
	private PreparedStatement stmtSave;
	private PreparedStatement stmtUpdate;
	private PreparedStatement stmtFindReviewById;
	private PreparedStatement stmtFindReviewsById;
	private PreparedStatement stmtShareReview;
	private PreparedStatement stmtDisplayShareReview;
	private PreparedStatement stmtDelete;

	public ReviewDaoImple() throws Exception {

//		String sqlFindAll = "SELECT * from Reviews";
		String sqlFindAll = "SELECT Reviews.*, Movies.title AS movie_name FROM Reviews JOIN Movies ON Reviews.movie_id = Movies.id";
		stmtFindAll = con.prepareStatement(sqlFindAll);

//		String sqlSave = "INSERT INTO Reviews VALUES (default,?,?,?,?,?)";
        String sqlSave = "INSERT INTO reviews (id, review, rating, user_id, movie_id , modified) VALUES (default,?,?,?,?,?)";
		stmtSave = con.prepareStatement(sqlSave);

		String sqlUpdate = "UPDATE Reviews SET rating = ?, review = ?, modified=NOW() where id=?";
		stmtUpdate = con.prepareStatement(sqlUpdate);

//		String sqlFindReviewById = "SELECT * from Reviews WHERE id = ?";
		String sqlFindReviewById = "SELECT Reviews.*, Movies.title AS movie_name FROM Reviews JOIN Movies ON Reviews.movie_id = Movies.id WHERE Reviews.id = ?";
		stmtFindReviewById = con.prepareStatement(sqlFindReviewById);

//		String sqlFindReviewsById = "SELECT * from Reviews WHERE user_id = ?";
		String sqlFindReviewsById = "SELECT Reviews.*, Movies.title AS movie_name FROM Reviews JOIN Movies ON Reviews.movie_id = Movies.id where user_id=?";
		stmtFindReviewsById = con.prepareStatement(sqlFindReviewsById);

		String sqlShareReview = "INSERT INTO Shares (review_id, user_id) VALUES (?, ?)";
		stmtShareReview = con.prepareStatement(sqlShareReview);

//		String sqlDisplayShareReview = "SELECT Reviews.* FROM Reviews JOIN Shares ON Reviews.id = Shares.review_id WHERE Shares.user_id = ?";
		String sqlDisplayShareReview = "SELECT Reviews.*, Movies.title AS movie_name\r\n" + "FROM Reviews\r\n"
				+ "JOIN Shares ON Reviews.id = Shares.review_id\r\n" + "JOIN Movies ON Reviews.movie_id = Movies.id\r\n"
				+ "WHERE Shares.user_id = ?";
		stmtDisplayShareReview = con.prepareStatement(sqlDisplayShareReview);

		String sqlDelete = "DELETE from Reviews where id=?";
		stmtDelete = con.prepareStatement(sqlDelete);

	}

	public int shareReview(int reviewId, List<Integer> sharedUserIds) throws Exception {
		int count = 0;

		for (int userId : sharedUserIds) {
			stmtShareReview.setInt(1, reviewId);
			stmtShareReview.setInt(2, userId);
			count += stmtShareReview.executeUpdate();
		}

		return count;

	}

	public int shareReview(int reviewId, int userId) throws Exception {
		int count = 0;

		stmtShareReview.setInt(1, reviewId);
		stmtShareReview.setInt(2, userId);
		count += stmtShareReview.executeUpdate();

		return count;

	}

//	public List<Review> findAll() throws Exception {
//		List<Review> list = new ArrayList<Review>();
//		try (ResultSet rs = stmtFindAll.executeQuery()) {
//			while (rs.next()) {
//				int id = rs.getInt("id");
//				int mid = rs.getInt("movie_id");
//				String text = rs.getString("review");
//				int rating = rs.getInt("rating");
//				int userId = rs.getInt("user_id");
//				Timestamp tStmp = rs.getTimestamp("modified");
//
//				Review r = new Review(id, mid, text, rating, userId, tStmp);
//				list.add(r);
//			}
//		} // rs.close()
//		return list;
//	}

	public List<Review> findAll() throws Exception {
		List<Review> list = new ArrayList<Review>();
		try (ResultSet rs = stmtFindAll.executeQuery()) {
			while (rs.next()) {
				int id = rs.getInt("id");
				String movieName = rs.getString("movie_name");
				String text = rs.getString("review");
				int rating = rs.getInt("rating");
				int userId = rs.getInt("user_id");
				Timestamp tStmp = rs.getTimestamp("modified");

				Review r = new Review(id, movieName, text, rating, userId, tStmp);
				list.add(r);
			}
		} // rs.close()
		return list;
	}

//	public List<Review> findSharedReviews(int uid) throws Exception {
//		List<Review> list = new ArrayList<Review>();
//		stmtDisplayShareReview.setInt(1, uid);
//		try (ResultSet rs = stmtDisplayShareReview.executeQuery()) {
//			while (rs.next()) {
//				int id = rs.getInt("id");
//				int mid = rs.getInt("movie_id");
//				String text = rs.getString("review");
//				int rating = rs.getInt("rating");
//				int userId = rs.getInt("user_id");
//				Timestamp tStmp = rs.getTimestamp("modified");
//
//				Review r = new Review(id, mid, text, rating, userId, tStmp);
//				list.add(r);
//			}
//		} // rs.close()
//		return list;
//	}

	public List<Review> findSharedReviews(int uid) throws Exception {
		List<Review> list = new ArrayList<Review>();
		stmtDisplayShareReview.setInt(1, uid);
		try (ResultSet rs = stmtDisplayShareReview.executeQuery()) {
			while (rs.next()) {
				int id = rs.getInt("id");
				String movie = rs.getString("movie_name");
				String text = rs.getString("review");
				int rating = rs.getInt("rating");
				int userId = rs.getInt("user_id");
				Timestamp tStmp = rs.getTimestamp("modified");

				Review r = new Review(id, movie, text, rating, userId, tStmp);
				list.add(r);
			}
		} // rs.close()
		return list;
	}

	public int save(Review q) throws Exception {
//		stmtSave.setInt(1, q.getId());
		stmtSave.setString(1, q.getReview());
		stmtSave.setInt(2, q.getRating());
		stmtSave.setInt(3, q.getUserId());
		stmtSave.setInt(4, q.getMovieId());
		stmtSave.setTimestamp(5, q.getModified());
		int count = stmtSave.executeUpdate();
		return count;
	}

	public int update(Review r) throws Exception {
		stmtUpdate.setInt(1, r.getRating());
		stmtUpdate.setString(2, r.getReview());
		stmtUpdate.setInt(3, r.getId());
		int count = stmtUpdate.executeUpdate();
		return count;
	}

//	public Optional<Review> findReviewById(int uid) throws Exception {
//		stmtFindReviewById.setInt(1, uid);
//		try (ResultSet rs = stmtFindReviewById.executeQuery()) {
//			while (rs.next()) {
//				int id = rs.getInt("id");
//				String review = rs.getString("review");
//				int rating = rs.getInt("rating");
//				int userId = rs.getInt("user_id");
//				int movieId = rs.getInt("movie_id");
//				Timestamp modified = rs.getTimestamp("modified");
//				
//				Review r = new Review(id, movieId, review, rating, userId, modified);
////				return r;
//				
//				return Optional.of(r);
//
//			}
//		} // rs.close()
//		return Optional.empty();
//	}

	public Optional<Review> findReviewById(int uid) throws Exception {
		stmtFindReviewById.setInt(1, uid);
		try (ResultSet rs = stmtFindReviewById.executeQuery()) {
			while (rs.next()) {
				int id = rs.getInt("id");
				String review = rs.getString("review");
				int rating = rs.getInt("rating");
				int userId = rs.getInt("user_id");
				String movie = rs.getString("movie_name");
				Timestamp modified = rs.getTimestamp("modified");

				Review r = new Review(id, movie, review, rating, userId, modified);
//				return r;

				return Optional.of(r);

			}
		} // rs.close()
		return Optional.empty();
	}
//	public Optional<Review> findReviewById(int uid) throws Exception {
//		stmtFindReviewById.setInt(1, uid);
//		try (ResultSet rs = stmtFindReviewById.executeQuery()) {
//			while (rs.next()) {
//				int id = rs.getInt("id");
//				String review = rs.getString("review");
//				int rating = rs.getInt("rating");
//				int userId = rs.getInt("user_id");
//				String movie = rs.getString("movie_name");
//				Timestamp modified = rs.getTimestamp("modified");
//				
//				Review r = new Review(id, movie, review, rating, userId, modified);
////				return r;
//				
//				return Optional.of(r);
//				
//			}
//		} // rs.close()
//		return Optional.empty();
//	}

	public List<Review> findReviewsById(int uid) throws Exception {
		List<Review> list = new ArrayList<Review>();
		stmtFindReviewsById.setInt(1, uid);
		try (ResultSet rs = stmtFindReviewsById.executeQuery()) {
			while (rs.next()) {
				int id = rs.getInt("id");
				String review = rs.getString("review");
				int userId = rs.getInt("user_id");
				String movie = rs.getString("movie_name");
				int rating = rs.getInt("rating");
				Timestamp modified = rs.getTimestamp("modified");

				Review r = new Review(id, movie, review, rating, userId, modified);
				list.add(r);

			}
		} // rs.close()
		return list;
	}

	public int delete(int id) throws Exception {
		stmtDelete.setInt(1, id);
		int count = stmtDelete.executeUpdate();
		return count;
	}

}
