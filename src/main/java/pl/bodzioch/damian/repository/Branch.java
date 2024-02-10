package pl.bodzioch.damian.repository;

import lombok.Builder;
import lombok.Getter;
import pl.bodzioch.damian.github.GitBranchModel;

@Builder
@Getter
public class Branch {

    private String name;
    private String sha;

    protected static Branch of(GitBranchModel branchModel) {
        return Branch.builder()
                .name(branchModel.getName())
                .sha(branchModel.getSha())
                .build();
    }
}
