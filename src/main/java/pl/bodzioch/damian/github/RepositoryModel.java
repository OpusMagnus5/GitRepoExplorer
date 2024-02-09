package pl.bodzioch.damian.github;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class RepositoryModel {

    private String repositoryName;
    private String ownerLogin;
}
