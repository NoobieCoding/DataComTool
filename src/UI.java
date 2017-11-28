import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class UI extends JFrame implements Observer {
	private JPanel panel;
	private JButton startBtn;
	private JLabel resultLbl;
	private NetworkInfo networkInfo;
	
	public UI(NetworkInfo ni) {
		super();
		networkInfo = ni;
		panel = (JPanel) getContentPane();
		panel.setPreferredSize(new Dimension(240, 120));
		setTitle("Find same network devices.");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		init();
		pack();
		
	}

	public void run() {
		setVisible(true);
		
	}

	public void init() {
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		JPanel myInfoPanel = new JPanel();
		JPanel startPanel = new JPanel();
		JPanel resultPanel = new JPanel();
		
		myInfoPanel.setLayout(new FlowLayout());
		myInfoPanel.add(new JLabel("Your IP Address: " + networkInfo.getMyIPAddress()));
		startPanel.setLayout(new FlowLayout());
		
		startBtn = new JButton("Find");
		resultLbl = new JLabel("Click to find");
		
		startBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				networkInfo.getData();
				startBtn.setEnabled(false);
				startBtn.setText("Finding..");
				
			}
			
		});
		
		startPanel.add(startBtn);
		resultPanel.add(resultLbl);

		panel.add(myInfoPanel);
		panel.add(startPanel);
		panel.add(resultPanel);
		
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		String result = "";
		
		if (arg1 instanceof String) {
			result = "Finish, all found: " + networkInfo.getAvailableDevices().size() + "\n";
			resultLbl.setText(result);
			String allFoundIPs = "All found IP Address:\n";
			Vector<String> v = networkInfo.getAvailableDevices();
			
			for (String s : v) {
				allFoundIPs += (s + "\n");
				
			}
			
			startBtn.setEnabled(true);
			startBtn.setText("Find");
			
			JOptionPane.showMessageDialog(panel, allFoundIPs);
			
		} else if (arg1 instanceof Integer) {
			int percent = (100 * (int) arg1 / 256);
			
			result = "Program is finding. (" + percent + "%) Found: "
					+ networkInfo.getAvailableDevices().size() + "\n";
			
			resultLbl.setText(result);			
			
		}

	}

}
