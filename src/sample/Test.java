package sample;

import service.Register;

public class Test {
    public static void main(String[] args) {
        String nu = "11111111111111111111111111111111111111111";
        int num = Integer.parseInt(nu.substring(0, 31), 2);
        Register register = new Register(num);
        for(int i = 0; i < 56; i++){
            System.out.print(register.move(28, 1));
        }
    }
}
