package org.camunda.bpm.training.trainingCode;


import java.util.Random;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

public class DeductExistingCredit implements JavaDelegate
{
    public void execute(final DelegateExecution execution) throws Exception {
        final Random rando1 = new Random();
        final int payableAmount = (int)execution.getVariable("paymentAmount");
        execution.setVariable("name", (Object)"ashish");
        execution.setVariable("remainingAmount", (Object)(payableAmount - 100));
        execution.setVariable("generateFailure", (Object)rando1.nextBoolean());
        System.out.println("********************************************************");
        System.out.println("DeductExistingCredit java code executed");
        final LoggerDelegate logger = new LoggerDelegate();
        logger.log("Inside CheckWeather Class for deducting from existing Credit");
    }
}