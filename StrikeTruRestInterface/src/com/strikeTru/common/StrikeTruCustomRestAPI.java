package com.strikeTru.common;

import java.util.Set;

import sailpoint.rest.SailPointRestApplication;
import sailpoint.tools.GeneralException;

public class StrikeTruCustomRestAPI extends SailPointRestApplication {

	public StrikeTruCustomRestAPI() throws GeneralException{
		
		super();
		
		
	}
	@Override
	public Set<Class<?>> getClasses() {
		// TODO Auto-generated method stub
		Set<Class<?>> classes= super.getClasses();
		classes.add(HelloWorld.class);
		classes.add(StrikeTruManagerChange.class);
		return classes;
		
		
	}
	
	
	
}
