package java17.sealedclasses;

public final class BankTransfer extends Payment {
    @Override
    public void process() {
        System.out.println("Processing bank transfer");
    }
}