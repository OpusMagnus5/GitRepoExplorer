package pl.bodzioch.damian.repository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.bodzioch.damian.github.GitBranchModel;
import pl.bodzioch.damian.github.GitHubClient;
import pl.bodzioch.damian.github.GitRepositoryModel;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@AllArgsConstructor
@Slf4j
public class RepositoryServiceImpl implements RepositoryService {

    private final GitHubClient gitHubClient;

    public List<Repository> getRepositoryData(String username) throws ExecutionException, InterruptedException {
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            return executor.submit(() -> collectRepositoryData(username))
                    .get();
        }

    }

    private List<Repository> collectRepositoryData(String username) {
        List<GitRepositoryModel> repositories = gitHubClient.getRepositories(username).stream()
                .filter(GitRepositoryModel::isNotFork)
                .toList();
        Iterator<List<GitBranchModel>> branchesIterator = repositories.stream()
                .map(repo -> gitHubClient.getBranches(username, repo.getRepositoryName()))
                .toList()
                .iterator();

        return repositories.stream()
                .map(repo -> Repository.of(repo, branchesIterator.next()))
                .toList();
    }
}
