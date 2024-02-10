package pl.bodzioch.damian.repository;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface RepositoryService {
    List<Repository> getRepositoryData(String username) throws ExecutionException, InterruptedException;
}
