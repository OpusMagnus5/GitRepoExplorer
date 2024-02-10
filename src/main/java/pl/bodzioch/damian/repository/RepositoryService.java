package pl.bodzioch.damian.repository;

import java.util.List;

public interface RepositoryService {
    List<Repository> getRepositoryData(String username);
}
