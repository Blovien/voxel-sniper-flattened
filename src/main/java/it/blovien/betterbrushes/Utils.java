package it.blovien.betterbrushes;

public class Utils {

	public static String[] hackTheArray(String[] args) {
		String[] returnValue = new String[args.length + 1];
		for (int i = 0; i < args.length; i++) {
			returnValue[i + 1] = args[i];;
		}
		return returnValue;
	}
}
