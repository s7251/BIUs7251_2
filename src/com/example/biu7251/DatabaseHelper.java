package com.example.biu7251;

import java.sql.SQLException;

import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.connection.JDBCConnectionPool;
import com.vaadin.data.util.sqlcontainer.connection.SimpleJDBCConnectionPool;
import com.vaadin.data.util.sqlcontainer.query.TableQuery;

public class DatabaseHelper {
    
	private JDBCConnectionPool pool=null;
	private SQLContainer personContainer = null;
	
    public DatabaseHelper() {
  	  initConnectionPool();
  	  initContainers();
  	  
    }

    private void initConnectionPool() {
      	try {
      		pool = new SimpleJDBCConnectionPool ("com.mysql.jdbc.Driver",
      				"jdbc:mysql://localhost:3306/biu", "root", "root", 2, 5);
      	} catch (SQLException e) {
        	  e.printStackTrace();
        }
    }
    
 
	private void initContainers() {
	    try {
	        /* TableQuery and SQLContainer for personaddress -table */
	        TableQuery q1 = new TableQuery("contractors", pool);
	        q1.setVersionColumn("OPTLOCK");
	        setPersonContainer(new SQLContainer(q1));

	      
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

	public SQLContainer getPersonContainer() {
		return personContainer;
	}

	public void setPersonContainer(SQLContainer personContainer) {
		this.personContainer = personContainer;
	}
}