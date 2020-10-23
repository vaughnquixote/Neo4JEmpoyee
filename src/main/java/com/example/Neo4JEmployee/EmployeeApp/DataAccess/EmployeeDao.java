package com.example.Neo4JEmployee.EmployeeApp.DataAccess;

import static org.neo4j.driver.Values.parameters;

import com.example.Neo4JEmployee.EmployeeApp.Model.Employee;
import java.util.LinkedList;
import java.util.List;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.Transaction;
import org.neo4j.driver.TransactionWork;


/**
 * This class represents the data access layer for employees. It establishes a connection to the
 */
public class EmployeeDao {
  // the database driver, using a bolt driver for Neo4j as specified
  Driver driver;

  /**
   * Constructor for the Data access object.
   *
   * Establish the connection to the database using the Driver object provided by the java driver
   * for Neo4J. This will connect to the default database.
   *
   * @param user
   * @param password
   */
  public EmployeeDao( String user, String password){
    driver = GraphDatabase.driver("bolt://localhost:7687", AuthTokens.basic(user, password));
  }

  /**
   * A method to create an employee in the database.
   *
   * @param employee
   * @return
   */
  public Employee createEmployee(Employee employee){

    // wrap database transaction in a session per recommendation on Neo4j docs
    try(Session session = driver.session()){

      // write a create transaction to the database for the employee
      String output = session.writeTransaction( new TransactionWork<String>()
      {
        @Override
        public String execute(Transaction transaction) {
          // create an employee in the database with a name and id
          Result result = transaction.run("CREATE (a:Employee) "
              + "SET a.name = $name, a.id = $id" + " RETURN a.name"
              , parameters("name", employee.getName(), "id",
              employee.getEmployeeID()));
          return result.single().get(0).asString();
        }
      });
      // return the employee object if successful, primarily for testing purposes
      return employee;
    } catch (Exception e) {
      // catch exception return null object
      return null;
    }
  }

  /**
   * Return a list of all of the employees in the database.
   *
   * makes a query to the database to get all employees
   *
   * @return a list of employees, held in their POJOs for the database
   */
  public List<Employee> getAllEmployees(){
    // wrap transaction in a session
    try (Session session = driver.session()) {

      // make transaction to the database
      // in this case, just making a request for all nodes in the database since the
      // database is only anticipated to store employee nodes
      List<Employee> output = session.writeTransaction(new TransactionWork<List<Employee>>() {
        @Override
        public List<Employee> execute(Transaction transaction) {
          List<Employee> employees = new LinkedList<>();
          Result result = transaction.run("MATCH (n) RETURN n");

          // iterate through the employees and build a list of java objects to represent them
          int counter = 0;
          while (result.hasNext()) {
            Record next = result.next();
            employees.add(
                new Employee(next.get(0).get("name").asString(), next.get(0).get("id").asInt()));
            counter++;
          }
          return employees;
        }
      });
      // return the needed list
      return output;
    } catch (Exception e) {
      return null;
    }
  }

  /**
   * Delete all the emloyees from the database.
   *
   * @return string result indicating success or failure for testing purposes, would normally use
   * an exception in java
   */
  public String deleteAllEmployees(){
    // wrapping transaction in a session
    try (Session session = driver.session()){
      // write transaction for delete for all nodes in the database
      String output = session.writeTransaction(new TransactionWork<String>() {
        @Override
        public String execute(Transaction transaction) {
          transaction.run("MATCH (n) DELETE n");
          return "success";
        }
      });
      return output;
    } catch (Exception e){
      return "failure";
    }
  }
}
