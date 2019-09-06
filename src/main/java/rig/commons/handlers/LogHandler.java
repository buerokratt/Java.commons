package rig.commons.handlers;

import lombok.Builder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import rig.commons.utils.GuidGenerator;
import rig.commons.utils.IDGenerator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Builder
public class LogHandler extends HandlerInterceptorAdapter {

    private static final String GUID = "REQ_GUID";
    private static final String PREFIX = "GUID_PREFIX";

    @Builder.Default
    private static final IDGenerator guidGenerator = new GuidGenerator();
    @Builder.Default
    private static final DynamicContent mdc = new MDCwrapper();

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
        String guidPrefix;

        if (requestId == null) {
            requestId = String.valueOf(guidGenerator.getId());
            guidPrefix = messagePrefix;
        }
        else {
            guidPrefix = incomingMessagePrefix;

        }

        if (requestId.isEmpty()) {
            guidPrefix = emptyMessagePrefix;
        }

        mdc.put(PREFIX, guidPrefix);
        mdc.put(GUID, requestId);
        return super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        mdc.remove(GUID);
        mdc.remove(PREFIX);
        super.postHandle(request, response, handler, modelAndView);
    }
}

