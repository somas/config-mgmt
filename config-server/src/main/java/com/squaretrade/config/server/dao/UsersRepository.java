package com.squaretrade.config.server.dao;

import com.squaretrade.config.server.bean.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<Users, String> {
    Users findByUsername(String username);
}
