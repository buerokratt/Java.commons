package rig.commons.utils;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GuidGeneratorTest  {

    private GuidGenerator guidGenerator;

    @Before
    public void setup() {
        this.guidGenerator = new GuidGenerator();
    }

    @Test
    public void testGetID() {
        assertEquals(guidGenerator.getUniqueCurrentTimeMS(), guidGenerator.getUniqueCurrentTimeMS());
    }

}