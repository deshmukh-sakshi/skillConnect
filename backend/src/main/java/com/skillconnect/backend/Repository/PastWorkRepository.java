package com.skillconnect.backend.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.skillconnect.backend.Entity.PastWork;
import org.springframework.stereotype.Repository;

@Repository
public interface PastWorkRepository extends JpaRepository<PastWork, Long>  {

}
