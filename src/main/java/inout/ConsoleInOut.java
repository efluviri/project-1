package main.java.inout;
import java.util.Scanner;

public class ConsoleInOut implements InOut {
    private final Scanner scanner = new Scanner(System.in);
    @Override
    public void print(String message){
        System.out.println(message);
    }
    public String readline(){
        return scanner.nextLine().trim();
    }
}
