package pl.bodzioch.damian.repository;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Builder
@Getter
@Setter
class BranchView implements Serializable {

    private String name;
    private String lastCommitSha;

    protected static BranchView of(Branch branch) {
        return BranchView.builder()
                .name(branch.getName())
                .lastCommitSha(branch.getSha())
                .build();
    }
}
