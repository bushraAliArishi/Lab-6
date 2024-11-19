package com.example.lab6_employee.Model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class Employee {

    @NotEmpty @Size(min = 3,message = "the id must be more than 2 ")
    private String id;

    @NotEmpty(message = "The name cannot be empty.") @Size(min = 5, message = "The name must be at least 5 characters long.")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "The name can only contain alphabetic characters.")
    private String name;

    @NotEmpty @Email
    private String email;

    @Min(value = 10,message = "phone number must be 10 digit") @Pattern(regexp = "^05\\d+$")
    private String phoneNumber;

    @NotNull @Min(value = 25,message = "the age must be older than 25 ")
    private int age;

    @NotEmpty @Pattern(regexp = "^(supervisor|coordinator)$", message = "The role must be either 'supervisor' or 'coordinator'.")
    private String position;

    private boolean onLeave=false;
    @NotNull @FutureOrPresent @JsonFormat(pattern ="yyyy-MM-dd")
    private LocalDate hireDate;

    @NotNull @PositiveOrZero
    private int annualLeave;


}
