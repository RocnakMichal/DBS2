package cz.uhk.dbsproject.service;

import cz.uhk.dbsproject.entity.Log;
import cz.uhk.dbsproject.entity.MovieUser;
import cz.uhk.dbsproject.repository.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LogService {

    private final LogRepository logRepository;

    @Autowired
    public LogService(LogRepository logRepository) {
        this.logRepository = logRepository;
    }

    public void log(MovieUser user, String action) {
        Log log = new Log();
        log.setUser(user);
        log.setAction(action);
        log.setTimestamp(LocalDateTime.now());
        logRepository.save(log);
    }

    public List<Log> getAllLogs() {
        return logRepository.findAll();
    }
}