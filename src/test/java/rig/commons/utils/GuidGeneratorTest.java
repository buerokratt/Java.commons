package rig.commons.utils;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class GuidGeneratorTest  {

    private GuidGenerator guidGenerator;

    @Before
    public void setup() {
        this.guidGenerator = new GuidGenerator();
    }

    @Test
    public void testGetID() {
        Assert.assertNotEquals(guidGenerator.getId(), guidGenerator.getId());
    }

}