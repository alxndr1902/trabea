package com.bc46.trabea.parttimeemployee;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PartTimeEmployeeRepository extends JpaRepository<PartTimeEmployee, Integer> {
    Boolean existsByPersonalEmail(String personalEmail);

    Boolean existsByPersonalPhoneNumber(String phoneNumber);
}
