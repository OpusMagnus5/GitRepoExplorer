package pl.bodzioch.damian.repository;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Builder
@Getter
@Setter
class RepositoryView implements Serializable {

    private String ownerName;
    private String name;
    private List<BranchView> branches;

    protected static RepositoryView of(Repository repository) {
        return RepositoryView.builder()
                .ownerName(repository.getOwnerLogin())
                .name(repository.getName())
                .branches(repository.getBranches().stream()
                        .map(BranchView::of)
                        .toList())
                .build();
    }
}
