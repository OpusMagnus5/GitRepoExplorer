package pl.bodzioch.damian.github;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@AllArgsConstructor
public class GitHubClient {

    private final static String SEARCH_REPO_PATH = "/search/repositories";
    private final static String USER_PARAM_NAME = "user";

    private final RestClient gitHubRestClient;

    protected void getUserRepositories(String username) {
        gitHubRestClient.get()
                .uri(UriComponentsBuilder
                        .fromPath(SEARCH_REPO_PATH)
                        .queryParam(USER_PARAM_NAME, username)
                        .build().toUri())
                .retrieve(); //TODO
    }
}
