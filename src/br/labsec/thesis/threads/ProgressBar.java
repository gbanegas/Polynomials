package br.labsec.thesis.threads;

import javax.swing.JProgressBar;
import javax.swing.JTextField;

public class ProgressBar implements Runnable {
	private JProgressBar jpb;
	private boolean interrupted = false;
	private JTextField jTextField1;

	public ProgressBar() {
	}

	public void setProgressBar(JProgressBar jpb) {
		this.jpb = jpb;
	}

	public void setInterrupted(boolean interrupted) {
		this.interrupted = interrupted;
	}

	public void run() {
		while (!interrupted) {
			jpb.setValue(0);
			jTextField1.setText("Calculanting things.");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException ex) {
			}
			jpb.setValue(25);
			jTextField1.setText("Calculanting things..");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException ex) {
			}
			jpb.setValue(50);
			jTextField1.setText("Calculanting things...");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException ex) {
			}
			jpb.setValue(75);
			jTextField1.setText("Is this really important? :)");
			try {
				Thread.sleep(1900);
			} catch (InterruptedException ex) {
			}
			jTextField1.setText("Sorry about the delay :(");
			jpb.setValue(100);
			
			try {
				Thread.sleep(1900);
			} catch (InterruptedException ex) {
			}
		}
	}

	public void setTextArea(JTextField jTextField1) {
		
		this.jTextField1 = jTextField1;
	}
}
