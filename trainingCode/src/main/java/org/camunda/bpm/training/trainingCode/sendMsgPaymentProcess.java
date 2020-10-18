package org.camunda.bpm.training.trainingCode;

import java.util.Random;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

public class sendMsgPaymentProcess implements JavaDelegate {

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("********************************************************");
        System.out.println("Sending msg for Payment Process initiation");
        SendMessage sendMsg = new SendMessage();
        sendMsg.execute(execution);
        final LoggerDelegate logger = new LoggerDelegate();
        logger.log("Sent msg for Payment Process initiation");

	}

}
