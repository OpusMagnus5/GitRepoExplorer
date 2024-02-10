package pl.bodzioch.damian.github;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Map;

@Getter
@Setter
class GetBranchResponse implements Serializable {

    private String name;
    private String lastCommitSha;

    @JsonProperty("commit")
    private void unpackCommit(Map<String, Object> commit) {
        this.lastCommitSha = (String) commit.get("sha");
    }
}
