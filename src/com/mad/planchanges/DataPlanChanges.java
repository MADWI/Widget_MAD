package com.mad.planchanges;

public class DataPlanChanges { 
  public String title; 
  public String date; 
  public String body; 
 
  public DataPlanChanges() { 
	  title = "";
	  date = "";
	  body = "";	  
  } 
  
  public String getTitle()
  {
	  return title;
  }
  public String getDate()
  {
	  return date;
  }
  public String getBody()
  {
	  return body;
  }
  
  public void setTitle(String arg)
  {
	  title = arg;
  }
  public void setDate(String arg)
  {
	  date = arg;
  }
  public void setBody(String arg)
  {
	  body = arg;
  }
  
} 