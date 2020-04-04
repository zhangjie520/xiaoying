package com.example.xiaoying.interceptor;

//import com.example.community.enums.AdPosEnum;
//import com.example.community.mapper.UserMapper;
//import com.example.community.model.User;
//import com.example.community.model.UserExample;
//import com.example.community.service.AdService;
//import com.example.community.service.NotificationService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
import com.example.xiaoying.mapper.UserMapper;
import com.example.xiaoying.model.User;
import com.example.xiaoying.model.UserExample;
import com.example.xiaoying.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Component
public class SessionInterceptor implements HandlerInterceptor {
    @Resource
    private UserMapper userMapper;
    @Autowired
    private NotificationService notificationService;
//    @Autowired
//    private AdService adService;
//    @Value("${github.redirect.uri}")
//    private String redirectUri;
//    @Value("${github.client.id}")
//    private String clientId;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        request.getServletContext().setAttribute("redirectUri",redirectUri);
//        request.getServletContext().setAttribute("clientId",clientId);
        Cookie[] cookies = request.getCookies();
//        for (AdPosEnum adPosEnum : AdPosEnum.values()) {
//            request.getServletContext().setAttribute(adPosEnum.name(),adService.list(adPosEnum.name()));
//        }

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("accountId")) {
                    String accountId = cookie.getValue();
                    UserExample userExample = new UserExample();
                    userExample.createCriteria()
                            .andAccountIdEqualTo(accountId);
                    List<User> users = userMapper.selectByExample(userExample);
                    if (users.size() != 0) {
                        request.getSession().setAttribute("user", users.get(0));
                        Long unReadCount=notificationService.unReadCount(Long.valueOf(users.get(0).getAccountId()));
                        request.getSession().setAttribute("unreadCount",unReadCount);
                    }
                    break;
                }

            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
