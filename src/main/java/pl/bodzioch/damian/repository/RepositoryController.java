package pl.bodzioch.damian.repository;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@AllArgsConstructor
@RequestMapping("/repository")
public class RepositoryController {

    private final RepositoryService repositoryService;

    @GetMapping("/{username}")
    public ResponseEntity<GetRepositoryResponse> getRepositories(@PathVariable("username") String username) throws ExecutionException, InterruptedException {
        List<Repository> repositories = repositoryService.getRepositoryData(username);
        List<RepositoryView> repositoryViews = repositories.stream()
                .map(RepositoryView::of)
                .toList();
        return ResponseEntity.ok(GetRepositoryResponse.builder()
                .repositories(repositoryViews)
                .build());
    }
}
