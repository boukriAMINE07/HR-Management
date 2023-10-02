package com.example.gestionemployes.web;

import com.example.gestionemployes.entities.Employee;
import com.example.gestionemployes.exception.IllegalArgumentException;
import com.example.gestionemployes.services.EmployeeService;
import com.example.gestionemployes.services.KeycloakService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/api/employees")
public class EmployeeController {
    private final EmployeeService employeeService;
    private final KeycloakService keycloakService;

    @Autowired
    public EmployeeController(EmployeeService employeeService, KeycloakService keycloakService){
        this.employeeService = employeeService;
        this.keycloakService = keycloakService;
    }

    @PreAuthorize("hasRole('client-admin')")
    @PostMapping
    public ResponseEntity<?> createEmployee( @RequestBody Employee employee)  {
        return ResponseEntity.status(HttpStatus.CREATED).body(employeeService.createEmployee(employee));
    }

    private String extractTokenFromAuthorizationHeader(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring("Bearer ".length());
        }
        throw new IllegalArgumentException("Le header Authorization est invalide");
    }

    @PreAuthorize("hasRole('client-admin')")
    @GetMapping
    public String token() {
        return keycloakService.getKeycloakAccessToken();
    }
    /*@GetMapping
    public ResponseEntity<PaginatedResponse<Employee>> getEmployeeNotDeleted(@RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "5") int size){
        Page<Employee> employeePage = employeeService.listEmployeeSupprimer(page,size);
        PaginatedResponse<Employee> response = new PaginatedResponse<>();
        response.setContent(employeePage.getContent());
        response.setPagenumber(employeePage.getNumber());
        response.setPage(employeePage.getSize());
        response.setTotalElements((int) employeePage.getTotalElements());
        response.setTotalPages(employeePage.getTotalPages());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{username}")
    public Employee getEmployeeByUsername(@PathVariable String username){
        return employeeService.getEmployeeByUsername(username);
    }
    @PutMapping("/{username}")
    public ResponseEntity<?> updateEmployee(@PathVariable String username, @RequestBody Employee employee){
        try{
            employee.setUsername(username);
            String Role = employee.getRole();

            Employee updateEmployee = employeeService.updateEmployee(username, employee);
            return ResponseEntity.ok(updateEmployee);
        }catch(NotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("L'employee n'a pas été trouvé.");
        }catch (ValidationException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Une erreur interne s'est produite.");
        }
    }
    @GetMapping("/chercher")
    public ResponseEntity<Page<Employee>> chercherEmployee(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ){
        Page<Employee> employees = employeeService.chercherEmployee(keyword, page, size);
        return ResponseEntity.ok(employees);
    }
    @PatchMapping("/{username}")
    public ResponseEntity<?> updateSupEmployee(@PathVariable String username, @RequestBody Employee employee){
        try{
            employee.setUsername(username);
            Employee employees = employeeService.cacherEmployee(employee);
            if(employee == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("L'employee avec l'ID spécifié n'existe pas.");
            }
            return ResponseEntity.ok(employees);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Une erreur interne s'est produite.");
        }

    }
    @ExceptionHandler(EmployeeNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleDepartementNotFoundException(EmployeeNotFoundException ex){
        return new ErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIllegalArgumentException(IllegalArgumentException ex){
        return new ErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }*/

}
