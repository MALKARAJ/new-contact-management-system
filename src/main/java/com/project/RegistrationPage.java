package com.project;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.Email;
import com.google.appengine.api.urlfetch.HTTPHeader;
import com.google.appengine.api.urlfetch.HTTPMethod;
import com.google.appengine.api.urlfetch.HTTPRequest;
import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;

import org.json.JSONObject;
import org.mindrot.jbcrypt.BCrypt;

/**
 * Servlet implementation class RegistrationPage
 */
@WebServlet("/register")
public class RegistrationPage extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger log = Logger.getLogger("logger");

    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegistrationPage() {
        super();
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // pre-flight request processing
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Methods", "*");
        resp.setHeader("Access-Control-Allow-Headers", "*");
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO Auto-generated method stub

        request.getRequestDispatcher("register.jsp").forward(request, response);

    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO Auto-generated method stub

        response.setContentType("application/json");
        // response.setHeader("Access-Control-Allow-Origin","*");
        response.addHeader("Access-Control-Allow-Origin", "*"); // george

        StringBuffer jb = new StringBuffer();
        String line = null;
        try {
            BufferedReader reader = request.getReader();
            while ((line = reader.readLine()) != null)
                jb.append(line);
        } catch (Exception e) {
            /* report an error */ }

        String str = jb.toString();
        JSONObject jsonobject = new JSONObject(str);

        String origin = request.getHeader("Origin");

        String email = jsonobject.getString("email");
        String pwd = BCrypt.hashpw(jsonobject.get("password").toString(), BCrypt.gensalt());

        User user = new User(email, pwd);

        UUID id = UUID.randomUUID();

        if (origin != null && (origin.equals("https://malkarajtraining12.uc.r.appspot.com")
                || origin.equals("http://localhost:8080"))) {
            user.setUser_id(id.toString());
        } else {
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        String user_id=jsonobject.get("user_id").toString();
        
		user.setUser_id(user_id.toString());

		            }
      
		UserDao userdao = new UserDaoImplementation();
		boolean check = userdao.createUser(user);

		PrintWriter out = response.getWriter();
		 JSONObject obj1=new JSONObject();
		
		 
		 if (check == true) {

			
			
			
			
		        	            if(origin!=null &&origin.equals("https://malkarajtraining12.uc.r.appspot.com"))
										{
											final String uri = "https://georgefulltraining12.uc.r.appspot.com/register";
											URL url = new URL(uri);
											// HTTPRequest req = new HTTPRequest(url, HTTPMethod.POST);
											// Set HTTP request method.
											
											
								//	req.addHeader(new HTTPHeader("Authorization", BCrypt.hashpw(SyncApp.sentKey,BCrypt.gensalt(10))));
                                // req.addHeader(new HTTPHeader("Origin","https://georgefulltraining12.uc.r.appspot.com"));
                                  
                                JSONObject reqObj=new JSONObject();
											 
											  reqObj.put("email", email);
											  reqObj.put("password", pwd);
											  reqObj.put("user_id", user.getUser_id());
											  
											//  OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
											 // writer.write(reqObj.toString());
											  //writer.close();
											  
											//  req.setPayload(reqObj.toString().getBytes());
                                              //obj1=SyncApp.sentRequest(req);
                                             
                                              //int respCode = conn.getResponseCode(); // New items get NOT_FOUND on PUT

                                        obj1=SyncApp.sentRequest(url, reqObj);
                                        if(obj1.get("success").toString().equals("true"))
                                        {
                                        log.info("User succesfully registered in cross domain");
                                        obj1.put("detail", email);
                                        response.setStatus(200);
                                        }
                                        else
                                        {
                                        log.severe("User registration failed due to exceeding retry limit");
                                        response.setStatus(500);
                                        }
                                    }

                                                                                            else
                                            {
                                            obj1.put("success", true);
                                            obj1.put("code", 200);
                                            obj1.put("detail", email);
                                            }                       
                                                        
         
            out.println(obj1);
                
		

		} else {
			JSONObject obj = new JSONObject();
			//JSONObject obj1 = new JSONObject();
			response.setStatus(400);
			obj1.put("email", email);
			obj.put("success", false);
			obj.put("code", 400);
			obj.put("message", "user already exist");
			obj.put("user", obj1);
			out.println(obj);
			log.info("user already exist");

		}

		// request.getRequestDispatcher("loginpage").forward(request, response);

	}

            }

