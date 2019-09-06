package rig.commons.handlers;

import lombok.Builder;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import rig.commons.utils.GuidGenerator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Builder
public class LogHandler extends HandlerInterceptorAdapter {

    private static final String KEY = "REQ_GUID";

    private static final GuidGenerator guidGenerator = new GuidGenerator();

    @Builder.Default
    private String messagePrefix = "request with id ";
    @Builder.Default
    private String incomingMessagePrefix = "request with incoming id ";
    @Builder.Default
    private String emptyMessagePrefix = "request with incoming empty id ";
    @Builder.Default
    private String headerName = "REQUEST_ID";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestId = request.getHeader(headerName);
        String message;

        if (requestId == null) {
            requestId = String.valueOf(guidGenerator.getUniqueCurrentTimeMS());
            message = messagePrefix + requestId;
        }
        else {
            message = incomingMessagePrefix + requestId;

        }

        if (requestId.isEmpty()) {
            message = emptyMessagePrefix;
        }

        MDC.put(KEY, message);
        org.slf4j.MDC.put(KEY, message);
        return super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        MDC.remove(KEY);
        org.slf4j.MDC.remove(KEY);
        super.postHandle(request, response, handler, modelAndView);
    }
}

