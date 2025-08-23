package ac.za.postapp;

import ac.za.postapp.Pages.Login;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Start the application with the login screen
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Login login = new Login();
                login.setVisible(true);
            }
        });
    }
}
