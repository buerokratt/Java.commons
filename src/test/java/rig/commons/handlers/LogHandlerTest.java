package rig.commons.handlers;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.MDC;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class LogHandlerTest {

    private LogHandler handler;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private static final Object object = new Object();
    private static final ModelAndView modelAndView = new ModelAndView();
    private static final String PREFIX = "GUID_PREFIX";
    private static final String GUID = "REQ_GUID";

    @Before
    public void setUp() {
        handler = LogHandler.builder().build();
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @Test
    public void testThatKeyAddedToMDC() throws Exception {
        handler.preHandle(request, response, object);
        assertNotNull(MDC.get(GUID));
    }

    @Test
    public void testThatKeyRemovedFromMDC() throws Exception {
        handler.postHandle(request, response, object, modelAndView);
        assertNull(MDC.get(GUID));
    }

    @Test
    public void testDefaultMessagePrefix() throws Exception {
        handler.preHandle(request, response, object);
        assertEquals(MDC.get(PREFIX), "request with id ");
    }

    @Test
    public void testDefaultIncomingMessagePrefix() throws Exception {
        request.addHeader("REQUEST_ID", "444");
        handler.preHandle(request, response, object);
        assertEquals(MDC.get(GUID), "444");
    }

    @Test
    public void testDefaultEmptyMessagePrefix() throws Exception {
        request.addHeader("REQUEST_ID", "");
        handler.preHandle(request, response, object);
        assertEquals(MDC.get(PREFIX), "request with incoming empty id ");
    }

    @Test
    public void testNONDefaultMessagePrefix() throws Exception {
        LogHandler builtHandler = LogHandler.builder().messagePrefix("NONDEFAULT").build();
        builtHandler.preHandle(request, response, object);
        assertEquals(MDC.get(PREFIX), "NONDEFAULT");
    }

    @Test
    public void testNONDefaultIncomingMessagePrefix() throws Exception {
        request.addHeader("REQUEST_ID", "444");
        LogHandler builtHandler = LogHandler.builder().incomingMessagePrefix("NONDEFAULT").build();
        builtHandler.preHandle(request, response, object);
        assertEquals(MDC.get(PREFIX), "NONDEFAULT");
    }

    @Test
    public void testNONDefaultEmptyMessagePrefix() throws Exception {
        request.addHeader("REQUEST_ID", "");
        LogHandler builtHandler = LogHandler.builder().emptyMessagePrefix("NONDEFAULT").build();
        builtHandler.preHandle(request, response, object);
        assertEquals(MDC.get(PREFIX), "NONDEFAULT");
    }

    @Test
    public void testNONDefaultHeaderName() throws Exception {
        request.addHeader("NONDEFAULT", "444");
        LogHandler builtHandler = LogHandler.builder().headerName("NONDEFAULT").build();
        builtHandler.preHandle(request, response, object);
        assertEquals(MDC.get(GUID), "444");
    }

    @Test
    @Ignore
    public void testThreadSafety() {
        List<String> strings = new ArrayList();
        Collection syncedStrings = Collections.synchronizedCollection(strings);
        handler = LogHandler.builder().build();

        for (int i = 0; i < 30_000; i++) {
            new Thread("" + i) {

                public void run() {
                    request = new MockHttpServletRequest();
                    response = new MockHttpServletResponse();
                    try {
                        handler.preHandle(request, response, object);
                    }
                    catch (Exception e) {
                        throw new RuntimeException();
                    }
                    syncedStrings.add(MDC.get("REQ_GUID"));
                }

            }.start();
        }

        Set<String> set = new HashSet<>(strings);
        assertEquals(set.size(), syncedStrings.size());
    }

}
