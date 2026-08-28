package com.newsnowbackend.repository;

import com.newsnowbackend.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<User> findByProviderAndProviderId(String provider, String providerId);

    /** Recipients for a breaking-news push: opted in and have a registered device token. */
    List<User> findByNotificationsEnabledTrueAndFcmTokenIsNotNull();
}
