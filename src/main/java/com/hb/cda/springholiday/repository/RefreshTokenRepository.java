package com.hb.cda.springholiday.repository;

import com.hb.cda.springholiday.entity.RefreshToken;
import com.hb.cda.springholiday.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String>{
    @Modifying
    @Query(value = "delete from RefreshToken rt where rt.expiresAt < current_date")
    void deleteExpiredToken();

    @Modifying
    @Query(value = "delete from RefreshToken rt where rt.user = :user")
    void deleteByUser(User user);
}
