import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Class representing the client of the binary tree through server
 */
public class TreeClient extends JFrame implements Runnable{
    /**
     * treeType determines type of the tree (String, Double, Integer)
     * result for showing the tree on the label in GUI
     */
    private Type treeType = Type.INTEGER;
    private final JLabel result;
    /**
     * Variables to open connection to a server
     * variables to operate on streams
     * message to send and receive message
     */
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private String message;

    TreeClient() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(600,600);
        setLayout(new BorderLayout());

        result = new JLabel("");

        add(new TreeType(), BorderLayout.PAGE_START);
        add(result, BorderLayout.CENTER);
        add(new ActionPanel(), BorderLayout.PAGE_END);
        setVisible(true);
    }

    /**
     * Runs client opening connection to a server and showing up results from server
     * Closes connection when done.
     */
    public void run()
    {
        try {
            socket = new Socket("localhost", 9876);

            out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(socket.getInputStream());


            while(true) {
                try {
                    String treeResult = (String) in.readObject();
                    result.setText("<html>"+treeResult+"</html>");
                    repaint();
                }catch (ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
            }

        }catch (IOException ex) {
            System.out.println("Can't find server");
        }
        finally {
            try {
                in.close();
                out.close();
                socket.close();
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
    }

    /**
     * Type of the tree to operate on
     */
    enum Type {
        STRING("String"), DOUBLE("Double"), INTEGER("Integer");
        final String type;
        Type(String t) {
            type = t;
        }
    }

    /**
     * Class that provides buttons for changing tree type
     */
    class TreeType extends JPanel {
        TreeType() {

            JButton integerType = new JButton("Integer");
            integerType.addActionListener(e -> {
                treeType = Type.INTEGER;
                try{out.writeObject("c INTEGER");} catch (IOException ex) {ex.printStackTrace();}
            });
            JButton doubleType = new JButton("Double");
            doubleType.addActionListener(e -> {
                treeType = Type.DOUBLE;
                try{out.writeObject("c DOUBLE");} catch (IOException ex) {ex.printStackTrace();}
            });
            JButton stringType = new JButton("String");
            stringType.addActionListener(e -> {
                treeType = Type.STRING;
                try{out.writeObject("c STRING");} catch (IOException ex) {ex.printStackTrace();}
            });

            add(integerType);
            add(doubleType);
            add(stringType);
        }
    }

    /**
     * ActionPanel provides list for choosing operation, text field for typing value and button to perform action
     */

    class ActionPanel extends JPanel {
        final String[] actionType = {"Search", "Insert", "Delete", "Draw"};
        String selectedAction = "Search";
        final JTextArea value = new JTextArea();
        final JButton performAction = new JButton("Perform Action");

        ActionPanel() {
            JComboBox<String> actionList = new JComboBox<>(actionType);
            actionList.addActionListener(e -> {
                JComboBox<String> cb = (JComboBox<String>)e.getSource();
                selectedAction = cb.getSelectedItem().toString();
            });

            value.setPreferredSize(new Dimension(200,25));

            performAction.addActionListener(e -> {
                switch (selectedAction) {
                    case "Search":
                        message = "s " + value.getText();
                        break;
                    case "Insert":
                        message = "i " + value.getText();
                        break;
                    case "Delete":
                        message = "d " + value.getText();
                        break;
                    case "Draw":
                        message = "dr " + value.getText();
                        break;
                }
                try {
                    out.writeObject(message + " " + treeType);
                    out.flush();
                }
                catch (IOException ex) {
                    System.out.println("Couldn't send message");
                }

            });

            add(actionList);
            add(value);
            add(performAction);
        }
    }
}