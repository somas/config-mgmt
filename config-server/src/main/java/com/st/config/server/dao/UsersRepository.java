package com.st.config.server.dao;

import com.st.config.server.bean.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<User, String> {
    User findByUsername(String username);
}
