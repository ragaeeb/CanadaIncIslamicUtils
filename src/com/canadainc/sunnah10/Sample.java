package com.canadainc.sunnah10;

import java.sql.*;
import java.util.*;

import com.canadainc.quran10.ibnkatheer.VerseExplanation;

public class Sample
{
  public static void run(Map< String, List<VerseExplanation> > map) throws ClassNotFoundException
  {
    // load the sqlite-JDBC driver using the current class loader
    Class.forName("org.sqlite.JDBC");
    
    Connection connection = null;
    try
    {
      // create a database connection
      connection = DriverManager.getConnection("jdbc:sqlite:quran.db");
      
      Collection<String> chapters = new TreeSet<String>( map.keySet() );
      
      for (String chapter: chapters) {
    	  List<VerseExplanation> explanations = map.get(chapter);
    	  int chapNumber = Integer.parseInt(chapter);
    	  
    	  for (VerseExplanation verse: explanations)
    	  {
    		  PreparedStatement ps = connection.prepareStatement("INSERT INTO ibn_katheer_english VALUES(?,?,?)");
    		  ps.setInt( 1, chapNumber );
    		  ps.setString( 2, verse.title );
    		  ps.setString( 3, verse.body );
    		  
    		  ps.execute();
    		  ps.close();
    	  }
      }
    }
    catch(SQLException e)
    {
      // if the error message is "out of memory", 
      // it probably means no database file is found
    	e.printStackTrace();
      System.err.println(e.getMessage());
    }
    finally
    {
      try
      {
        if(connection != null)
          connection.close();
      }
      catch(SQLException e)
      {
        // connection close failed.
    	  e.printStackTrace();
        System.err.println(e);
      }
    }
  }
}