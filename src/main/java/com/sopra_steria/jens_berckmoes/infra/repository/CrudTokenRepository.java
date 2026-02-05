package com.sopra_steria.jens_berckmoes.infra.repository;

import com.sopra_steria.jens_berckmoes.infra.entity.TokenEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@SuppressWarnings("unused")
@Repository
public interface CrudTokenRepository extends CrudRepository<TokenEntity, String> {

}
