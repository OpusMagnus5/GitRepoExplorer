package pl.bodzioch.damian.github;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClient;
import pl.bodzioch.damian.exception.AppException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class GitHubClient {

    private final static String SEARCH_REPO_PATH = "/search/repositories";
    private final static String QUERY_PARAM_NAME = "q";
    private final static String USER_QUALIFIER_NAME = "user";
    private static final String PER_PAGE_PARAM_NAME = "per_page";
    private static final int MAX_ITEMS_PER_PAGE = 100;
    private static final String PAGE_PARAM_NAME = "page";

    private final RestClient gitHubRestClient;

    public void getUserRepositories(String username) {
        GetRepositoryByUserResponse firstPage = getPageOfUserRepositories(username, 1);
        List<GetRepositoryByUserResponse> remainingRepositories = getRemainingPagesOfUserRepositories(username, firstPage.getTotalCount());
        remainingRepositories.add(0, firstPage);
        //TODO pobieranie branchy
    }

    private List<GetRepositoryByUserResponse> getRemainingPagesOfUserRepositories(String username, long totalCount) {
        List<CompletableFuture<GetRepositoryByUserResponse>> futures = new ArrayList<>();
        try (ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor()) {
            long pages = (long) Math.ceil((double) totalCount / MAX_ITEMS_PER_PAGE);
            for (long i = 2; i <= pages; i++) {
                long currentPage = i;
                futures.add(CompletableFuture.supplyAsync(() -> getPageOfUserRepositories(username, currentPage), executorService));
            }

            CompletableFuture<Void> allFutures = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
            allFutures.get(1, TimeUnit.MINUTES);
            return futures.stream()
                    .map(CompletableFuture::join)
                    .collect(Collectors.toList());
        } catch (ExecutionException | InterruptedException e) {
            log.error("An error occurred while waiting for futures to complete.", e);
            throw new AppException("github.GitHubClient.getRepositories.error", HttpStatus.INTERNAL_SERVER_ERROR, e);
        } catch (TimeoutException e) {
            log.warn("A timeout occurred while waiting for futures to complete.");
            throw new AppException("github.GitHubClient.getRepositories.timeout", HttpStatus.REQUEST_TIMEOUT, e);
        }
    }

    private GetRepositoryByUserResponse getPageOfUserRepositories(String username, long page) {
        try {
            return gitHubRestClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path(SEARCH_REPO_PATH)
                            .queryParam(QUERY_PARAM_NAME, USER_QUALIFIER_NAME + ":" + username)
                            .queryParam(PER_PAGE_PARAM_NAME, MAX_ITEMS_PER_PAGE)
                            .queryParam(PAGE_PARAM_NAME, page)
                            .build())
                    .retrieve()
                    .toEntity(GetRepositoryByUserResponse.class)
                    .getBody();
        } catch (HttpClientErrorException e) {
            log.error("User with username {} not found", username, e);
            throw new AppException("github.GitHubClient.userNotFound", HttpStatus.NOT_FOUND);
        } catch (HttpServerErrorException e) {
            log.error("An error occurred while fetching repositories for username {}", username, e);
            throw new AppException("github.GitHubClient.getRepositories.error", HttpStatus.INTERNAL_SERVER_ERROR, e);
        }
    }
}
