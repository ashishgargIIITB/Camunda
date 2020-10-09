package org.camunda.bpm.TaskCompletion;


import org.camunda.bpm.client.ExternalTaskClient;
//import org.camunda.bpm.engine.variable.Variables;
//import org.camunda.bpm.engine.variable.value.ObjectValue;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.List;


public class ExternalTaskCompletion  {

	public static void main(String[] args) {
		ExternalTaskClient client = ExternalTaskClient.create()
			      .baseUrl("http://localhost:8080/engine-rest")
			      .build();

			    // subscribe to the topic
			    client.subscribe("checkingExternalTask_Topic")
			      .lockDuration(1000)
			      .handler((externalTask, externalTaskService) -> {
			    	
			    	  boolean isTaskFailed = externalTask.getVariable("isTaskFailed");
				        if(!isTaskFailed) {
			        // retrieve a variable from the Workflow Engine
			        int paymentAmount = externalTask.getVariable("paymentAmount");

//			        List<Integer> creditScores = new ArrayList<>(Arrays.asList(defaultScore, 9, 1, 4, 10));
//
//			        // create an object typed variable
//			        ObjectValue creditScoresObject = Variables
//			          .objectValue(creditScores)
//			          .create();

			        // complete the external task
			        externalTaskService.complete(externalTask);
//			          Collections.singletonMap("creditScores", creditScoresObject));
			        System.out.println("Payment Amount here was: "+paymentAmount);
			        System.out.println("The External Task " + externalTask.getId() + " has been completed!");
			      }
			      else {
		        		externalTaskService.handleBpmnError(externalTask, "externalSystemIssueErrorCode");//, "raisingErrorMessage");
		        		 System.out.println("Error has been rasied for " + externalTask.getId());
		        }
			      }).open();

	}

}
