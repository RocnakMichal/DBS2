package cz.uhk.dbsproject.service;

import cz.uhk.dbsproject.entity.Movie;
import cz.uhk.dbsproject.entity.MovieUser;
import cz.uhk.dbsproject.entity.Recommendation;
import cz.uhk.dbsproject.entity.UserGroup;
import cz.uhk.dbsproject.entity.GroupMember;
import cz.uhk.dbsproject.repository.GroupMemberRepository;
import cz.uhk.dbsproject.repository.MovieRepository;
import cz.uhk.dbsproject.repository.RecommendationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecommendationService {

    @Autowired
    private RecommendationRepository recommendationRepository;
    @Autowired
    private StatisticsService statisticsService;
    @Autowired
    private GroupMemberRepository groupMemberRepository;
    @Autowired
    private MovieRepository movieRepository;

    public void recommendMovie(MovieUser user, Movie movie) {
        Recommendation recommendation = new Recommendation();
        recommendation.setMovieUser(user);
        recommendation.setRecommendedMovie(movie);
        recommendation.setCreatedAt(LocalDateTime.now());
        recommendationRepository.save(recommendation);
    }

    public List<Recommendation> getByUser(MovieUser user) {
        return recommendationRepository.findByMovieUser(user);
    }

    public List<Recommendation> getAll() {
        return recommendationRepository.findAll();
    }

    public void toggleRecommendation(Movie movie, MovieUser user) {
        Optional<Recommendation> existing = recommendationRepository.findByRecommendedMovieAndMovieUser(movie, user);

        if (existing.isPresent()) {
            recommendationRepository.delete(existing.get());
        } else {
            Recommendation recommendation = new Recommendation();
            recommendation.setRecommendedMovie(movie);
            recommendation.setMovieUser(user);
            recommendation.setCreatedAt(LocalDateTime.now());
            recommendationRepository.save(recommendation);
        }

        statisticsService.updateStats(movie);
    }

    public Movie getRecommendedMovieForGroup(UserGroup group) {
        List<GroupMember> members = groupMemberRepository.findByGroup(group);
        if (members.isEmpty()) return null;

        List<MovieUser> users = members.stream()
                .map(GroupMember::getUser)
                .toList();

        // Get recommendations per user
        List<List<Movie>> userRecommendations = users.stream()
                .map(user -> recommendationRepository.findByMovieUser(user).stream()
                        .map(Recommendation::getRecommendedMovie)
                        .toList())
                .toList();

        // Find intersection
        Set<Movie> common = new HashSet<>(userRecommendations.get(0));
        for (int i = 1; i < userRecommendations.size(); i++) {
            common.retainAll(userRecommendations.get(i));
        }

        if (!common.isEmpty()) {
            return common.stream()
                    .skip(new Random().nextInt(common.size()))
                    .findFirst()
                    .orElse(null);
        }

        // Fallback to most recommended
        Map<Movie, Long> recommendationCounts = userRecommendations.stream()
                .flatMap(List::stream)
                .collect(Collectors.groupingBy(movie -> movie, Collectors.counting()));

        if (!recommendationCounts.isEmpty()) {
            long max = recommendationCounts.values().stream().mapToLong(Long::longValue).max().orElse(0);
            List<Movie> top = recommendationCounts.entrySet().stream()
                    .filter(entry -> entry.getValue() == max)
                    .map(Map.Entry::getKey)
                    .toList();

            return top.get(new Random().nextInt(top.size()));
        }

        // Final fallback - random movie
        List<Movie> all = movieRepository.findAll();
        if (!all.isEmpty()) {
            return all.get(new Random().nextInt(all.size()));
        }

        return null; // if literally no movie exists in DB
    }

    public Movie getRandomMovie(List<Movie> pool) {
        if (pool == null || pool.isEmpty()) return null;
        Collections.shuffle(pool);
        return pool.get(0);
    }
}