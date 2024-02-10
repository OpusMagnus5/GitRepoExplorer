package pl.bodzioch.damian.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pl.bodzioch.damian.App;

import java.util.Locale;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pl.bodzioch.damian.repository.RepositoryController.REPOSITORY_CONTROLLER_PATH;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = App.class)
@AutoConfigureMockMvc
public class RepositoryControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private MessageSource messageSource;

    @Test
    public void testRepositoryControllerForExistingUser() throws Exception {
        String username = "OpusMagnus5";

        mvc.perform(get(REPOSITORY_CONTROLLER_PATH + "/" + username).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.repositories.length()").value(not(0)));
    }

    @Test
    public void testRepositoryControllerNotExistingUser() throws Exception {
        String username = "asgtrhgrsthrgfs";

        mvc.perform(get(REPOSITORY_CONTROLLER_PATH + "/" + username).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(is(HttpStatus.NOT_FOUND.value())))
                .andExpect(jsonPath("$.message").value(is(messageSource.getMessage("github.GitHubClient.userNotFound", null, Locale.ENGLISH))));
    }
}
