package com.skillconnect.backend.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.skillconnect.backend.Entity.Skills;
import org.springframework.stereotype.Repository;

@Repository
public interface SkillRepository extends JpaRepository<Skills, Long> {

}
