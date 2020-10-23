package com.example.Neo4JEmployee.EmployeeApp.Controller;

import com.example.Neo4JEmployee.EmployeeApp.DataAccess.EmployeeDao;
import com.example.Neo4JEmployee.EmployeeApp.Model.Employee;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * The rest controller for the API, maps urls to java methods.
 */
@RestController
public class EmployeeController {
  // the data access object for employees
  protected EmployeeDao employeeDao;


  /**
   * This method establishes a connection to the database taking path variables for the user
   * name and password for the server.
   *
   * @param user the username for the database server
   * @param password the password for the database server
   */
  @GetMapping("/databaseconnect/{user}/{password}")
  public void setDao(@PathVariable String user,
      @PathVariable String password){
    // establish connection to database, and store data access object for use in other methods
    employeeDao = new EmployeeDao( user, password);
  }

  /**
   * Create an employee in the database using path variables from the url. Takes a name and a string
   * creates an employee object and then writes the object to the database as a node.
   *
   * Returns a json with this information when a request is made to the url.
   *
   * @param name string, name of employee
   * @param id int, employee id
   * @return the employee added to the database as a java object
   */
  @PostMapping("/create/{name}/{id}")
  public Employee createEmployee(@PathVariable String name, @PathVariable int id){
    if(employeeDao == null){
      return null;
    }
    Employee newEmployee = new Employee(name, id);
    newEmployee = employeeDao.createEmployee(newEmployee);
    return newEmployee;
  }

  /**
   * Return a list of all employees in the database.
   *
   * @return a linked list of employee objects
   */
  @GetMapping("/getemployees")
  public List<Employee> getAllEmployees(){
    if(employeeDao == null){
      return null;
    }
    // makes request to database through dao, gets employees and returns them
    List<Employee> employees = employeeDao.getAllEmployees();
    return employees;
  }

  /**
   * Delete all employees in the database.
   */
  @DeleteMapping("/deleteemployees")
  public void deleteAllEmployees(){
    if(employeeDao == null){
      return;
    }
    // makes query via dao to delete
    employeeDao.deleteAllEmployees();
  }

  /**
   * A home url for the api, in case of starting in browser.
   *
   * @return string greeting to user
   */
  @GetMapping("/")
  public String home(){
    return "Hello!";
  }
}
