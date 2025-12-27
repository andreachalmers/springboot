package com.example.portlet.controller;

import com.example.mongodb.model.User;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.portlet.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Portlet Controller for User Management
 * 
 * IMPORTANT: This portlet requires a portlet container (like Liferay) to run.
 * For standalone Spring Boot applications, use the REST API endpoints instead.
 * 
 * This is a basic portlet implementation that demonstrates portlet functionality.
 * To use this portlet:
 * 1. Deploy the application to a portlet container (e.g., Liferay)
 * 2. Configure the portlet in the portal (see portlet.xml)
 * 3. The portlet will be available in the portal's portlet registry
 * 
 * Note: Spring Boot 3 has limited portlet support. For production portlet applications,
 * consider using a dedicated portlet container or migrating to REST APIs with Vue.js.
 */
@Component
public class UserPortletController implements Portlet {
    
    @Autowired
    private UserService userService;
    
    @Override
    public void init(PortletConfig config) throws PortletException {
        // Initialization logic if needed
    }
    
    @Override
    public void processAction(ActionRequest request, ActionResponse response) 
            throws PortletException, IOException {
        String action = request.getParameter("action");
        
        if ("addUser".equals(action)) {
            addUser(request, response);
        } else if ("deleteUser".equals(action)) {
            deleteUser(request, response);
        }
    }
    
    @Override
    public void render(RenderRequest request, RenderResponse response) 
            throws PortletException, IOException {
        response.setContentType("text/html");
        PrintWriter writer = response.getWriter();
        
        try {
            List<User> users = userService.getAllUsers();
            
            writer.println("<div class='portlet-container'>");
            writer.println("<h3>User Management Portlet</h3>");
            writer.println("<table border='1' style='width:100%; border-collapse:collapse;'>");
            writer.println("<tr><th>Username</th><th>Email</th><th>First Name</th><th>Last Name</th><th>Actions</th></tr>");
            
            for (User user : users) {
                writer.println("<tr>");
                writer.println("<td>" + user.getUsername() + "</td>");
                writer.println("<td>" + user.getEmail() + "</td>");
                writer.println("<td>" + user.getFirstName() + "</td>");
                writer.println("<td>" + user.getLastName() + "</td>");
                writer.println("<td><a href='" + response.createActionURL() + "&action=deleteUser&userId=" 
                    + user.getId() + "'>Delete</a></td>");
                writer.println("</tr>");
            }
            
            writer.println("</table>");
            writer.println("</div>");
        } catch (Exception e) {
            writer.println("<div class='error'>Error loading users: " + e.getMessage() + "</div>");
        }
    }
    
    private void addUser(ActionRequest request, ActionResponse response) throws PortletException {
        try {
            String username = request.getParameter("username");
            String email = request.getParameter("email");
            String firstName = request.getParameter("firstName");
            String lastName = request.getParameter("lastName");
            
            User user = new User();
            user.setUsername(username);
            user.setEmail(email);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            
            userService.createUser(user);
        } catch (Exception e) {
            throw new PortletException("Error creating user", e);
        }
    }
    
    private void deleteUser(ActionRequest request, ActionResponse response) throws PortletException {
        try {
            String userId = request.getParameter("userId");
            if (userId != null && !userId.isEmpty()) {
                userService.deleteUser(userId);
            }
        } catch (Exception e) {
            throw new PortletException("Error deleting user", e);
        }
    }
    
    @Override
    public void destroy() {
        // Cleanup logic if needed
    }
}

