package org.camunda.bpm.training.trainingCode;

import org.apache.ibatis.logging.LogFactory;
import org.assertj.core.api.Assertions;
import org.camunda.bpm.engine.externaltask.LockedExternalTask;
import org.camunda.bpm.engine.runtime.ActivityInstance;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.test.assertions.ProcessEngineTests;
import org.camunda.bpm.extension.process_test_coverage.junit.rules.TestCoverageProcessEngineRuleBuilder;
import org.camunda.bpm.engine.test.Deployment;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import static org.camunda.bpm.engine.test.assertions.ProcessEngineTests.*;
import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.runtimeService;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Test case starting an in-memory database-backed Process Engine.
 */
public class ProcessUnitTest {

  @ClassRule
  @Rule
  public static ProcessEngineRule rule = TestCoverageProcessEngineRuleBuilder.create().build();

  private static final String PROCESS_DEFINITION_KEY = "trainingCode";

  static {
    LogFactory.useSlf4jLogging(); 
  }

  @Before
  public void setup() {
    init(rule.getProcessEngine());
  }

  /**
   * Just tests if the process definition is deployable.
   */
  @Test
  @Deployment(resources = "trainingProcess.bpmn")
  public void testParsingAndDeployment() {
    // nothing is done here, as we just want to check for exceptions during deployment
  }

  @Test
  @Deployment(resources = "trainingProcess.bpmn")
  public void amountRemainingYes() {
	// Complete the task with variables
		  Map<String, Object> varMap1 = new HashMap<>();
		  varMap1.put("isCreditSufficient", false);
		  varMap1.put("amountDeductible",400);
		  varMap1.put("amountPayable", 500);
		  varMap1.put("invokeFailure", false);
		  varMap1.put("isErrorResolvable", true);
	  //ProcessInstance processInstance = processEngine().getRuntimeService().startProcessInstanceByKey(PROCESS_DEFINITION_KEY);
	  // Now: Drive the process by API and assert correct behavior by camunda-bpm-assert
	  ProcessInstance processInstance = runtimeService().startProcessInstanceByKey(PROCESS_DEFINITION_KEY,varMap1);
		
	  // Assert that the process has started
	  ProcessEngineTests.assertThat(processInstance).isStarted();
	
	  //Check that process is at manual Task
	  ProcessEngineTests.assertThat(processInstance).isWaitingAt("userTaskActivity_05khf53");
	  //rule.getTaskService().claim("")
	  TaskService taskService = rule.getTaskService();
	  Task task = taskService.createTaskQuery().singleResult();
	  assertEquals("userTask", task.getName());
	  
	  taskService.complete(task.getId());
	  
	  // Assert that the process has completed
	  ProcessEngineTests.assertThat(processInstance).isEnded();
  }

  @Test
  @Deployment(resources = "trainingProcess.bpmn")
  public void amountRemainingNo() {
	// Complete the task with variables
		  Map<String, Object> varMap1 = new HashMap<>();
		  varMap1.put("isCreditSufficient", false);
		  varMap1.put("amountDeductible",500);
		  varMap1.put("amountPayable", 500);
		  varMap1.put("invokeFailure", false);
		  varMap1.put("isErrorResolvable", true);
	  //ProcessInstance processInstance = processEngine().getRuntimeService().startProcessInstanceByKey(PROCESS_DEFINITION_KEY);
	  // Now: Drive the process by API and assert correct behavior by camunda-bpm-assert
	  ProcessInstance processInstance = runtimeService().startProcessInstanceByKey(PROCESS_DEFINITION_KEY,varMap1);
		
	  // Assert that the process has started
	  ProcessEngineTests.assertThat(processInstance).isStarted();
	
	  //Check that process is present at checkingExternalTask
	  List<LockedExternalTask> externalTasks = rule
		      .getExternalTaskService()
		      .fetchAndLock(1, "aWorker")
		      .topic("checkingExternalTask_Topic", 5000L)
		      .execute();
		      
		  // Assert that the list size is 1
		  Assertions.assertThat(externalTasks).hasSize(1);
	    

	    // Now: Drive the process by API and assert correct behavior by camunda-bpm-assert

		// Complete the task with variables
		  Map<String, Object> varMap = new HashMap<>();
		  varMap.put("Description", "Updating the checkingExternalTask");
		  rule.getExternalTaskService().complete(externalTasks.get(0).getId(), "aWorker", varMap);
		  
		  //Check that process is at manual Task
		  ProcessEngineTests.assertThat(processInstance).isWaitingAt("userTaskActivity_05khf53");
		  //rule.getTaskService().claim("")
		  TaskService taskService = rule.getTaskService();
		  Task task = taskService.createTaskQuery().singleResult();
		  assertEquals("userTask", task.getName());
		  
		  taskService.complete(task.getId());
		  
		  
		  // Assert that the process has completed
	  ProcessEngineTests.assertThat(processInstance).isEnded();
  }

}
