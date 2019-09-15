package com.project.siapp.Algorithm;

public class Fraction {
    private int numerator;
    private int denominator;

    public Fraction(int numerator, int denominator){

        if (denominator == 0){
            throw new ArithmeticException("Denominator cannot be 0");
        }else {
            this.numerator = numerator;
            this.denominator = denominator;
        }
    }
    public int getNumerator(){
        return numerator;
    }

    public int getDenominator() {
        return denominator;
    }
}
