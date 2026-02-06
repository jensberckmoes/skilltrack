package com.sopra_steria.jens_berckmoes.infra.repository;

import com.sopra_steria.jens_berckmoes.infra.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface CrudUserRepository extends CrudRepository<UserEntity, String> {
    /**
     * Checks if any username exists with a username in the provided collection of usernames.
     * @param usernames the collection of usernames to check for existence
     * @return true if at least one username exists with a username in the provided collection, false otherwise
     * */
    boolean existsByUsernameIn(final Collection<String> usernames);
}
