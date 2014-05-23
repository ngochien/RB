/**
 * Hamburg University of Applied Sciences
 *
 * Programming assignments
 *
 * ngochien.le@haw-hamburg.de
 */
package smoker;

/**
 * @author h13n
 *
 */
public enum Ingredient {
	TOBACCO,
	PAPER,
	MATCH;
	
	public static Ingredient[] getCoupleOfIngredients() {
		System.out.println(Thread.currentThread().getName() + " :Preparing couple of ingredients");
		Ingredient[] ingredients = new Ingredient[2];
		int first = (int) (Math.random() * 3);
		ingredients[0] = Ingredient.values()[first];
		int second = (int) (Math.random() * 3);
		while (second == first) {
			second = (int) (Math.random() * 3);
		}
		ingredients[1] = Ingredient.values()[second];
		System.out.println(Thread.currentThread().getName() + " :Returning " + ingredients[0].name() + " and " + ingredients[1].name());
		return ingredients;
	}
}
