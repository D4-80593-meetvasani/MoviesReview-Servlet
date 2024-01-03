package com.sunbeam.servlets;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.sunbeam.daos.ReviewDao;
import com.sunbeam.daos.ReviewDaoImple;
import com.sunbeam.pojos.User;
import com.sunbeam.pojos.Review;


@WebServlet("/revdel")
public class ReviewDeleteServlet extends HttpServlet{
	
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		processRequest(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		processRequest(req, resp);
	}
	
	
	 private static boolean isReviewBelongsToUser(int reviewId, int userId) throws ServletException{
	        try (ReviewDao dao = new ReviewDaoImple()) {
	            Optional<Review> review = dao.findReviewById(reviewId);
	            return review.isPresent() && review.get().getUserId() == userId;
	        } catch (Exception e) {
	            e.printStackTrace();
				throw new ServletException(e);
	        }
	    }
	
	protected void processRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String reviewId = req.getParameter("id");
		
		int id = Integer.parseInt(reviewId);
		
		HttpSession session = req.getSession();
		
        User currentUser = (User) session.getAttribute("curUser");

        
        if (!isReviewBelongsToUser(id, currentUser.getId())) {
            // Send an error message or redirect to an error page
        	System.out.println("You can only edit your own reviews!");
//        	req.setAttribute("message", "You can only delete your own reviews! ");
        	String message = "You can only delete your own reviews!";
        	resp.sendRedirect("reviews?message=" + message);

            return;
        }
        
		
        int cnt = 0;
		
		try(ReviewDao dao = new ReviewDaoImple()){
			
			cnt = dao.delete(id);
			
			 resp.getWriter().println("Review deleted: " + cnt);
		}catch(Exception e) {
			e.printStackTrace();
			throw new ServletException(e);
		}
		
		//resp.sendRedirect("result");
		RequestDispatcher rd = req.getRequestDispatcher("reviews");
		rd.forward(req, resp);

		
	}
	
	
	
}
