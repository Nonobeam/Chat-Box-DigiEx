package project.View;

import project.data.dataChat;
import project.socket.Send;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Socket;

public class Group extends JDialog {

    private Socket _socket = null;
    private String _myName;
    public Group(JFrame parent, Socket socket, String myName) {
        super(parent, "Create Group", true); // true for modal dialog
        _socket = socket;
        _myName = myName;
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(parent);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); // Vertical layout

        JLabel nameLabel = new JLabel("Group Name: ");
        JTextField fieldName = new JTextField(20);

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            StringBuilder selectedUsers = new StringBuilder();
            for (Component comp : panel.getComponents()) {
                if (comp instanceof JCheckBox) {
                    JCheckBox checkBox = (JCheckBox) comp;
                    if (checkBox.isSelected()) {
                        selectedUsers.append(checkBox.getText()).append(",");
                    }
                }
            }
            if (selectedUsers.length() > 0) {
                selectedUsers.setLength(selectedUsers.length() - 2);
                new Send(_socket).sendData("type:group&&receive:" + _myName + "," + selectedUsers.toString() + "&&" + "send:" + fieldName.getText());
            } else {
                JOptionPane.showMessageDialog(this, "You have not selected any users!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });



        JPanel namePanel = new JPanel();
        namePanel.add(nameLabel);
        namePanel.add(fieldName);
        panel.add(namePanel);
        panel.add(new JLabel("Select users:"));

        for (String online : dataChat.userOnline) {
            JCheckBox checkBox = new JCheckBox(online);
            panel.add(checkBox);
        }

        panel.add(submitButton);

        setContentPane(panel);
    }
}
