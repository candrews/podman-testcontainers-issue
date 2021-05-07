import java.time.Duration;
import java.util.Map;

import io.netty.handler.codec.http.HttpMethod;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.MediaType;
import org.mockserver.verify.VerificationTimes;
import org.testcontainers.Testcontainers;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.startupcheck.OneShotStartupCheckStrategy;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

@Slf4j
/* default */ class CurlToHostTest {
	private ClientAndServer mockServer;
	private final int MOCK_SERVER_PORT = 2435;

	@BeforeEach
	public void startMockServer() {
		mockServer = ClientAndServer.startClientAndServer(MOCK_SERVER_PORT);
	}

	@AfterEach
	public void stopMockServer() {
		mockServer.stop();
	}

	@Test
	/* default */ void testGet() throws Exception {
		final String path = "/path";
		final String body = "success body";
		mockServer.when(request().withPath(path)).respond(response().withStatusCode(200).withContentType(MediaType.TEXT_PLAIN).withBody(body));
		Testcontainers.exposeHostPorts(Map.of(MOCK_SERVER_PORT, 80));

		try (GenericContainer<?> container = new GenericContainer<>("curlimages/curl:7.76.0")) {
			container
				.withLogConsumer(new Slf4jLogConsumer(log))
				.withCommand(String.format("curl -sS http://%s:%d%s", GenericContainer.INTERNAL_HOST_HOSTNAME, 80, path))
				.withStartupCheckStrategy(
						new OneShotStartupCheckStrategy().withTimeout(Duration.ofSeconds(20))
					).start();
			assertThat(container.getLogs()).isEqualTo(body);
		}

		mockServer.verify(request().withMethod(HttpMethod.GET.name()).withPath(path), VerificationTimes.once());
	}
}
