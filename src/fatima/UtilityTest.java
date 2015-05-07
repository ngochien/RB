package fatima;

import static org.junit.Assert.*;

import org.junit.Test;

public class UtilityTest {

	@Test
	public void testRandom() {
		int min = 10;
		int max = 100;
		for (int i = 0; i < 1000000; i++) {
			int result = Utility.random(min, max);
			assertTrue(result >= min);
			assertTrue(result <= max);
		}
	}

}
