package com.example.Neo4JEmployee.EmployeeApp.Model;

/**
 * This class represents the data model for employees in the database. An employee has a name and
 * an employee id.
 */
public class Employee {
  private int employeeID;
  private String name;

  /**
   * The constructor for the class.
   *
   * @param name a string, the name of the employee
   * @param id an int, the employee id
   */
  public Employee(String name, int id){
    this.name = name;
    this.employeeID = id;
  }

  /**
   * Simply return the employee's name
   *
   * @return a string, the name
   */
  public String getName(){
    return this.name;
  }

  /**
   * Simply return the employee's id
   *
   * @return an int, the id
   */
  public int getEmployeeID(){
    return this.employeeID;
  }

  /**
   * Provide a string representation of the employee as:
   *     name: $name id: $id
   *
   * @return the string representation
   */
  @Override
  public String toString(){
    String id = Integer.toString(this.employeeID);
    String employeeString = "name: " + this.name + " id: " + id;
    return employeeString;
  }

}
