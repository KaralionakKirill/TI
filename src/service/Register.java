package service;

public class Register {
    private int register;

    public Register(int register) {
        this.register = register;
    }

    public int move(int firstIndex, int secondIndex) {
        //get bit
        int first = (register >> firstIndex) & 1;
        int second = (register >> secondIndex) & 1;

        //xor new bit value
        int newBit = first ^ second;

        //move and write new bit
        register <<= 1;
        register |= newBit;

        return first;
    }

    @Override
    public String toString() {
        return "Register{" +
                "register=" + Integer.toBinaryString(register) +
                '}';
    }
}
