package zjn.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import zjn.model.RecommendMovieList;
import zjn.recommender.UserBasedRecommender;
import zjn.util.FactoryBean;

public class MovieRecommenderServlet extends HttpServlet {

	private Recommender recommender;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		this.doPost(request, response);
	}


	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String idString = request.getParameter("id");
		String rType = request.getParameter("rType");
		long userID = 0;
		int recommenderType = 0;
		if(idString != null){
			userID = Long.parseLong(idString);
		}
		
		if(rType != null){
			recommenderType = Integer.parseInt(rType);
		}
		
		try {
			recommender = FactoryBean.createRecommender(recommenderType);
			List<RecommendedItem> items = recommender.recommend(userID, 20);
	        RecommendMovieList movieList = new RecommendMovieList(items);
	        writeJSON(response,movieList);
			
		} catch (TasteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private static void writeJSON(HttpServletResponse response,
			RecommendMovieList movieList) throws IOException {
		response.setContentType("text/plain");
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter writer = response.getWriter();
		writer.print(movieList.toJSON());
	}

}
