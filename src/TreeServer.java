import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Class that provides server side operation on binary tree
 */
class TreeServer implements Runnable {

    /**
     * Variables to run a host on specified socket and wait for connection from client
     * Two streams to communicate with client
     * Message variable to send and receive messages.
     */
    private ServerSocket socket;
    private Socket connection;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private String message;

    /**
     * Default tree type on application start is integer
     * Creating proper trees to operate on.
     */

    private String treeType = "INTEGER";
    private Tree<Integer> integerTree = new Tree<>();
    private Tree<String> stringTree = new Tree<>();
    private Tree<Double> doubleTree = new Tree<>();

    /**
     * Running the server on specified socket
     * Obtaining two streams for communication
     * Reading data in infinite loop
     * Closing connection at the end
     */
    public void run()
    {
        try{
            socket = new ServerSocket(9876);
            connection = socket.accept();

            out = new ObjectOutputStream(connection.getOutputStream());
            out.flush();
            in = new ObjectInputStream(connection.getInputStream());

            do{
                try{

                    message = (String)in.readObject();
                    System.out.println(message);
                    performAction(message);
                } catch (ClassNotFoundException ex) {
                    System.out.println("Can't read data");
                }
            }while(true);
        }
        catch(IOException ioException){
            ioException.printStackTrace();
        }
        finally{
            try{
                in.close();
                out.close();
                socket.close();
            }
            catch(IOException ioException){
                ioException.printStackTrace();
            }
        }
    }

    /**
     * Performing the action specified by client
     * Resetting the tree if client changes type of the tree
     * Then doing proper action on tree object in doAction
     *
     * @param action string with action to be made
     */

    private void performAction(String action) {
        String[] actions = action.split(" ");
        try {
            if(actions[0].equals("c")) {
                treeType = actions[1];
                integerTree = new Tree<>();
                stringTree = new Tree<>();
                doubleTree = new Tree<>();
                doAction(integerTree, "dr", 0);
            }
            else if(actions[2].equals("INTEGER")) {
                int value = Integer.parseInt(actions[1]);
                doAction(integerTree, actions[0], value);
            }
            else if(actions[2].equals("DOUBLE")) {
                double value = Double.parseDouble(actions[1]);
                doAction(doubleTree, actions[0], value);
            }
            else {
                String value = actions[1];
                doAction(stringTree, actions[0], value);
            }
        }catch (NumberFormatException ex) {
            return;
        }
    }

    /**
     * Executing action specified by user
     * @param tree reference to tree to made operation on
     * @param action command to be executed
     * @param value value of the command
     */
    private void doAction(Tree<?> tree, String action, Object value) {
        switch (action) {
            case "i":
                tree.insert(value);
                try {
                    out.writeObject(tree.draw());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "s":
                try {
                    out.writeObject(tree.doSearch(value));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "d":
                tree.doRemove(value);
                try {
                    out.writeObject(tree.draw());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "dr":
                try {
                    out.writeObject(tree.draw());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    /**
     * Running server and client for tests in two threads.
     * @param args
     */
    public static void main(String[] args) {
        Thread client = new Thread(new TreeClient());
        Thread server = new Thread(new TreeServer());
        server.start();
        client.start();
    }

}
