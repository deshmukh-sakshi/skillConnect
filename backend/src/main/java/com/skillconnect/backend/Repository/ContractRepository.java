package com.skillconnect.backend.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.skillconnect.backend.Entity.Contract;

@Repository
public interface ContractRepository extends JpaRepository<Contract,Long>{
}
