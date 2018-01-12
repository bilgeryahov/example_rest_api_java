package tci_crawler.integration_testing;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.InputStream;

/**
 * Not tests.
 * Helper functions.
 */
public class IntegrationTestsUtil {

    public static String ConvertToString(HttpEntity entity) throws IOException {
        return EntityUtils.toString(entity);
    }

    public static String ConvertFromJSONFileToString(String urlFromFile) {
        try {
            //Get file from resources folder
            InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(urlFromFile);
            return IOUtils.toString(stream);
        } catch (Exception e) {
        }
        return "";
    }

    public static String setTimeToZero(String jsonString) {
        String[] strings = jsonString.split("\"time\":");
        return strings[0] + "\"time\":0}";
    }

    public static String setElapsedTimeToZero(String jsonString) {
        String[] strings = jsonString.split("\"time_elapsed\":");
        return strings[0] + "\"time_elapsed\":0," + strings[1].split(",", 2)[1];
    }
}
