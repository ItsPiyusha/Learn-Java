package java17.sealedclasses;

public sealed class Payment permits BankTransfer, UpiPayment, CardPayment {
    public void process(){
        System.out.println("Processing payment");
    }
}
