package pl.bodzioch.damian.github;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

import java.util.List;

@Configuration
public class GitHubClientConf {

    private final String token;

    public GitHubClientConf(@Value("${gitHub.GitHubClientConf.token}") String token) {
        this.token = token;
    }

    @Bean("gitHubRestClient")
    protected RestClient restClient() {
        return RestClient.builder()
                .baseUrl("https://api.github.com/")
                .defaultHeaders(this::createDefaultHeaders)
                .build();
    }

    private void createDefaultHeaders(HttpHeaders httpHeaders) {
        httpHeaders.setBearerAuth(token);
        httpHeaders.setAccept(List.of(MediaType.parseMediaType("application/vnd.github+json")));
    }
}
