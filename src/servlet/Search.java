package servlet;

import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import spider.copy.Data;

/**
 * Servlet implementation class Search
 */
public class Search extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Search() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String searchWord = request.getParameter("building");
		System.out.println("searching for" + searchWord);
		
		Set<String> keys = Data.ubcMap.keySet();
		Iterator<String> itr = keys.iterator();
		String key;
		while(itr.hasNext()) {
			key = itr.next();
			if (key.contains(searchWord.toUpperCase())) {
				response.getOutputStream().print(Data.ubcMap.get(key));
				break;
			}
		}
		
		response.getOutputStream().print("Not found");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

}
