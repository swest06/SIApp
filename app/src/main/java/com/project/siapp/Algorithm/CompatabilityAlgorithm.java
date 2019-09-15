package com.project.siapp.Algorithm;



public class CompatabilityAlgorithm {

    public double calculateCompatibility(Fraction userA, Fraction userB){
        double avgDenom = (userA.getDenominator() + userB.getDenominator())/2.0;
        double result1 = Math.sqrt(userA.getNumerator() * userB.getNumerator());
        double percentage = (result1/avgDenom) * 100;
        return percentage;
    }
}
