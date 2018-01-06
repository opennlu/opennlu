package tests.configuration;

import org.junit.Assert;
import org.junit.Test;
import org.opennlu.json.JsonConfig;

import java.io.File;

/**
 * Created by René Preuß on 6/15/2017.
 */
public class JsonConfigTest {
    @Test
    public void testLoadConfiguration() throws Exception {
        File jsonFile = new File("test.json");

        JsonConfig configuration = JsonConfig.loadConfiguration(jsonFile);
        configuration.set("test", "test");
        configuration.save(jsonFile);

        Assert.assertTrue(jsonFile.delete());
    }
}
