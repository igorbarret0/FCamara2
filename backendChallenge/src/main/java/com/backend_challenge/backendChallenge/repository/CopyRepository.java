package com.backend_challenge.backendChallenge.repository;

import com.backend_challenge.backendChallenge.entites.Copy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CopyRepository extends JpaRepository<Copy, Long> {

    Optional<Copy> findCopyByCopyCode(String copyCode);

}
