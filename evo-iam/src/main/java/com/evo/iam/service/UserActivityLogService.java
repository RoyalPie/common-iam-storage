package com.evo.iam.service;


import com.evo.iam.entity.User;
import com.evo.iam.entity.UserActivityLog;
import com.evo.iam.repository.UserActivityLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserActivityLogService {

    @Autowired
    private UserActivityLogRepository userActivityLogRepository;

    public void logActivity(User user, String action, String ipAddress, String userAgent) {
        UserActivityLog log = new UserActivityLog(user, action, ipAddress, userAgent);
        userActivityLogRepository.save(log);
    }
}
