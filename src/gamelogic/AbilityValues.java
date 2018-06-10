package gamelogic;

import java.util.HashMap;

public class AbilityValues {

    public static HashMap<String, Double> factorMax = new HashMap<String, Double>() {
        private static final long serialVersionUID = 1L;
	{
        put("fireball", (double) 3);
    }};

    public static HashMap<String, Double> factorIncrease = new HashMap<String, Double>() {
        private static final long serialVersionUID = 1L;
	{
        put("fireball", (double) 0.04);
    }};

    public static HashMap<String, Integer> cooldown = new HashMap<String, Integer>() {
        private static final long serialVersionUID = 1L;
	{
        put("fireball", 50);
    }};
}
