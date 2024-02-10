package pl.bodzioch.damian.repository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.bodzioch.damian.github.GitBranchModel;
import pl.bodzioch.damian.github.GitHubClient;
import pl.bodzioch.damian.github.GitRepositoryModel;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
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
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            List<GitRepositoryModel> repositories = gitHubClient.getRepositories(username).stream()
                    .filter(GitRepositoryModel::isNotFork)
                    .toList();
            List<CompletableFuture<List<GitBranchModel>>> branchFutures = repositories.stream()
                    .map(repo -> CompletableFuture.supplyAsync(() -> gitHubClient.getBranches(username, repo.getRepositoryName()), executor))
                    .toList();

            Iterator<List<GitBranchModel>> branchesIterator = branchFutures.stream()
                    .map(CompletableFuture::join)
                    .toList()
                    .iterator();

            return repositories.stream()
                    .map(repo -> Repository.of(repo, branchesIterator.next()))
                    .toList();
        }
    }
}
