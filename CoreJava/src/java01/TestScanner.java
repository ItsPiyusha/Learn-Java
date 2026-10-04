package java01;
import java.util.Scanner;
class TestScanner{
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter emp id, salary, name, permanent status, grade");
        System.out.printf("Emp ID %d Salary %.1f Name %s isPermanent %b Grade %c%n", sc.nextInt(),sc.nextDouble(),sc.next(),sc.nextBoolean(),sc.next().charAt(0));
        sc.close();
    }
}