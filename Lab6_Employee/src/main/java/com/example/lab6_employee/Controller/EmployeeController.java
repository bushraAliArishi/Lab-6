package com.example.lab6_employee.Controller;

import com.example.lab6_employee.ApiResponse.ApiResponse;
import com.example.lab6_employee.Model.Employee;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/employee")
public class EmployeeController {
     ArrayList<Employee> employees = new ArrayList<>();

    @GetMapping("/display")
    public ResponseEntity getAllEmployees() {
        return ResponseEntity.ok(employees);
    }

    @PostMapping("/add")
    public ResponseEntity addEmployee(@RequestBody @Valid Employee employee, Errors errors) {
        if (errors.hasErrors()) {
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(message));
        }
        employees.add(employee);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Employee added successfully"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity updateEmployee(@PathVariable String id, @RequestBody @Valid Employee employee, Errors errors) {
        if (errors.hasErrors()) {
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
        }
        for (int i = 0; i < employees.size(); i++) {
            if (employees.get(i).getId().equals(id)) {
                employees.set(i, employee);
                return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Employee updated successfully"));
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Employee not found"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteEmployee(@PathVariable String id) {
        for (int i = 0; i < employees.size(); i++) {
            if (employees.get(i).getId().equals(id)) {
                employees.remove(i);
                return ResponseEntity.ok(new ApiResponse("Employee deleted successfully"));
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Employee not found"));
    }

    @GetMapping("/roll/{position}")
    public ResponseEntity employeePosition(@PathVariable String position) {
        ArrayList<Employee> rollFilter = new ArrayList<>();
        for (Employee employee : employees) {
            if (employee.getPosition().equals(position)) {
                rollFilter.add(employee);
            }

        }
        return ResponseEntity.status(HttpStatus.OK).body(rollFilter);
    }

    @GetMapping("/agefilter/{minAge}/{maxAge}")
    public ResponseEntity employeePosition(@PathVariable int minAge, @PathVariable int maxAge) {
        ArrayList<Employee> ageFilter = new ArrayList<>();
        for (Employee employee : employees) {
            if (employee.getAge() >= minAge && employee.getAge() <= maxAge) {
                ageFilter.add(employee);
            }

        }
        return ResponseEntity.status(HttpStatus.OK).body(ageFilter);
    }

    @PutMapping("/applyleave/{id}")
    public ResponseEntity<ApiResponse> applyLeave(@PathVariable String id ) {
        Employee leaveEmp = null;

        for (int i = 0; i < employees.size(); i++) {
            if (employees.get(i).getId().equals(id)) {
                leaveEmp = employees.get(i);
                break;
            }
        }
        if (leaveEmp != null) {
            if (!leaveEmp.isOnLeave() && leaveEmp.getAnnualLeave() >= 1) {
                leaveEmp.setOnLeave(true);
                leaveEmp.setAnnualLeave(leaveEmp.getAnnualLeave() - 1);
                return ResponseEntity.ok(new ApiResponse("Leave request approved"));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("Leave request rejected"));
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Employee not found"));
    }

    @GetMapping("/noleave")
    public ResponseEntity noLeaveEmployee() {
        ArrayList<Employee> noLeaveEmployee = new ArrayList<>();
        for (Employee employee : employees) {
            if (employee.getAnnualLeave() == 0) {
                noLeaveEmployee.add(employee);
            }
        }
        if (noLeaveEmployee.size()>0){
        return ResponseEntity.status(HttpStatus.OK).body(noLeaveEmployee);}
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("not found"));
    }

    @PutMapping("/promote/{superID}/{employeeID}")
    public ResponseEntity<ApiResponse> promoteEmployee(@PathVariable String superID, @PathVariable String employeeID) {
        Employee supervisor = null;
        Employee promotedEmployee = null;

        for (Employee emp : employees) {
            if (emp.getId().equals(superID)) {
                if (emp.getPosition().equalsIgnoreCase("supervisor")) {
                    supervisor = emp;
                    break;
                }
            }
        }

        if (supervisor == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("Supervisor not found"));
        }

        for (Employee emp : employees) {
            if (emp.getId().equals(employeeID)) {
                promotedEmployee = emp;
                break;
            }
        }

        if (promotedEmployee == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Employee not found"));
        }

        if (promotedEmployee.getAge() >= 30 && !promotedEmployee.isOnLeave()) {
            promotedEmployee.setPosition("supervisor");
            return ResponseEntity.ok(new ApiResponse("Employee promoted to supervisor"));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("Employee does not meet criteria for promotion"));
        }
    }
}
