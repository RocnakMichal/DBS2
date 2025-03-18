package cz.uhk.dbsproject.service;

import cz.uhk.dbsproject.entity.MovieUser;
import cz.uhk.dbsproject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public MovieUser createUser(MovieUser movieUser) {
        return userRepository.save(movieUser);
    }

    public MovieUser getUser(int id) {
        return userRepository.findById(id).orElse(null);
    }

    public List<MovieUser> getAllUsers() {
        return userRepository.findAll();
    }

    public MovieUser getUserByName(String name) {
        return userRepository.findByEmail(name);
    }

    public MovieUser getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public MovieUser updateUser(int id, MovieUser updatedMovieUser) {
        MovieUser existingMovieUser = userRepository.findById(id).orElse(null);
        if (existingMovieUser == null) {
            return null;
        }
        existingMovieUser.setName(updatedMovieUser.getName());
        existingMovieUser.setEmail(updatedMovieUser.getEmail());
        existingMovieUser.setPasswordHash(updatedMovieUser.getPasswordHash());
        existingMovieUser.setRole(updatedMovieUser.getRole());
        return userRepository.save(existingMovieUser);
    }

    public boolean deleteUser(int id) {
        if (!userRepository.existsById(id)) {
            return false;
        }
        userRepository.deleteById(id);
        return true;
    }
}
