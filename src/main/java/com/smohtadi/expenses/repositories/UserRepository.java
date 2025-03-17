package com.smohtadi.expenses.repositories;

import com.smohtadi.expenses.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    public User findByUid(String uid);
}
