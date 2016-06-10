package net.techcable.sonarpet.utils;

public class MathMagic {
    public static int divideRoundUp(int dividend, int divisor) {
        return (dividend + (divisor - 1)) / divisor;
    }
}
