package cl.josetto.retroboard.service;

import cl.josetto.retroboard.model.User;
import cl.josetto.retroboard.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Service
public class UserService implements UserDetailsService {

    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User does not exist!");
        }
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(), user.getPassword(), Arrays.asList(new SimpleGrantedAuthority(user.getRole())));
    }

    @Transactional(rollbackFor = Exception.class)
    public User saveUser(User user) {
        return userRepository.save(user);
    }
}
