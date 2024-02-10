package pl.bodzioch.damian.github;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class GitBranchModel {

    private String name;
    private String sha;

    protected static GitBranchModel of(GetBranchResponse branch) {
        return new GitBranchModel(branch.getName(), branch.getLastCommitSha());
    }
}
