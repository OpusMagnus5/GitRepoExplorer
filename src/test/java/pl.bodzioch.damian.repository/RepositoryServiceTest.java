package pl.bodzioch.damian.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.bodzioch.damian.github.GitBranchModel;
import pl.bodzioch.damian.github.GitHubClient;
import pl.bodzioch.damian.github.GitRepositoryModel;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RepositoryServiceTest {

    private RepositoryService repositoryService;

    @Mock
    private GitHubClient gitHubClient;

    @BeforeEach
    void setUp() {
        repositoryService = new RepositoryServiceImpl(gitHubClient);
    }

    @Test
    public void getRepositoryDataShouldNotReturnForks() {
        String username = "testuser";
        String repo1Name = "repo1";
        String repo2Name = "repo1";
        List<GitRepositoryModel> repositoryModels = List.of(
                new GitRepositoryModel(repo1Name, username, false),
                new GitRepositoryModel(repo2Name, username, true)
        );
        List<GitBranchModel> branchModels = List.of(
                new GitBranchModel("branch1", "1"),
                new GitBranchModel("branch2", "2")
        );

        when(gitHubClient.getRepositories(username)).thenReturn(repositoryModels);
        when(gitHubClient.getBranches(eq(username), anyString())).thenReturn(branchModels);

        List<Repository> repositories = repositoryService.getRepositoryData(username);
        boolean notHaveForks = repositories.stream()
                .noneMatch(Repository::getFork);

        assertTrue(notHaveForks);
    }
}
