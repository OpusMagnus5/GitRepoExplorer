package pl.bodzioch.damian.github;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Map;

@Getter
@Setter
class RepositoryResponse implements Serializable {

    private String name;
    private String ownerLogin;
    private Boolean fork;

    @JsonProperty("owner")
    private void unpackOwner(Map<String, Object> owner) {
        this.ownerLogin = (String) owner.get("login");

    }
}
