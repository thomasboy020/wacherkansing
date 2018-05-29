package nl.hu.v1wac.herkansing.webservices;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;
import nl.hu.v1wac.herkansing.persistence.UserDAO;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.security.Key;
import java.util.Calendar;


@Path("/authentication")
public class AuthenticationResource {
    final static public Key key = MacProvider.generateKey();

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response authenticateUser(@FormParam("username") String username,
                                     @FormParam("password") String password) {
        System.out.println("Authentication");
        try {
            // Authenticate the user against the database
            UserDAO dao = new UserDAO();
            String role = dao.findRoleForUsernameAndPassword(username, password);

            if (role == null) { throw new IllegalArgumentException("No user found!"); }

            // Issue a token for the user
            Calendar expiration = Calendar.getInstance();
            expiration.add(Calendar.MINUTE, 30);

            String token = Jwts.builder()
                    .setSubject(username)
                    .claim("role", role)
                    .setExpiration(expiration.getTime())
                    .signWith(SignatureAlgorithm.HS512, key)
                    .compact();
            // Return the token on the response
            return Response.ok(token).build();
        } catch (JwtException | IllegalArgumentException e) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response registerUser(@FormParam("username") String username,
                                 @FormParam("password") String password,
                                 @FormParam("password_confirmation") String password_confirmation) {
        boolean success = false;
        try {
            UserDAO dao = new UserDAO();
            if(password.equals(password_confirmation)) {
                success = dao.registerUser(username, password, "user");
            }


            if(success) {
                Calendar expiration = Calendar.getInstance();
                expiration.add(Calendar.MINUTE, 30);

                String token = Jwts.builder()
                        .setSubject(username)
                        .claim("role", "user")
                        .setExpiration(expiration.getTime())
                        .signWith(SignatureAlgorithm.HS512, key)
                        .compact();
                // Return the token on the response
                return Response.ok(token).build();
            }

        } catch (JwtException | IllegalArgumentException e) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }


        return Response.status(Response.Status.NOT_ACCEPTABLE).build();
    }
}