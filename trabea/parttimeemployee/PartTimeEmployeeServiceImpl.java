package com.bc46.trabea.parttimeemployee;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PartTimeEmployeeServiceImpl implements PartTimeEmployeeService{
    private final PartTimeEmployeeRepository partTimeEmployeeRepository;

    @Override
    public boolean existsByPersonalEmail(String email) {
        return !partTimeEmployeeRepository.existsByPersonalEmail(email);
    }

    @Override
    public boolean existsByPersonalPhoneNumber(String personalPhoneNumber) {
        return !partTimeEmployeeRepository.existsByPersonalPhoneNumber(personalPhoneNumber);
    }
}
