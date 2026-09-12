package java17.sealedclasses;

public final class CardPayment extends Payment {
    @Override
    public void process() {
        System.out.println("Processing card payment");
    }
    
}
