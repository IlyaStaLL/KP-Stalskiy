package com.example.kp_stalskiy;

public class Calc {
    private int population;
    private double birthRate;
    private double deathRate;

    public Calc(int population, double birthRate, double deathRate) {
        this.population = population;
        this.birthRate = birthRate;
        this.deathRate = deathRate;
    }

    public int calculatePopulationIncrease() {
        return (int) (population * ((birthRate - deathRate) / 100));
    }

    // Другие методы для демографических расчетов
}
