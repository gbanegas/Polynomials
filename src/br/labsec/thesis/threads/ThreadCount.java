package br.labsec.thesis.threads;

import java.util.HashMap;
import java.util.List;

import javax.naming.LimitExceededException;

import br.labsec.thesis.otimization.PolynomialContXor;
import br.labsec.thesis.polynomials.Polynomial;

public class ThreadCount extends Thread {

	private List<String> listPol;
	private HashMap<String, Integer> toSalve;
	private Lock lock;

	public ThreadCount(List<String> listPol,
			HashMap<String, Integer> toSalve, Lock lock) {
		this.listPol = listPol;
		this.toSalve = toSalve;
		this.lock = lock;
	}

	@Override
	public void run() {
		System.out.println("Starting thread... " + this.getId());
		for (int i = 0; i < listPol.size(); i++) {
			Polynomial pol = Polynomial.createFromString(listPol.get(i));
			PolynomialContXor cont = new PolynomialContXor();
			int xor = cont.calculate(pol);
			
			try {
				lock.lock();
				toSalve.put(listPol.get(i), xor);
				lock.unlock();
				try {
					cont.saveXLS();
				} catch (LimitExceededException e) {
					e.printStackTrace();
				}
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				lock.unlock();
				e1.printStackTrace();
			}

		}

	}
}
