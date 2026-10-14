import java.net.HttpURLConnection;
import java.net.URI;

/**
 * Container health probe used by the Dockerfile HEALTHCHECK.
 * The distroless runtime image has no shell, curl or wget, so the JRE does the HTTP call.
 * Exits with 0 when the URL responds with a 2xx status, 1 otherwise.
 */
public final class HealthCheck {
    private HealthCheck() {
    }

    public static void main(String[] args) {
        var url = args.length > 0 ? args[0] : "http://localhost:8080/actuator/health";
        try {
            var connection = (HttpURLConnection) URI.create(url).toURL().openConnection();
            connection.setConnectTimeout(2_000);
            connection.setReadTimeout(3_000);
            var status = connection.getResponseCode();
            System.exit(status >= 200 && status < 300 ? 0 : 1);
        } catch (Exception e) {
            System.err.println(e);
            System.exit(1);
        }
    }
}
