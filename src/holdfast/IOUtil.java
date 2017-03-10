package holdfast;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 *
 * @author aNNiMON, HoldFast
 */
public final class IOUtil {

    public static IOUtil th = new IOUtil();

    public static String get(String link) {
        try {
            final URL url = new URL(link);
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setConnectTimeout(2000);
            return readResponse(http.getInputStream());
        } catch (IOException ex) {
//            Log.error(ex);
            return "";
        }
    }

    public static String getWithUserAgent(String address, String userAgent) throws IOException {
        URL urls = new URL(address);
        HttpURLConnection http = (HttpURLConnection) urls.openConnection();
        http.setRequestProperty("User-Agent", userAgent);
        http.setRequestProperty("Charset", "UTF-8");
        http.setConnectTimeout(2000);
        return readResponse(http.getInputStream());

    }

    public static String uploadTelegram(String link, String params, byte[] data, String ext) {
        try {
            final URL url = new URL(link);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setConnectTimeout(2000);
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            final String boundary = Long.toHexString(System.currentTimeMillis());
            con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

            try {
                DataOutputStream writer = new DataOutputStream(con.getOutputStream());

                //  writer.write(params.getBytes());
                writer.write(("\r\n--" + boundary + "\r\n").getBytes());
                writer.write(("Content-Disposition: form-data; name=\"" + params + "\"; filename=\"audio." + ext + "\"\r\n").getBytes());
                //  writer.write(("Content-Type: image/" + ext + "\r\n").getBytes());
                writer.write("Content-Transfer-Encoding: binary\r\n\r\n".getBytes());
                writer.write(data);
                writer.write(("\r\n--" + boundary + "--\r\n").getBytes());
                writer.flush();
            } catch (IOException ex) {
//                Log.error(ex);
                return "";
            }

            return readResponse(con.getInputStream());
        } catch (IOException ex) {
//            Log.error(ex);
            return "";
        }
    }

    public static String upload(String link, String params, byte[] data, String ext) {
        try {
            final URL url = new URL(link);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setConnectTimeout(2000);
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            final String boundary = Long.toHexString(System.currentTimeMillis());
            con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

            try {
                DataOutputStream writer = new DataOutputStream(con.getOutputStream());

                //  writer.write(params.getBytes());
                writer.write(("\r\n--" + boundary + "\r\n").getBytes());
                writer.write(("Content-Disposition: form-data; name=\"file\"; filename=\"" + link.hashCode() + "." + ext + "\"\r\n").getBytes());
                // writer.write(("Content-Type: image/" + ext + "\r\n").getBytes());
                writer.write("Content-Transfer-Encoding: binary\r\n\r\n".getBytes());
                writer.write(data);
                writer.write(("\r\n--" + boundary + "--\r\n").getBytes());
                writer.flush();
            } catch (IOException ex) {
//                Log.error(ex);
                return "";
            }

            return readResponse(con.getInputStream());
        } catch (IOException ex) {
//            Log.error(ex);
            return "";
        }
    }

    public static byte[] download(String link) throws MalformedURLException, IOException {
        ByteArrayOutputStream uploadParams = new ByteArrayOutputStream();
        /// try {
        final URL url = new URL(link);
        HttpURLConnection http = (HttpURLConnection) url.openConnection();
        http.setConnectTimeout(2000);
        http.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.17 (KHTML, like Gecko) Chrome/24.0.1312.56 Safari/537.17");
        InputStream is = http.getInputStream();
        byte[] byteChunk = new byte[4096];
        int n;
        while ((n = is.read(byteChunk)) > 0) {
            uploadParams.write(byteChunk, 0, n);
        }
        return uploadParams.toByteArray();
    }

    public static String post(String link, byte[] data) {
        try {
            final URL url = new URL(link);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setConnectTimeout(2000);
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Length", "" + data.length);
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            con.setDoOutput(true);
            try {
                DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                wr.write(data);
                wr.flush();
            } catch (IOException ioe) {
//                Log.error(ioe);
            }
            return readResponse(con.getInputStream());
        } catch (IOException ex) {
            //     Log.error(ex);
            return "";
        }
    }

    public static String readResponse(InputStream is) throws IOException {
        StringBuilder response = new StringBuilder();
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine).append(System.lineSeparator());
            }
        } catch (IOException ioe) {
            //   Log.error(ioe);
        }
        return response.toString();
    }

    public static String stackTraceToString(Throwable t) {
        try {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            t.printStackTrace(pw);
            return sw.toString();
        } catch (Exception ex) {
//            Log.error(ex);
            return "";
        }
    }
}
