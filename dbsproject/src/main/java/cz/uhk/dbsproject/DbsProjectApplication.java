package cz.uhk.dbsproject;

import cz.uhk.dbsproject.entity.MovieUser;
import cz.uhk.dbsproject.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class DbsProjectApplication {

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public static void main(String[] args) {
        SpringApplication.run(DbsProjectApplication.class, args);
    }

    @Bean
    public CommandLineRunner init(UserRepository repo) {
        String adminMail = "admin@test.com";
        return args -> {
            if (repo.findByEmail(adminMail) == null) {
                MovieUser admin = new MovieUser();
                admin.setName("admin");
                admin.setEmail(adminMail);
                admin.setPasswordHash(passwordEncoder.encode("password"));
                admin.setRole("ADMIN");
                repo.save(admin);
            }
        };
    }
}
