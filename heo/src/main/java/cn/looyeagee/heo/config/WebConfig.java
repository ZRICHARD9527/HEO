package cn.looyeagee.heo.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // 这个方法是用来配置静态资源的，比如html，js，css，等等
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        File dir = new File("/home/" + System.getProperty("user.name") + "/clbb/uploads/");
        if (!dir.exists()) {
            System.out.println("----------创建文件夹" + dir.mkdirs());
        }
        //前者资源映射地址，后者为上传地址
        registry.addResourceHandler("/uploads/**").addResourceLocations("file:/home/" + System.getProperty("user.name") + "/clbb/uploads/");

    }

    // 这个方法用来注册拦截器，我们自己写好的拦截器需要通过这里添加注册才能生效
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // addPathPatterns("/**") 表示拦截所有的请求，
        // excludePathPatterns("/login", "/register") 表示除了登陆与注册之外，因为登陆注册不需要登陆也可以访问
        // /*不包括子目录，/**包括子目录 ,/ 只包括路径型url不包括后缀型url
        registry.addInterceptor(new JwtInterceptor())
                .addPathPatterns("/user/**")
                .addPathPatterns("/action/**")
                .addPathPatterns("/study/**")
                .excludePathPatterns("/user/login");

    }
}
