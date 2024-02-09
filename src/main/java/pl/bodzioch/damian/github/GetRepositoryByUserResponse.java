package pl.bodzioch.damian.github;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class GetRepositoryByUserResponse implements Serializable {

    @JsonProperty("total_count")
    private Long totalCount;

    @JsonProperty("items")
    private List<Repository> repositories = new ArrayList<>();
}
