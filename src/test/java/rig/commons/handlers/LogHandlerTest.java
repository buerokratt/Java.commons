package rig.commons.handlers;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.MDC;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class LogHandlerTest {

    private LogHandler handler;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private static final Object object = new Object();
    private static final ModelAndView modelAndView = new ModelAndView();

    @Before
    public void setUp() {
        handler = LogHandler.builder().build();
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @Test
    public void testThatKeyAddedToMDC() throws Exception {
        handler.preHandle(request, response, object);
        Assert.assertNotNull(MDC.get("REQ_GUID"));
    }

    @Test
    public void testThatKeyRemovedFromMDC() throws Exception {
        handler.postHandle(request, response, object, modelAndView);
        Assert.assertNull(MDC.get("REQ_GUID"));
    }

    @Test
    public void testDefaultMessagePrefix() throws Exception {
        handler.preHandle(request, response, object);
        Assert.assertThat(MDC.get("REQ_GUID"), CoreMatchers.containsString("request with id "));
    }

    @Test
    public void testDefaultIncomingMessagePrefix() throws Exception {
        request.addHeader("REQUEST_ID", "444");
        handler.preHandle(request, response, object);
        Assert.assertEquals(MDC.get("REQ_GUID"), "request with incoming id 444");
    }

    @Test
    public void testDefaultEmptyMessagePrefix() throws Exception {
        request.addHeader("REQUEST_ID", "");
        handler.preHandle(request, response, object);
        Assert.assertEquals(MDC.get("REQ_GUID"), "request with incoming empty id ");
    }

    @Test
    public void testNONDefaultMessagePrefix() throws Exception {
        LogHandler builtHandler = LogHandler.builder().messagePrefix("NONDEFAULT").build();
        builtHandler.preHandle(request, response, object);
        Assert.assertThat(MDC.get("REQ_GUID"), CoreMatchers.containsString("NONDEFAULT"));
    }

    @Test
    public void testNONDefaultIncomingMessagePrefix() throws Exception {
        request.addHeader("REQUEST_ID", "444");
        LogHandler builtHandler = LogHandler.builder().incomingMessagePrefix("NONDEFAULT").build();
        builtHandler.preHandle(request, response, object);
        Assert.assertEquals(MDC.get("REQ_GUID"), "NONDEFAULT444");
    }

    @Test
    public void testNONDefaultEmptyMessagePrefix() throws Exception {
        request.addHeader("REQUEST_ID", "");
        LogHandler builtHandler = LogHandler.builder().emptyMessagePrefix("NONDEFAULT").build();
        builtHandler.preHandle(request, response, object);
        Assert.assertEquals(MDC.get("REQ_GUID"), "NONDEFAULT");
    }

    @Test
    public void testNONDefaultHeaderName() throws Exception {
        request.addHeader("NONDEFAULT", "444");
        LogHandler builtHandler = LogHandler.builder().headerName("NONDEFAULT").build();
        builtHandler.preHandle(request, response, object);
        Assert.assertEquals(MDC.get("REQ_GUID"), "request with incoming id 444");
    }

    @Test
    public void testThreadSafety() {
        List<String> strings = new ArrayList();
        for(int i=0; i<10; i++){
            new Thread("" + i){
                public void run()  {

                    handler = LogHandler.builder().build();
                    request = new MockHttpServletRequest();
                    response = new MockHttpServletResponse();
                    try {
                    handler.preHandle(request, response, object);}
                    catch (Exception e){
                        System.out.println(e);
                    }
                    strings.add(MDC.get("REQ_GUID"));
                }
            }.start();
        }

        Set<String> set = new HashSet<>(strings);
        Assert.assertEquals(set.size(),strings.size());

    }

}
