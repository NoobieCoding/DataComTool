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
		myInfoPanel.setLayout(new FlowLayout());
		myInfoPanel.add(new JLabel("Your IP Address: " + networkInfo.getMyIPAddress()));
		JPanel startPanel = new JPanel();
		startPanel.setLayout(new FlowLayout());
		JButton startBtn = new JButton("Find");
		startBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				networkInfo.getData();
			}
		});
		startPanel.add(startBtn);
		JPanel resultPanel = new JPanel();
		resultLbl = new JLabel("Click to find");
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
			JOptionPane.showMessageDialog(panel, allFoundIPs);
		} else if (arg1 instanceof Integer) {
			result = "Program is finding. (" + (100 * (int) arg1 / 256) + "%) Found: "
					+ networkInfo.getAvailableDevices().size() + "\n";
			resultLbl.setText(result);
		}

	}

}
