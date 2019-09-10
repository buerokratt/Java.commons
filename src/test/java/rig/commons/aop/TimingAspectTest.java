package rig.commons.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.hamcrest.CoreMatchers;
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
        String logString = StaticAppender.getEvents().get(0);
        Assert.assertThat(logString, CoreMatchers.containsString("TESTSIGNATURE started at"));
        Assert.assertThat(logString, CoreMatchers.containsString("execution took"));
    }
}
