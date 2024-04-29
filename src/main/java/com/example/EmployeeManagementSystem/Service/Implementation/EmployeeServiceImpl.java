package com.example.EmployeeManagementSystem.Service.Implementation;

import com.example.EmployeeManagementSystem.Model.Employee;
import com.example.EmployeeManagementSystem.Repository.EmployeeRepository;
import com.example.EmployeeManagementSystem.Service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class EmployeeServiceImpl implements EmployeeService {


    private Logger log = LoggerFactory.getLogger(Employee.class);
    @Autowired
    private EmployeeRepository employeeRepository;


    @Override
    public Employee saveEmployee(Map<String,Object> params){
//        Creates new employee object from params
        Employee new_employee = null;

//        Check if there is id parameter present in params Map
        if(params.containsKey("id") && params.get("id")!= null ) {

            log.info("Id parameter type:" + params.get("id").getClass());

//            Check if the employee already exists
            new_employee = findByEmpId(((Integer)params.get("id")).longValue());

//            If employee exists then log it and return an empty employee
            if (new_employee != null) {
                log.info("saveEmployee -> Employee with id : {} already exists",params.get("id"));
                return new Employee();
            }
            else{
                new_employee = new Employee(
                        ((Integer) params.get("id")).longValue(),
                        (String) params.get("name"), (String) params.get("email"),
                        (String) params.get("position"), (String) params.get("department"),
                        (Integer) params.get("salary")
                );
//        Save the newly created employee using the repository
                Employee saved_employee = employeeRepository.save(new_employee);
//        return the saved employee
                return saved_employee;
            }

        }
//        If employee does not exist, create a new employee with the provided parameters
        new_employee = new Employee(
                (String) params.get("name"), (String) params.get("email"),
                (String) params.get("position"), (String) params.get("department"),
                (Integer) params.get("salary")
        );
//        Save the newly created employee using the repository
        Employee saved_employee = employeeRepository.save(new_employee);
//        return the saved employee
        return saved_employee;
    }

    @Override
    public Employee findByEmpId(Long id) {
        Employee employee = employeeRepository.findByid(id);
        if(employee == null){
            log.info("findByEmpId -> No employee with the id : {} found",id);
        }
        return employee;
    }

    @Override
    public Page<Employee> findAllEmployees(Pageable pageable) {
        log.info("Pageable object :" + pageable);

//        Find all the employees in the specified page and size
        Page<Employee> employeePage = employeeRepository.findAll(pageable);

//        if there are no records on the specified page then log it as a message
        if(employeePage.getNumberOfElements() == 0){
            log.info("No employees found on the specified page : {}",pageable.getPageNumber());
        }
        return employeePage;
    }

    @Override
    public Boolean deleteEmployee(Long id) {
//        Check if the employee with the specified id exists
        Employee employee = findByEmpId(id);

//        If there are no employees with the specified id, log it as a message and return
        if (employee == null){
            log.info("deleteEmployee -> Employee does not exist");
            return Boolean.FALSE;
        }

//      Delete the employee that was found.
        employeeRepository.delete(employee);

//        log the message
        log.info("deleteEmployee -> Employee with id : {} has been deleted",id);
        return Boolean.TRUE;
    }

    @Override
    public void deleteAllEmployees() {
//        delete all Employee records
        employeeRepository.deleteAll();
    }

    @Override
    public Employee updateEmployee(Long id, Map<String, Object> params) {

//      Check if the employee with the given id exists
        Employee employee = findByEmpId(id);

//      If employee is present update params as received from the request and return the update employee object
        if(employee != null){
//            if(params.containsKey("id") && params.get("id") != null){
//                employee.setId(((Integer)params.get("id")).longValue());
//            }
            if(params.containsKey("name") && params.get("name") != null){
                employee.setName(params.get("name").toString());
            }
            if(params.containsKey("email") && params.get("email") != null){
                employee.setEmail(params.get("email").toString());
            }
            if(params.containsKey("position") && params.get("position") != null){
                employee.setPosition(params.get("position").toString());
            }
            if(params.containsKey("department") && params.get("department") != null){
                employee.setDepartment(params.get("department").toString());
            }
            if(params.containsKey("salary") && params.get("salary") != null){
                employee.setSalary((Integer) params.get("salary"));
            }

            employeeRepository.save(employee);

            return employee;
        }

//        Else log the message and return the null employee object
        else{
            log.info("updateEmployee -> No employee with id : {} exists",id);
            return employee;
        }
    }
}
