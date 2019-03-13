package com.alexjamesmalcolm.dishbot;

import com.alexjamesmalcolm.dishbot.physical.Account;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.Optional;

public interface AccountRepository extends CrudRepository<Account, Long> {
    Optional<Account> findByUserId(Long userId);

    @Query("select a from Account a WHERE :groupId in elements(a.groupIds)")
    Collection<Account> findAllByGroupId(@Param("groupId") Long groupId);

    @Query("select a from Account a where :groupId in elements(a.groupIds)")
    Optional<Account> findByGroupId(@Param("groupId") Long groupId);

    Collection<Account> findAll();
}
