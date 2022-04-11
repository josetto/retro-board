package cl.josetto.retroboard.repository;

import cl.josetto.retroboard.model.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void findByUsernameShouldReturn1User() {
        //given
        User user = new User();
        user.setUsername("Joseto");
        user.setPassword("12345");
        user.setRole("USER");
        userRepository.save(user);

        //when
        User actual = userRepository.findByUsername("Joseto");

        //then
        Assertions.assertThat(actual).isEqualTo(user);

    }
}
