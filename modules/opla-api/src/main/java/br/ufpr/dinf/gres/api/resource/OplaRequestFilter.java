package br.ufpr.dinf.gres.api.resource;

import br.ufpr.dinf.gres.domain.OPLAThreadScope;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Order(1)
public class OplaRequestFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        response.setHeader("Access-Control-Allow-Origin", "*");
//        response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, DELETE, OPTIONS,HEAD");
//        response.setHeader("Access-Control-Allow-Headers", "Content-Type, authorization, Connection, group");
        String token = getAuthorization(request);
        OPLAThreadScope.token.set(token);
        if (request.getRequestURI().equals("/api/user/login") || request.getRequestURI().equals("/api/user/forgot") || request.getMethod().equals("OPTIONS")) filterChain.doFilter(request, response);
        else if (token == null && request.getRequestURI().startsWith("/api")) {
            String s = "METHOD: " + request.getMethod() + " URI: " + request.getRequestURI();
            throw new RuntimeException("NOT_ALLOWED::" + s);
        } else {
            filterChain.doFilter(request, response);
        }
    }

    private String getAuthorization(HttpServletRequest request) {
        String token = request.getHeader("authorization");
        token = token != null ? token : request.getParameter("authorization");
        return token;
    }
}
