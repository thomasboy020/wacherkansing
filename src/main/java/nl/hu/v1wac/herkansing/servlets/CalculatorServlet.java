package nl.hu.v1wac.herkansing.servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(urlPatterns = "/Rekenmachine")
public class CalculatorServlet extends HttpServlet {
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException,IOException {
		
		Double first = Double.parseDouble(req.getParameter("1"));
		Double second = Double.parseDouble(req.getParameter("2"));
		String sort = req.getParameter("sort");
		String result = first + " " + sort + " " + second + " = ";
		
		switch(sort) {
		case "+": {result +=  (first + second); break;}
		case "-": {result +=  (first - second); break;}
		case "/": {result +=  (first / second); break;}
		case "*": {result +=  (first * second); break;}
			
		}

		
		PrintWriter out = resp.getWriter();
		resp.setContentType("text/html");
		
		out.println("<!DOCTYPE html>");
		out.println("<html>");
		out.println(" <title>Calculator</title>");
		out.println(" <body>");
		out.println(" <h2>Calculator Results</h2>");
		out.println("<h2>" + result + "</h2>");
		out.println(" </body>");
		out.println("</html>");
		
	}
}

