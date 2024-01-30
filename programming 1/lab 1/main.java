public class main {
	public static void main(String[] args) {
		short[] c = new short[9];
		short k = 21;
		for (int i = 0; i < 9; i++) {
			c[i] = k;
			k -= 2;
		}

		double[] x = new double[20];
		for (int i = 0; i < x.length; i++) {
			x[i] = (Math.random( ) * (- 15)) + 5;
		}

		double [][] c0 = new double[9][20];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 20; j++) {
                if (c[i] == 19) {
                    c0[i][j] = Math.tan(Math.asin(Math.sin(x[j])));
                }
                else if (c[i] == 5 || c[i] == 9 || c[i] == 13 || c[i] == 21) {
                    c0[i][j] = Math.atan(1 / Math.pow(Math.E, 
                        (Math.acos((3 * (x[j] - 2.5)) / (4 * 15)))));
                }
                else {
                    c0[i][j] = (Math.sin(Math.pow(Math.E, 
                        (Math.pow((x[j] + 1) / 4, 2)))) + 0.5) 
			      / (Math.pow(Math.cos((3 + x[j]) / 3), 2));
                }
            }
        }

        for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 20; ++j) {
				System.out.printf("%.3f\t", c0[i][j]);
			}
			System.out.println();
		}
		System.out.println();

		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 20; j++) {
				c0[i][j] = switch (c[i]) {
					case 19 -> Math.tan(Math.asin(Math.sin(x[j])));
					case 5, 9, 13, 21 -> Math.atan(1 / Math.pow(Math.E, 
						(Math.acos((3 * (x[j] - 2.5)) / (4 * 15)))));
					default -> (Math.sin(Math.pow(Math.E, 
							(Math.pow((x[j] + 1) / 4, 2)))) + 0.5) / 
							(Math.pow(Math.cos((3 + x[j]) / 3), 2));
				};
			}
		}		


		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 20; ++j) {
				System.out.printf("%.3f\t", c0[i][j]);
			}
			System.out.println();
		}
	}
}

//switch приведенеи типов приоритет