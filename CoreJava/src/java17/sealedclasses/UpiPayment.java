package java17.sealedclasses;

public final class UpiPayment extends Payment {
    @Override
    public void process() {
        System.out.println("Processing UPI payment");
    }
}