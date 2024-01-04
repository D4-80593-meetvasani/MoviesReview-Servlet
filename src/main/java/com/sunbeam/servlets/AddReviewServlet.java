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
import com.sunbeam.pojos.Review;
import com.sunbeam.pojos.User;
import com.sunbeam.pojos.Movies;

@WebServlet("/addreview")
public class AddReviewServlet extends HttpServlet {

	private MovieDao movieDao;

	@Override
	public void init() throws ServletException {
		try {
			movieDao = new MovieDaoImple();
		} catch (Exception e) {
			throw new ServletException("Error initializing MovieDao", e);
		}
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// Display the form for adding a review
		HttpSession session = req.getSession();
		User currentUser = (User) session.getAttribute("curUser");

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
		out.println("<title>Add Review</title>");
		out.println("<style>");
		out.println("body { font-family: 'Arial', sans-serif; background-color: #f5f5f5; }");
		out.println(
				"form { width: 50%; margin: 0 auto; padding: 20px; background-color: #fff; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); }");
		out.println("input, textarea, select { width: 100%; padding: 10px; margin-bottom: 10px; }");
		out.println("input[type='submit'] { background-color: #4caf50; color: white; cursor: pointer; }");
		out.println("</style>");
		out.println("</head>");
		out.println("<body>");
		out.println("<form method='post' action='addreview'>");

		// Movie dropdown
		out.println("Movie: <select name='movie_name'>");
		for (Movies movie : movieList) {
			out.printf("<option value='%s'>%s</option>\n", movie.getId(), movie.getTitle());
		}
		out.println("</select><br/><br/>");

		// Rating and Review fields
		out.println("Rating: <input type='text' name='rating'><br/><br/>");
		out.println("Review: <textarea rows='4' cols='50' name='review'></textarea><br/><br/>");

		// Submit button
		out.println("<input type='submit' value='Add Review'/>");
		out.println("</form>");
		out.println("</body>");
		out.println("</html>");
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession();
		User currentUser = (User) session.getAttribute("curUser");

		String movieId = req.getParameter("movie_name");
		int rating = Integer.parseInt(req.getParameter("rating"));
		String reviewText = req.getParameter("review");

		Review newReview = new Review();
		newReview.setMovieId(Integer.parseInt(movieId));
		newReview.setRating(rating);
		newReview.setReview(reviewText);
		newReview.setUserId(currentUser.getId());

		try (ReviewDao dao = new ReviewDaoImple()) {
			int rowsAffected = dao.save(newReview);

			if (rowsAffected > 0) {
//                resp.sendRedirect("reviews?message=Review added successfully");
				req.setAttribute("message", "Review added successfully");

			} else {
//                resp.sendRedirect("addreview?message=Failed to add the review");
				req.setAttribute("message", "Failed to add review");
			}

			RequestDispatcher rd = req.getRequestDispatcher("reviews");
			rd.forward(req, resp);

		} catch (Exception e) {
			e.printStackTrace();
			throw new ServletException(e);
		}
	}
}
