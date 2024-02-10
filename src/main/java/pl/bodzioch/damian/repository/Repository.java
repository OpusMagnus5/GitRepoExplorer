package pl.bodzioch.damian.repository;

import lombok.Builder;
import lombok.Getter;
import pl.bodzioch.damian.github.GitBranchModel;
import pl.bodzioch.damian.github.GitRepositoryModel;

import java.util.List;

@Builder
@Getter
public class Repository {

    private String name;
    private String ownerLogin;
    private List<Branch> branches;

    protected static Repository of(GitRepositoryModel repositoryModel, List<GitBranchModel> branchModel) {
        return Repository.builder()
                .name(repositoryModel.getRepositoryName())
                .ownerLogin(repositoryModel.getOwnerLogin())
                .branches(branchModel.stream()
                        .map(Branch::of)
                        .toList())
                .build();
    }
 }
