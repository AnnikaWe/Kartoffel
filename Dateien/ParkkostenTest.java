import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ParkkostenTest implements Parkkosten {

	int dauer = 12;
	int gebühr = 2;
	int summe = 24;

	@Override
	public int berechnen(int parkdauerstd, int Stundengebühr) {
		return parkdauerstd * Stundengebühr;
	}

	@Before
	public void setUp() {
		int dauer = 12;
		int gebühr = 2;
		int summe = 24;

	}

	@Test
	public void test_simple_example_1() {
		assertEquals(berechnen(dauer, gebühr), summe);
	}

}
