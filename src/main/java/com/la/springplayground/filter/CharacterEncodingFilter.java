package com.la.springplayground.filter;

/**
 *
 * @author Luís Alves
 */

import java.io.IOException;
import javax.servlet.*;

public class CharacterEncodingFilter implements Filter {

    public void init(FilterConfig config) throws ServletException {
        //No-op
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
                                                           throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        chain.doFilter(request, response);
    }

    public void destroy() {
        //No-op
    }
}
