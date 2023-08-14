package dodd;

import static dodd.Helpers.*;

import java.util.*;

public enum Type {
	
	NORMAL("Normal"),
	FIGHTING("Fighting"),
	FLYING("Flying"),
	POISON("Poison"),
	GROUND("Ground"),
	ROCK("Rock"),
	BUG("Bug"),
	GHOST("Ghost"),
	STEEL("Steel"),
	FIRE("Fire"),
	WATER("Water"),
	GRASS("Grass"),
	ELECTRIC("Electric"),
	PSYCHIC("Psychic"),
	ICE("Ice"),
	DRAGON("Dragon"),
	DARK("Dark"),
	FAIRY("Fairy");
	
	public static final Type[] VALUES = values();
	
	public static final Map<String, Type> TYPES;
	public static final Map<Type, Map<Type, Effectiveness>> TYPE_CHARTS;
	
	static {
		TYPES = new HashMap<>();
		
		for (Type type : Type.VALUES) {
			TYPES.put(lowerCase(type.name), type);
		}
		
		TYPE_CHARTS = new HashMap<>();
		
		TYPE_CHARTS.put(NORMAL, map(arr(), arr(ROCK, STEEL), arr(GHOST)));
		TYPE_CHARTS.put(FIGHTING, map(arr(DARK, ICE, NORMAL, ROCK, STEEL), arr(BUG, FAIRY, FLYING, POISON, PSYCHIC), arr(GHOST)));
		TYPE_CHARTS.put(FLYING, map(arr(BUG, FIGHTING, GRASS), arr(ELECTRIC, ROCK, STEEL), arr()));
		TYPE_CHARTS.put(POISON, map(arr(FAIRY, GRASS), arr(POISON, GROUND, ROCK, GHOST), arr(STEEL)));
		TYPE_CHARTS.put(GROUND, map(arr(ELECTRIC, FIRE, POISON, ROCK, STEEL), arr(BUG, GRASS), arr(FLYING)));
		TYPE_CHARTS.put(ROCK, map(arr(BUG, FIRE, FLYING, ICE), arr(FIGHTING, GROUND, STEEL), arr()));
		TYPE_CHARTS.put(BUG, map(arr(DARK, GRASS, PSYCHIC), arr(FAIRY, FIGHTING, FIRE, FLYING, GHOST, POISON, STEEL), arr()));
		TYPE_CHARTS.put(GHOST, map(arr(GHOST, PSYCHIC), arr(DARK), arr(NORMAL)));
		TYPE_CHARTS.put(STEEL, map(arr(FAIRY, ICE, ROCK), arr(ELECTRIC, FIRE, STEEL, WATER), arr()));
		TYPE_CHARTS.put(FIRE, map(arr(BUG, GRASS, ICE, STEEL), arr(DRAGON, FIRE, ROCK, WATER), arr()));
		TYPE_CHARTS.put(WATER, map(arr(FIRE, GROUND, ROCK), arr(DRAGON, GRASS, WATER), arr()));
		TYPE_CHARTS.put(GRASS, map(arr(GROUND, ROCK, WATER), arr(BUG, DRAGON, FIRE, FLYING, GRASS, POISON, STEEL), arr()));
		TYPE_CHARTS.put(ELECTRIC, map(arr(FLYING, WATER), arr(DRAGON, ELECTRIC, GRASS), arr(GROUND)));
		TYPE_CHARTS.put(PSYCHIC, map(arr(FIGHTING, POISON), arr(PSYCHIC, STEEL), arr(DARK)));
		TYPE_CHARTS.put(ICE, map(arr(DRAGON, FLYING, GRASS, GROUND), arr(FIRE, ICE, STEEL, WATER), arr()));
		TYPE_CHARTS.put(DRAGON, map(arr(DRAGON), arr(STEEL), arr(FAIRY)));
		TYPE_CHARTS.put(DARK, map(arr(GHOST, PSYCHIC), arr(DARK, FAIRY, FIGHTING), arr()));
		TYPE_CHARTS.put(FAIRY, map(arr(DARK, DRAGON, FIGHTING), arr(FIRE, POISON, STEEL), arr()));
	}
	
	public final String name;
	
	private Type(String name) {
		this.name = name;
	}
	
	public Effectiveness getOffensiveEffectiveness(Type defense) {
		return TYPE_CHARTS.get(this).getOrDefault(defense, Effectiveness.NORMAL);
	}
	
	public Effectiveness getDefensiveEffectiveness(Type offense) {
		return offense.getOffensiveEffectiveness(this);
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	public static Map<Type, Effectiveness> map(Type[] weak, Type[] resistant, Type[] immune) {
		Map<Type, Effectiveness> map = new HashMap<>();
		for (Type type : weak) {
			map.put(type, Effectiveness.WEAK);
		}
		for (Type type : resistant) {
			map.put(type, Effectiveness.RESISTANT);
		}
		for (Type type : immune) {
			map.put(type, Effectiveness.IMMUNE);
		}
		return map;
	}
}
