package dodd;

import static dodd.FractionSize.*;

public class Effectiveness {
	
	public static final Effectiveness NORMAL = new Effectiveness(1, 1);
	public static final Effectiveness WEAK = new Effectiveness(2, 1);
	public static final Effectiveness RESISTANT = new Effectiveness(1, 2);
	public static final Effectiveness IMMUNE = new Effectiveness(0, 1);
	
	public final int num, den;
	public final FractionSize size;
	
	public Effectiveness(int num, int den) {
		if (den == 0) {
			throw new IllegalArgumentException(String.format("Denominator must not be zero (%d/%d)!", num, den));
		}
		
		int hcf = Helpers.hcf(num, den);
		this.num = (den < 0 ? -num : num) / hcf;
		this.den = (den < 0 ? -den : den) / hcf;
		
		int n = Math.abs(this.num), d = Math.abs(this.den);
		size = n == d ? EQUAL_TO_UNITY : (n == 0 ? EQUAL_TO_ZERO : (n > d ? MORE_THAN_UNITY : LESS_THAN_UNITY));
	}
	
	public Effectiveness multiply(Effectiveness other) {
		return new Effectiveness(num * other.num, den * other.den);
	}
	
	public String getDescription() {
		String str;
		switch (size) {
			case EQUAL_TO_UNITY:
				str = "normal damage";
				break;
			case EQUAL_TO_ZERO:
				str = "no effect";
				break;
			case LESS_THAN_UNITY:
				str = "not very effective";
				break;
			default:
				str = "super effective";
		}
		
		StringBuilder builder = new StringBuilder().append(this).append("x (").append(str).append(")");
		return builder.toString();
	}
	
	@Override
	public String toString() {
		if (den == 1) {
			return String.format("%d", num);
		}
		else {
			return String.format("%d/%d", num, den);
		}
	}
}
