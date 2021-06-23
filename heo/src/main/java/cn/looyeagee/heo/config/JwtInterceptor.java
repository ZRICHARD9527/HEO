package cn.looyeagee.heo.config;


import cn.looyeagee.heo.util.Tools;
import io.jsonwebtoken.*;
import io.swagger.models.auth.In;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//拦截器
public class JwtInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //自动排除生成token的路径,并且如果是options请求是cors跨域预请求，设置allow对应头信息
        String myuri = request.getRequestURI();

        if (RequestMethod.OPTIONS.toString().equals(request.getMethod())) {
            System.out.println("自动排除生成token的路径");
            return true;
        }


        String token = request.getHeader("jwt");
        if (token == null || "".equals(token.trim())) {
            throw new ServletException("无法获取token");
        }

        try {
            Claims claims = Tools.parseJwt(token);
            request.setAttribute("userId", Integer.valueOf(claims.getId()));
            return true;

        } catch (ExpiredJwtException e) {
            throw new ServletException("身份过期，请重新登录");
        }catch ( UnsupportedJwtException | MalformedJwtException| SignatureException|IllegalArgumentException e) {
            throw new ServletException("jwt解析失败");
        }
    }

}
