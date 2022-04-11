package cl.josetto.retroboard.service;

import cl.josetto.retroboard.model.User;
import cl.josetto.retroboard.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class UserServiceTest {

    @MockBean
    private UserRepository userRepository;

    private UserService userService;

    @BeforeEach
    public void init() {
        this.userService = new UserService(userRepository);
    }

    @Test
    public void loadUserByUsernameShouldReturnTheSameUser() {
        //given
        User user = new User();
        user.setUsername("Joseto");
        user.setPassword("12345");
        user.setRole("USER");
        Mockito.when(userRepository.findByUsername("Joseto")).thenReturn(user);

        //when
        UserDetails actual = userService.loadUserByUsername("Joseto");

        Mockito.verify(userRepository, Mockito.times(1)).findByUsername("Joseto");
        Assertions.assertThat(actual.getUsername()).isEqualTo(user.getUsername());
        Assertions.assertThat(actual.getPassword()).isEqualTo(user.getPassword());

    }
}
