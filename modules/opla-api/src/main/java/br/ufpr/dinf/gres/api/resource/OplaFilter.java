package br.ufpr.dinf.gres.api.resource;

import br.ufpr.dinf.gres.architecture.io.OPLAThreadScope;
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
public class OplaFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = getAuthorization(request);
        OPLAThreadScope.token.set(token);
        if (token != null || request.getMethod().equals("OPTIONS") || request.getRequestURI().equals("/api/user/login")) {
            filterChain.doFilter(request, response);
        } else {
            String s = "METHOD: " + request.getMethod() + " URI: " + request.getRequestURI();
            throw new RuntimeException("NOT_ALLOWED::" + s);
        }
    }

    private String getAuthorization(HttpServletRequest request) {
        String token = request.getHeader("authorization");
        token = token != null ? token : request.getParameter("authorization");
        return token;
    }
}
