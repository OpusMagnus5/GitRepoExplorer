package pl.bodzioch.damian.github;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClient;
import pl.bodzioch.damian.exception.AppException;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class GitHubClientImpl implements GitHubClient {

    private final static String USERS_PATH = "users";
    private final static String REPOSITORIES_PATH = "repos";
    private static final String BRANCHES_PATH = "branches";

    private final RestClient gitHubRestClient;

    public List<GitRepositoryModel> getRepositories(String username) {
        return getUserRepositories(username).stream()
                .map(GitRepositoryModel::of)
                .collect(Collectors.toList());
    }

    public List<GitBranchModel> getBranches(String username, String repositoryName) {
        return getRepositoryBranches(repositoryName, username).stream()
                .map(GitBranchModel::of)
                .collect(Collectors.toList());
    }

    private List<RepositoryResponse> getUserRepositories(String username) {
        try {
            ResponseEntity<RepositoryResponse[]> response = gitHubRestClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .pathSegment(USERS_PATH, username, REPOSITORIES_PATH)
                            .build())
                    .retrieve()
                    .toEntity(RepositoryResponse[].class);

            if (response.getStatusCode() == HttpStatus.OK && response.hasBody()) {
                return Arrays.asList(response.getBody());
            } else {
                throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
            }
        } catch (HttpClientErrorException e) {
            log.error("User with username {} not found", username, e);
            throw new AppException("github.GitHubClient.userNotFound", HttpStatus.NOT_FOUND);
        } catch (HttpServerErrorException e) {
            log.error("An error occurred while fetching repositories for username {}", username, e);
            throw new AppException("github.GitHubClient.getRepositories.error", HttpStatus.INTERNAL_SERVER_ERROR, e);
        }
    }

    private List<GetBranchResponse> getRepositoryBranches(String repository, String username) {
        try {
            ResponseEntity<GetBranchResponse[]> response = gitHubRestClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .pathSegment(REPOSITORIES_PATH, username, repository, BRANCHES_PATH)
                            .build())
                    .retrieve()
                    .toEntity(GetBranchResponse[].class);

            if (response.getStatusCode() == HttpStatus.OK && response.hasBody()) {
                return Arrays.asList(response.getBody());
            } else {
                throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
            }
        } catch (HttpClientErrorException e) {
            log.error("Repository with name {} and user {} not found", repository, username, e);
            throw new AppException("github.GitHubClient.repositoryNotFound", HttpStatus.NOT_FOUND);
        } catch (HttpServerErrorException e) {
            log.error("An error occurred while fetching branches for repository name {} an user {}", repository, username, e);
            throw new AppException("github.GitHubClient.getBranches.error", HttpStatus.INTERNAL_SERVER_ERROR, e);
        }
    }
}
