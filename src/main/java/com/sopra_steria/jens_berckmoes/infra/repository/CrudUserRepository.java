package com.sopra_steria.jens_berckmoes.infra.repository;

import com.sopra_steria.jens_berckmoes.infra.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@SuppressWarnings("unused")
@Repository
public interface CrudUserRepository extends CrudRepository<UserEntity, String> {
}
