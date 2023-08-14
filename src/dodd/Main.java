package dodd;

import static dodd.Helpers.lowerCase;

import java.io.*;
import java.util.*;

public class Main {
	
	public static final BufferedReader READER = new BufferedReader(new InputStreamReader(System.in));
	
	private static final Map<String, Runnable> MODES;
	
	static {
		MODES = new HashMap<>();
		
		MODES.put("move", Main::move);
		MODES.put("summary", Main::summary);
		MODES.put("search", Main::search);
	}
	
	public static void main(String[] args) {
		String mode;
		while (true) {
			System.out.print("Program mode (move / summary / search): ");
			mode = lowerCase(Helpers.read());
			
			if (mode.isEmpty()) {
				System.out.println("\nProgram terminated!");
				return;
			}
			
			if (MODES.containsKey(mode)) {
				MODES.get(mode).run();
			}
			else {
				System.out.println(String.format("Unknown mode \"%s\"!", mode));
			}
		}
	}
	
	private static void move() {
		String input;
		
		Type[] types;
		Type move;
		
		System.out.println("\nGet the effectiveness of a move!");
		
		loop: while (true) {
			System.out.print("\nInput defending type combination: ");
			input = Helpers.read();
			
			if (input.isEmpty()) {
				System.out.println("\nReturning...\n");
				return;
			}
			
			if ((types = splitTypes(input)) == null) {
				continue loop;
			}
			
			System.out.print("Input move type: ");
			input = lowerCase(Helpers.read());
			
			move = Type.TYPES.get(input);
			if (move == null) {
				System.out.println(String.format("Unknown type \"%s\"!", input));
				continue;
			}
			
			System.out.println(offensiveEffectiveness(move, types).getDescription());
		}
	}
	
	private static void summary() {
		String input;
		
		Type[] types;
		
		System.out.println("\nGet the offensive and defensive effectivenesses of a type combination!");
		
		loop: while (true) {
			System.out.print("\nInput type combination: ");
			input = lowerCase(Helpers.read());
			
			if (input.isEmpty()) {
				System.out.println("\nReturning...\n");
				return;
			}
			
			if ((types = splitTypes(input)) == null) {
				continue loop;
			}
			
			System.out.println("\nOffensive effectiveness:");
			
			for (Type defense : Type.VALUES) {
				System.out.println(String.format("%s: %s", defense, defensiveEffectiveness(defense, types).getDescription()));
			}
			
			System.out.println("\nDefensive effectiveness:");
			
			for (Type offense : Type.VALUES) {
				System.out.println(String.format("%s: %s", offense, offensiveEffectiveness(offense, types).getDescription()));
			}
		}
	}
	
	private static void search() {
		String input = null;
		
		int max, types, count;
		int[] indices;
		List<Type> typeCombo;
		Effectiveness multiplier;
		
		System.out.println("\nSearch for type combinations with no weaknesses!");
		
		while (true) {
			System.out.print("\nInput maximum number of types: ");
			input = Helpers.read();
			
			if (input.isEmpty()) {
				System.out.println("\nReturning...\n");
				return;
			}
			
			try {
				max = Integer.parseUnsignedInt(input);
			}
			catch (NumberFormatException e) {
				System.out.println(String.format("Could not parse \"%s\"!", input));
				continue;
			}
			
			if (max < 1) {
				System.out.println("Must use at least one type!");
				continue;
			}
			else if (max > Type.VALUES.length) {
				System.out.println("Too many types!");
				continue;
			}
			
			count = 0;
			
			for (types = 1; types <= max; ++types) {
				indices = new int[types];
				for (int i = 0; i < types; ++i) {
					indices[i] = i;
				}
				
				loop: do {
					typeCombo = typeCombination(indices);
					for (Type offense : Type.VALUES) {
						multiplier = offensiveEffectiveness(offense, typeCombo.toArray(new Type[types]));
						
						if (multiplier.size == FractionSize.MORE_THAN_UNITY) {
							continue loop;
						}
					}
					System.out.println(typeCombo);
					++count;
				}
				while (indices[0] < Type.VALUES.length + 1 - types);
			}
			
			System.out.println(String.format("Count: %d", count));
		}
	}
	
	private static Type[] splitTypes(String input) {
		String[] split = input.split("\\W+");
		Type[] types = new Type[split.length];
		for (int i = 0; i < split.length; ++i) {
			String str = lowerCase(split[i]);
			types[i] = Type.TYPES.get(str);
			if (types[i] == null) {
				System.out.println(String.format("Unknown type \"%s\"!", str));
				return null;
			}
		}
		return types;
	}
	
	private static Effectiveness offensiveEffectiveness(Type offense, Type[] types) {
		Effectiveness multiplier = new Effectiveness(1, 1);
		for (Type type : types) {
			multiplier = multiplier.multiply(offense.getOffensiveEffectiveness(type));
		}
		return multiplier;
	}
	
	private static Effectiveness defensiveEffectiveness(Type defense, Type[] types) {
		Effectiveness multiplier = new Effectiveness(1, 1);
		for (Type type : types) {
			multiplier = multiplier.multiply(defense.getDefensiveEffectiveness(type));
		}
		return multiplier;
	}
	
	private static List<Type> typeCombination(int[] indices) {
		List<Type> list = new ArrayList<>();
		for (int index : indices) {
			list.add(Type.VALUES[index]);
		}
		shiftIndices(indices, indices.length - 1);
		return list;
	}
	
	private static void shiftIndices(int[] indices, int i) {
		if (indices[i] < Type.VALUES.length + 1 - indices.length) {
			++indices[i];
		}
		else if (i > 0) {
			shiftIndices(indices, i - 1);
			indices[i] = indices[i - 1] + 1;
		}
	}
}
