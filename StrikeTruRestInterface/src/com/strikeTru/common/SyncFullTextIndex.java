package com.strikeTru.common;

import java.net.InetAddress;

import java.net.UnknownHostException;
import java.util.Date;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import sailpoint.api.SailPointContext;
import sailpoint.api.SailPointFactory;
import sailpoint.object.Attributes;
import sailpoint.object.Identity;
import sailpoint.object.TaskDefinition;
import sailpoint.object.TaskResult;
import sailpoint.object.TaskSchedule;
import sailpoint.rest.BaseResource;
import sailpoint.task.FullTextIndexer;
import sailpoint.tools.GeneralException;
import sailpoint.tools.Message;

@Path("XOMRefreshIndex")
public class SyncFullTextIndex extends BaseResource {
	private static Logger FTILog = Logger.getLogger(SyncFullTextIndex.class.getName());
	
	@GET
	@Produces(MediaType.TEXT_XML)
	public String syncFTI () throws GeneralException{
		String ret = "Failed";
		Attributes<String, Object> args = new Attributes<String, Object>();
		TaskResult result = new TaskResult();
		FullTextIndexer indexer = new FullTextIndexer();
		TaskSchedule schedule = new TaskSchedule();
		String host = null;
		try {
			host = InetAddress.getLocalHost().getHostName();
			FTILog.debug("Got HOSTNAME: " + host);
		} catch (UnknownHostException e) {
			FTILog.error("Unable to determine HOSTNAME");
			FTILog.error(e);
			
		}
		
		try {
			SailPointContext context = SailPointFactory.getCurrentContext();
			TaskDefinition def = context.getObjectByName(TaskDefinition.class, "FTI - REST");
			FTILog.debug("Created TaskDefinition: " + def.getName());
			try {
				TaskResult oldResult = context.getObjectByName(TaskResult.class, "FTI - REST: " + "[" + host + "] ");
				context.removeObject(oldResult);
				context.commitTransaction();
			} catch (GeneralException ex) {
				FTILog.debug("Previous task result did not exist... Skipping.");
			}
			
			Message message = new Message();
			
			result.setName("FTI - REST: " + "[" + host + "] ");
			result.setLaunched(new Date());
			result.setType(sailpoint.object.TaskItemDefinition.Type.System);
			result.setDefinition(def);
			result.setOwner(context.getObject(Identity.class, "spadmin"));
			
			indexer.execute(context, schedule, result, args);
			
			result.setCompletionStatus(TaskResult.CompletionStatus.Success);
			result.setCompleted(new Date());
			
			
			
			message.setType(Message.Type.Info);
			message.setKey("Successfully Indexed Full Text on " + host);
			
			result.addMessage(message);
			
			
			
			ret = result.toXml();
			
			//context.getObjectByName(Identity.class, "");
			
			
			
			context.saveObject(result);
			context.commitTransaction();
			
		} catch (Exception ex) {
			result.setCompletionStatus(TaskResult.CompletionStatus.Error);
			FTILog.error("ERROR: Unable to complete Full Text Index.");
			FTILog.error(ex);
		}
		return ret;
	}

}
