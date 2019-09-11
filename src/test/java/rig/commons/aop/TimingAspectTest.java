package rig.commons.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TimingAspectTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private ProceedingJoinPoint proceedingJoinPoint;

    private TimingAspect aspect = new TimingAspect();

    private MethodSignature signature = mock(MethodSignature.class);

    @Before
    public void setUp() {
        when(proceedingJoinPoint.getSignature()).thenReturn(signature);
        when(signature.toShortString()).thenReturn("TESTSIGNATURE");
    }

    @Test
    public void testLogging() throws Throwable {
        aspect.logExecutionTime(proceedingJoinPoint);
        String pattern = "TESTSIGNATURE started at (\\d\\d\\.){2}\\d{4} (\\d\\d:){2}\\d\\d\\.\\d{3}" +
                ", execution took \\d+ ms\\.";
        String logString = StaticAppender.getEvents().get(0);
        Assert.assertTrue(logString.matches(pattern));
    }
}
