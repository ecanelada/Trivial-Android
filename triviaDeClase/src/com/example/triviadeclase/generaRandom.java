package com.example.triviadeclase;

import java.util.Random;

public class generaRandom {

	Random r = new Random();
	int ra = r.nextInt(1000);
	
	
	public int getRa() {
		return ra;
	}
}
