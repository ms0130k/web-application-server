package util;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MapTest {
    private static final Logger log = LoggerFactory.getLogger(MapTest.class);

    @Test
    public void test() {
        Map<String, String> map = new HashMap<String, String>();
        map.putAll(new HashMap<String, String>());
        assertEquals(0, map.size());
        map.put("1", "1");
        map.putAll(new HashMap<String, String>());
        assertEquals(1, map.size());
    }

    @Test
    public void logMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("1", "1");
        log.debug("map: {}", map);
        System.out.println(map);
    }
}
