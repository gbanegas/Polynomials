package br.labsec.thesis.gui;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import br.labsec.thesis.threads.ProgressBar;

public class ProgressBarGUI extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	ProgressBar proc;
	Thread thr;
	private javax.swing.JProgressBar jProgressBar1;
	private javax.swing.JTextField jTextField1;

	public void startProcessing() {
		proc.setInterrupted(false);
		thr = new Thread(proc);
		thr.start();
	}

	public void stopProcessing() {
		proc.setInterrupted(true);
		try {
			if (thr != null)
				thr.join();
		} catch (InterruptedException ex) {
		}
		jProgressBar1.setValue(0);
	}

	public ProgressBarGUI() {
		super();
		initComponents();
		// setBounds (new Rectangle (200, 200, 100, 100));
		// setPreferredSize (new Dimension (300, 200));
		setTitle("Polynomial Progress");
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		proc = new ProgressBar();
		proc.setProgressBar(this.jProgressBar1);
		proc.setTextArea(jTextField1);

		
	}

	private void initComponents() {

		jProgressBar1 = new javax.swing.JProgressBar();
		jTextField1 = new javax.swing.JTextField();

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

		jTextField1.setText("jTextField1");

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(
				getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addComponent(jProgressBar1,
						javax.swing.GroupLayout.DEFAULT_SIZE, 281,
						Short.MAX_VALUE).addComponent(jTextField1));
		layout.setVerticalGroup(layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(
						layout.createSequentialGroup()
								.addComponent(jProgressBar1,
										javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(
										javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(jTextField1,
										javax.swing.GroupLayout.PREFERRED_SIZE,
										41,
										javax.swing.GroupLayout.PREFERRED_SIZE)
								.addContainerGap()));

		pack();
	}

	public static void main(String[] args) {
		ProgressBarGUI tb = new ProgressBarGUI();
		tb.pack();
		tb.setVisible(true);
	}

	
}
