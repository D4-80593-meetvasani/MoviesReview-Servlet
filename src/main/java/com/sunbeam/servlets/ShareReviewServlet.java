package com.sunbeam.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Optional;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
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
import com.sunbeam.daos.UserDao;
import com.sunbeam.daos.UserDaoImple;
import com.sunbeam.pojos.Review;
import com.sunbeam.pojos.Share;
import com.sunbeam.pojos.User;


@WebServlet("/reviewshare")
public class ShareReviewServlet extends HttpServlet{
	
	private UserDao userDao;
	private ReviewDao reviewDao;
	
	@Override
	public void init() throws ServletException {
		try {
			userDao = new UserDaoImple();
			reviewDao = new ReviewDaoImple();
		} catch (Exception e) {
			throw new ServletException("Error initializing UserDao / ReviewDao", e);
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
		
		String id = req.getParameter("id");
		int reviewId = Integer.parseInt(id);
		
        // Display the form for sharing a review
        HttpSession session = req.getSession();
        User currentUser = (User) session.getAttribute("curUser");
        
        
		if (!isReviewBelongsToUser(reviewId, currentUser.getId())) {
			String message = "You can only share your own reviews!";
//			resp.sendRedirect("reviews?message=" + message);
			req.setAttribute( "message" , message);
			
			RequestDispatcher rd = req.getRequestDispatcher("reviews");
			rd.forward(req, resp);
			
			return;
		}
        

        List<User> list = null;
		try {
			list = userDao.findAll();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ServletException("Error fetching users",e);
		}
        
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Share Review</title>");
        out.println("<style>");
        out.println("body { font-family: 'Arial', sans-serif; background-color: #f5f5f5; }");
        out.println(
                "form { width: 50%; margin: 0 auto; padding: 20px; background-color: #fff; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); }");
        out.println("input, textarea, select { width: 100%; padding: 10px; margin-bottom: 10px; }");
        out.println("input[type='submit'] { background-color: #4caf50; color: white; cursor: pointer; }");
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");
        
		ServletContext  app = req.getServletContext();
		String title = app.getInitParameter("appTitle");
		out.printf("<h1>%s</h1>\n", title);
		
        
        out.println("<form method='post' action='reviewshare'>");
		out.printf("Review Id: <input type='text' name='reviewid' value='%d' readonly/>", reviewId);
		out.println("<select name='userid'>");
		for(User u:list)
		 out.printf("<option value='%d'>%s</option>", u.getId(), u.getFirstName());
		out.println("</select>");
		out.println("<input type='submit' value='Share'/>");
		out.println("</form>");
		out.println("</body>");
		out.println("</html>");
		
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
				
		


		int reviewId = Integer.parseInt(req.getParameter("reviewid"));
		int userId = Integer.parseInt(req.getParameter("userid"));
		
		
//		Share s = new Share(reviewId, userId);
		
		
		
		try {
			int rowsAffected = reviewDao.shareReview(reviewId, userId);
			
			if (rowsAffected > 0) {
				req.setAttribute("message", "Review shared successfully");

			} else {
				req.setAttribute("message", "Failed to share review");
			}

			RequestDispatcher rd = req.getRequestDispatcher("reviews");
			rd.forward(req, resp);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
