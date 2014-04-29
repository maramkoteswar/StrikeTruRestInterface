package com.strikeTru.common;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.apache.log4j.Logger;

import sailpoint.api.SailPointContext;
import sailpoint.api.SailPointFactory;
import sailpoint.api.Workflower;
import sailpoint.object.Identity;
import sailpoint.object.Workflow;
import sailpoint.object.WorkflowLaunch;
import sailpoint.rest.BaseResource;

@Path("/customrestapi")
public class StrikeTruManagerChange extends BaseResource {
	String result;
	SailPointContext context;
	public static Logger strikeTruLog = Logger
			.getLogger(StrikeTruManagerChange.class);

	public StrikeTruManagerChange() {
		super();
		// strikeTruLog.info("StrikeTruManagerChange const called");
		System.out.println("StrikeTruManagerChange const called");

	}

	@GET
	@Produces("text/plain")
	public String sayHello() {
		try {
			SailPointContext myContext = SailPointFactory.getCurrentContext();
			Identity loggedIdentity = this.getLoggedInUser();
			// strikeTruLog.info("Identity ::: " + loggedIdentity.getName());
			System.out.println("Identity ::: " + loggedIdentity.getName());

			// loggedIdentity.getManager();
			// strikeTruLog.info("Identity Manager ::: " +
			// loggedIdentity.getManager());
			strikeTruLog.info("Identity Manager ::: "
					+ loggedIdentity.getManager());

			// if
			// (loggedIdentity.getCapabilityManager().hasCapability(Capability.SYSTEM_ADMINISTRATOR)){

			Identity targetIdentity = myContext.getObjectByName(Identity.class,
					"Adam.Kennedy");
			// strikeTruLog.info("targetIdentity" +targetIdentity.getName());

			System.out.println("targetIdentity" + targetIdentity.getName());

			// Get the target Manager
			String targetManager = targetIdentity.getManager().getName();
					

			System.out.println("TargetManagerStatus: "
					+ targetIdentity.getManager().getManagerStatus());

			// strikeTruLog.info("target Identity:::" +
			// targetManager.toString());
			System.out.println("target Identity:::" + targetManager.toString());

			String NewManagers = "Catherine.Simmons";

			if (targetManager != null) {
				// Checking whether he is assigned manager or not
				if (targetManager.equals(NewManagers)) {
					// strikeTruLog.info("Manager is already existing");
					System.out.println("Manager is already existing");
					result = "Manager is already existing";
				} else {
					// strikeTruLog.info("Manager Assigned");
					System.out.println("Manager Assigned");
					result = "New Manager Assigned";
				}
			}

			// }
			// }
		} catch (Exception e) {
			// strikeTruLog.info("Exception Raisedddd::");

			System.out.println("Exception Raisedddd::");
			e.printStackTrace();

		}
		return result;

	}

	@POST
	@Produces("text/plain")
	public String changeManager1(Map<String, String> post) {
		String message = "FAILED";

		try {

			context = SailPointFactory.getCurrentContext();
			Identity myIdentity = context.getObjectByName(Identity.class,
					post.get("targetId"));
			Identity managerIdentity = context.getObjectByName(Identity.class,
					post.get("managerId"));
		
			System.out.println("Before getManagerStatus()" + myIdentity + ", "
					+ managerIdentity + "ManagerStatus: "
					+ managerIdentity.getManagerStatus());
			if (managerIdentity.getManagerStatus() == true) {
				System.out.println("inside getManagerStatus"
						+ myIdentity.getManager().getName() + "Name:"
						+ managerIdentity.getName().toString());
				managerIdentity.getAttributes();
				if ((myIdentity.getManager().getName())
						.equalsIgnoreCase(managerIdentity.getName().toString())) {

					// System.out.println(managerIdentity.getName()+
					// " : already exists ");
					message = managerIdentity.getName() + " : already exists ";

				} else {
					myIdentity.setManager(managerIdentity);
					context.saveObject(myIdentity);
					context.commitTransaction();

					message = myIdentity.getName()
							+ "'s manager has been changed to : "
							 ;
					// System.out.println(myIdentity.getName() +
					// "'s manager has been changed to : "+managerIdentity.getName());
				}

			} else {

				message = managerIdentity.getName() + " : is not a Manager";
				// System.out.println(managerIdentity.getName()+" : is not a Manager");
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return message;
	}

	@POST
	@Produces("text/plain")
	public String changeManager(Map<String, String> post) {
		managerchangeworkflowRequest();
		return result;
	
	}
	
	public void managerchangeworkflowRequest() {

		System.out.println("managerchangeworkflowRequest stars from here");
		WorkflowLaunch wfl = new WorkflowLaunch();
		Map<String, Object> launchArgs = new HashMap<String, Object>();
		System.out.println("before try");
		launchArgs.put("identity", "spadmin");
		try {
			context = SailPointFactory.getCurrentContext();
			System.out.println("in try");

			Workflow workflow = (Workflow) context.getObjectByName(
					Workflow.class, "StrikeTruManagerChangeRequest");
			wfl.setWorkflowName(workflow.getName());
			wfl.setWorkflowRef(workflow.getName());
			System.out.println("workflow name " + workflow.getName());
			wfl.setCaseName("Testing StrikeTruManagerChangeRequest Case");
			wfl.setVariables(launchArgs);

			Workflower workflower = new Workflower(context);
			WorkflowLaunch launch = workflower.launch(wfl);
			System.out.println("managerchangeworkflow   ends here");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
