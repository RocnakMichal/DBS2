package cz.uhk.dbsproject.service;

import cz.uhk.dbsproject.entity.Genre;
import cz.uhk.dbsproject.repository.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GenreService {

    private final GenreRepository genreRepository;

    @Autowired
    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    public List<Genre> getAllGenres() {
        return genreRepository.findAll();
    }

    public Genre getGenreById(int id) {
        return genreRepository.findById(id).orElse(null);
    }

    public Optional<Genre> findByName(String name) {
        return genreRepository.findByNameIgnoreCase(name);
    }

    public Genre save(Genre genre) {
        return genreRepository.save(genre);
    }
}
