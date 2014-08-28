package br.labsec.thesis.algorithm;

import org.apache.commons.math3.linear.RealMatrix;

import br.labsec.thesis.polynomials.Trinomial;

public class GenerateAlgorithm {

	public static String generateAlgorithm(Trinomial tri, RealMatrix matrix,
			int W) {

		String alg = "";
		double columSize = matrix.getColumnDimension();
		double wordSize = W;
		int maxSizeArray = (int) Math.ceil(columSize / wordSize);

		switch (W) {
		case 8:
			alg = alg + "byte[] array = " + "new byte["
					+ maxSizeArray + "];\n";
			break;
		case 16:
			alg = alg + "short[] array = " + "new short["
					+ maxSizeArray + "];\n";
			break;
		case 32:
			alg = alg + "int[] array = " + "new int["
					+ maxSizeArray + "];\n";
			break;

		case 64:
			alg = alg + "long[] array = " + "new long["
					+ maxSizeArray + "];\n";
			break;
		}

		for (int i = matrix.getColumnDimension(); i > matrix
				.getColumnDimension() - W; i--) {

		}
		System.out.println(alg);
		return alg;

	}

}
