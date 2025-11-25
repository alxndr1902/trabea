package com.bc46.trabea.parttimeemployee;

public interface PartTimeEmployeeService {
    boolean existsByPersonalEmail(String email);
    boolean existsByPersonalPhoneNumber(String personalPhoneNumber);
}
