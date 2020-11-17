package org.camunda.bpm.training.trainingCode;


import java.util.Random;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

public class DeductExistingCredit implements JavaDelegate
{
    public void execute(final DelegateExecution execution) throws Exception {
        Random rando1 = new Random();
        double payableAmount = (double)execution.getVariable("amountPayable");
        double amountDeductible = (double)execution.getVariable("amountDeductible");
        execution.setVariable("name", (Object)"ashish");
        execution.setVariable("remainingAmount", (Object)(payableAmount - amountDeductible));
        execution.setVariable("generateFailure", (Object)rando1.nextBoolean());
        System.out.println("********************************************************");
        System.out.println("DeductExistingCredit java code executed");
        final LoggerDelegate logger = new LoggerDelegate();
        logger.log("Inside DeductExistingCredit Class for deducting payment rom existing Credit");
    }
}