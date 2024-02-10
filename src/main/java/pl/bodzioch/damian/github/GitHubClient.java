package pl.bodzioch.damian.github;

import java.util.List;

public interface GitHubClient {

    List<GitRepositoryModel> getRepositories(String username);
    List<GitBranchModel> getBranches(String username, String repositoryName);
}
