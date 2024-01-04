package com.sunbeam.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Optional;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.sunbeam.daos.MovieDao;
import com.sunbeam.daos.MovieDaoImple;
import com.sunbeam.daos.ReviewDao;
import com.sunbeam.daos.ReviewDaoImple;
import com.sunbeam.pojos.Movies;
import com.sunbeam.pojos.Review;
import com.sunbeam.pojos.User;

@WebServlet("/revedit")
public class ReviewEditServlet extends HttpServlet {

	private MovieDao movieDao;

	@Override
	public void init() throws ServletException {
		try {
			movieDao = new MovieDaoImple(); // Initialize MovieDao instance
		} catch (Exception e) {
			throw new ServletException("Error initializing MovieDao", e);
		}
	}

	private static boolean isReviewBelongsToUser(int reviewId, int userId) throws ServletException {
		try (ReviewDao dao = new ReviewDaoImple()) {
			Optional<Review> review = dao.findReviewById(reviewId);
			return review.isPresent() && review.get().getUserId() == userId;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServletException(e);
		}
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String reviewId = req.getParameter("id");

		int id = Integer.parseInt(reviewId);

		HttpSession session = req.getSession();
		User currentUser = (User) session.getAttribute("curUser");

		if (!isReviewBelongsToUser(id, currentUser.getId())) {
			String message = "You can only edit your own reviews!";
//			resp.sendRedirect("reviews?message=" + message);
			req.setAttribute( "message" , message);
			
			RequestDispatcher rd = req.getRequestDispatcher("reviews");
			rd.forward(req, resp);
			
			return;
		}

		Optional<Review> revOpt = null;

		try (ReviewDao dao = new ReviewDaoImple()) {
			revOpt = dao.findReviewById(id);

		} catch (Exception e) {
			e.printStackTrace();
			throw new ServletException(e);
		}

		if (!revOpt.isPresent()) {
			String message = "Review not found!";
			resp.sendRedirect("reviews?message=" + message);
			return;
		}

		Review r = revOpt.get();

		List<Movies> movieList;
		try {
			movieList = movieDao.findAll();
		} catch (Exception e) {
			throw new ServletException("Error fetching movies", e);
		}

		resp.setContentType("text/html");
		PrintWriter out = resp.getWriter();
		out.println("<html>");
		out.println("<head>");
		out.println("<title>Edit Review</title>");
		out.println("<style>");
		out.println("body { font-family: 'Arial', sans-serif; background-color: #f5f5f5; }");
		out.println(
				"form { width: 50%; margin: 0 auto; padding: 20px; background-color: #fff; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); }");
		out.println("input, textarea, select { width: 100%; padding: 10px; margin-bottom: 10px; }");
		out.println("input[type='submit'] { background-color: #4caf50; color: white; cursor: pointer; }");
		out.println("</style>");
		out.println("</head>");
		out.println("<body>");
		out.println("<form method='post' action='revedit'>");

		// Hidden input for review ID
		out.printf("<input type='hidden' name='id' value='%d' readonly><br/><br/>\n", r.getId());

		// Movie dropdown
		out.println("Movie: <select name='movie_name'>");
		for (Movies movie : movieList) {
			// Select the current movie in the dropdown
			String selected = (movie.getId() == r.getMovieId()) ? "selected" : "";
			out.printf("<option value='%s' %s>%s</option>\n", movie.getId(), selected, movie.getTitle());
		}
		out.println("</select><br/><br/>");

		// Rating, User ID (readonly), and Review fields
		out.printf("Rating: <input type='text' name='rating' value='%s'><br/><br/>\n", r.getRating());
		out.printf("User Id: <input type='text' name='user_id' value='%d' readonly><br/><br/>\n", r.getUserId());
		out.printf("Review: <textarea  rows='4' cols='50' name='review'>%s</textarea><br/><br/>\n", r.getReview());

		// Submit button
		out.println("<input type='submit' value='Update Review'/>");
		out.println("</form>");
		out.println("</body>");
		out.println("</html>");
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		// get review values from req param and update in db.
		String revId = req.getParameter("id"); // from hidden form field
		String movie_name = req.getParameter("movie_name"); // from input text
		String rating = req.getParameter("rating"); // from input text
		String user_id = req.getParameter("user_id"); // from input text (read-only)
		String review = req.getParameter("review"); // from input text area

		Review r = new Review(Integer.parseInt(revId), movie_name, review, Integer.parseInt(rating),
				Integer.parseInt(user_id), null);
		
		
		
		int cnt = 0;
		try (ReviewDao dao = new ReviewDaoImple()) {
			cnt = dao.update(r);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServletException(e);
		}

		// forward req to result servlet
		RequestDispatcher rd = req.getRequestDispatcher("reviews");
		rd.forward(req, resp);

	}

}
