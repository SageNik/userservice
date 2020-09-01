package userservice.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.net.http.HttpRequest.BodyPublishers.ofInputStream;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class KafkaConnectorInitializer {

    @Value(value = "${kafka-connect.uri}")
    private static String KAFKA_CONNECTOR_URI;

    @Value(value = "${kafka-connect}")
    private static String KAFKA_CONNECT_CONNECTORS_FOLDER_PATH;

    public static void setConnectors() {
        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            HttpPost request = new HttpPost(KAFKA_CONNECTOR_URI);
            Path path = Paths.get(KAFKA_CONNECT_CONNECTORS_FOLDER_PATH);
            if (Files.isDirectory(path)) {
                Files.list(path).forEach(filePath -> {
                    try {
                        StringEntity params = new StringEntity(ofInputStream(() ->
                                KafkaConnectorInitializer.class.getClassLoader().getResourceAsStream(filePath.toString())).toString());
                        request.addHeader("content-type", "application/json");
                        request.setEntity(params);
                        httpClient.execute(request);
                    } catch (IOException e) {
                        log.error("Connector by file:" + filePath + " hasn't been created");
                    }
                });

            }

        } catch (Exception ex) {
            log.error("Kafka connect setting connectors failed");
        }
    }
}
