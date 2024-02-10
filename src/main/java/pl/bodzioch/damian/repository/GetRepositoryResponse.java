package pl.bodzioch.damian.repository;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Builder
class GetRepositoryResponse implements Serializable {

    private List<RepositoryView> repositories;
}
