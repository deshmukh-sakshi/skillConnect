package com.skillconnect.backend.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.skillconnect.backend.Entity.Freelancer;
import org.springframework.stereotype.Repository;

@Repository
public interface FreelancerRepository extends JpaRepository<Freelancer, Long> {

}
