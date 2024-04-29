package com.example.EmployeeManagementSystem.Controller;

import com.example.EmployeeManagementSystem.Model.Employee;
import com.example.EmployeeManagementSystem.Service.EmployeeService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.slf4j.Logger;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
public class EmployeeController {

    private Logger log = LoggerFactory.getLogger(Employee.class);

    @Autowired
    private EmployeeService employeeService;

    @RequestMapping(value = "api/v1/json/employee/saveEmployee", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> saveEmployee(@RequestBody Map<String,Object> params) throws MethodArgumentNotValidException {

//        log the request data
        log.info("saveEmployee : Request received : " + params);

//        Create the new employee object and save it to the database
        Employee new_employee = employeeService.saveEmployee(params);
//        Return the response object
        return ResponseEntity.status(HttpStatus.CREATED).body(new_employee);
    }

    @RequestMapping(value = "api/v1/json/employee/updateEmployee/{id}", method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> updateEmployee(@PathVariable("id") long id, @RequestBody Map<String,Object> params) throws MethodArgumentNotValidException {

//        log the request data
        log.info("updateEmployee : Request Received : {} {}" ,id,params);

//        Attempt updating of employee parameters
        Employee update_employee = employeeService.updateEmployee(id,params);

//        Return the appropriate HTTP Response based on the result
        if(update_employee == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No such Employee with id : " + id);
        }

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(update_employee);

    }

    @RequestMapping(value = "api/v1/json/employee/findAllEmployees", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> findAllEmployees(@PageableDefault Pageable pageable) throws MethodArgumentNotValidException{

//        log the request data
        log.info("findAllEmployees : Request Received : "+ pageable);

//        Find all employees in the given page with given page size
        Page<Employee> employeePage = employeeService.findAllEmployees(pageable);

//        Return the response object with appropriate HTTP status
        if(employeePage.getNumberOfElements() == 0){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(employeePage);
        }

        return ResponseEntity.status(HttpStatus.OK).body(employeePage);

    }

    @RequestMapping(value = "api/v1/json/employee/findEmployee/{id}", method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> findEmployee(@PathVariable("id") long id) throws MethodArgumentNotValidException{
//        log the request data
        log.info("findEmployee : Request Received : {}",id);

//      find the employee
        Employee employee = employeeService.findByEmpId(id);

//        Return Appropriate HTTP Response
        if(employee == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No such Employee with id : " + id);
        }

        return ResponseEntity.status(HttpStatus.OK).body(employee);
    }

    @RequestMapping(value = "api/v1/json/employee/deleteEmployee/{id}", method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> deleteEmployee(@PathVariable("id") long id) throws MethodArgumentNotValidException{
//        log the request data
        log.info("deleteEmployee : Request Received : " + id);

//        Attempt deletion of the employee
        Boolean deletionStatus = employeeService.deleteEmployee(id);

//        Return Appropriate HTTP Response
        if(deletionStatus){
            return ResponseEntity.status(HttpStatus.OK).body("Employee with id :"+id+" has been deleted");
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No employee with id : "+id+ " was found");

    }

    @RequestMapping(value = "api/v1/json/employee/deleteAllEmployees", method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> deleteAllEmployees(){
//        log the request
        log.info("deleteAllEmployees : Request Received");

//      Delete All Employee records
        employeeService.deleteAllEmployees();
        return ResponseEntity.status(HttpStatus.OK).body("All Employee records have been deleted");
    }
}
