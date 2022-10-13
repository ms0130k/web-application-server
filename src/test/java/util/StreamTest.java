package util;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

public class StreamTest {

    @Test
    public void output_WhenFileIsNotExists() {
        String filename = "/Temp/text.txt";
        String text = "HTTP\r\n" + "TEXT\r\n" + "\r\n" + "body";

        try (OutputStream os = new FileOutputStream(new File(filename));) {
            os.write(text.getBytes(StandardCharsets.UTF_8));
            assertEquals(true, new File(filename).exists());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (InputStream is = new FileInputStream(new File(filename));
                BufferedReader br = new BufferedReader(new InputStreamReader(is));) {
            String[] results = new String[4];

            for (int i = 0; i < results.length; i++) {
                results[i] = br.readLine();
            }

            assertEquals(new String[] { "HTTP", "TEXT", "", "body" }, results);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

}
