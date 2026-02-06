package com.sopra_steria.jens_berckmoes.infra.repository;

import com.sopra_steria.jens_berckmoes.infra.entity.TokenEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface CrudTokenRepository extends CrudRepository<TokenEntity, String> {

    /**
     * Checks if any token exists with a value in the provided collection of values.
     * @param values the collection of token values to check for existence
     * @return true if at least one token exists with a value in the provided collection, false otherwise
     * */
    boolean existsByValueIn(final Collection<String> values);
}
