package pl.bodzioch.damian.github;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class GitRepositoryModel {

    private String repositoryName;
    private String ownerLogin;
    private Boolean fork;

    protected static GitRepositoryModel of(RepositoryResponse repository) {
        return new GitRepositoryModel(repository.getName(), repository.getOwnerLogin(), repository.getFork());
    }

    public boolean isNotFork() {
        return !this.fork;
    }
}
