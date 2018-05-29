package nl.hu.v1wac.herkansing.webservices;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {
    @Override
    public void filter(ContainerRequestContext requestCtx) throws IOException {
        System.out.println("AuthenticationFilter");

        // Users are treated as guests, unless a valid JWT is provided
        boolean isSecure = requestCtx.getSecurityContext().isSecure();
        MySecurityContext msc = new MySecurityContext("Unknown", "guest", isSecure);
        // Check if the HTTP Authorization header is present and formatted correctly
        String authHeader = requestCtx.getHeaderString(HttpHeaders.AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            // Extract the token from the HTTP Authorization header
            String token = authHeader.substring("Bearer".length()).trim();
            try {
                // Validate the token
                JwtParser parser = Jwts.parser().setSigningKey(AuthenticationResource.key);
                Claims claims = parser.parseClaimsJws(token).getBody();

                String user = claims.getSubject();
                String role = claims.get("role").toString();

                msc = new MySecurityContext(user, role, isSecure);

                System.out.println(user+" : "+role);
                System.out.println(msc.toString());

            } catch (JwtException | IllegalArgumentException e) {
                System.out.println("Invalid JWT, processing as guest!");
            }
        }

        requestCtx.setSecurityContext(msc);
    }
}
