package br.labsec.thesis.threads;

import java.util.HashMap;
import java.util.List;

import org.apache.commons.math3.exception.OutOfRangeException;

import br.labsec.thesis.otimization.PolynomialContXor;
import br.labsec.thesis.polynomials.Polynomial;

public class ThreadCount extends Thread {

	private List<String> listPol;
	private HashMap<String, Integer> toSalve;
	private Lock lock;

	public ThreadCount(List<String> listPol, HashMap<String, Integer> toSalve,
			Lock lock) {
		this.listPol = listPol;
		this.toSalve = toSalve;
		this.lock = lock;
	}

	@Override
	public void run() {
		System.out.println("Starting thread... " + this.getId());
		for (int i = 0; i < listPol.size(); i++) {
			int xor = 99999999;
			Polynomial pol = Polynomial.createFromString(listPol.get(i));
			PolynomialContXor cont = new PolynomialContXor();
			try {
				xor = cont.calculate(pol);
			} catch (OutOfRangeException ex) {
				System.err.println(pol.toPolynomialString());
			}
			try {
				lock.lock();
				toSalve.put(listPol.get(i), xor);
				lock.unlock();
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				lock.unlock();
				e1.printStackTrace();
			}

		}

	}
}
