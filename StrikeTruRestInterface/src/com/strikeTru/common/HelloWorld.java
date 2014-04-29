package com.strikeTru.common;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import sailpoint.rest.BaseResource;

@Path("/helloworld")
public class HelloWorld extends BaseResource {

	public HelloWorld() {
		System.out.println("Helloworld Constructor called");
		System.out.println("Helloworld");
	}

	@GET
	@Produces("text/plain")
	public String sayHello() {

		return "helloworld";
	}
	

}
