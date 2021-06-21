package kvz.zsu.control.services;

import kvz.zsu.control.models.User;
import kvz.zsu.control.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepo repo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = repo.findByName(username);

        if (user == null)
            throw new UsernameNotFoundException("User not found");

        return user;
    }

    public List<User> findAll(){
        return repo.findAll();
    }

    public User findById( Long id) {
        return repo.findById(id).get();
    }

    public void save(User user) {
        repo.save(user);
    }
}
