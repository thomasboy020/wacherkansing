package nl.hu.v1wac.herkansing.webservices;

import javax.ws.rs.core.SecurityContext;
import java.security.Principal;

public class MySecurityContext implements SecurityContext {
    private String name;
    private String role;
    private boolean isSecure;

    public MySecurityContext(String name, String role, boolean isSecure) {
        this.name = name;
        this.role = role;
        System.out.println("Secure: " + isSecure);
        System.out.println("MySecurityContext");
    }

    public Principal getUserPrincipal() {
        System.out.println("AuthenticationFilter/getUserPrincipal");
        return () -> name;
    }
    public boolean isUserInRole(String role) { System.out.println("AuthenticationFilter/isUserInRole, role: " + role + ", role2: " + this.role); return role.equals(this.role); }
    public boolean isSecure() { System.out.println("AuthenticationFilter/isSecure"); return isSecure; }
    public String getAuthenticationScheme() { System.out.println("AuthenticationFilter/getAuthenticationScheme"); return "Bearer"; }

    @Override
    public String toString() {
        return "MySecurityContext{" +
                "name='" + name + '\'' +
                ", role='" + role + '\'' +
                ", isSecure=" + isSecure +
                '}';
    }
}