package pl.bodzioch.damian.github;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class Repository implements Serializable {

    private String name;
    private Owner owner;
}
