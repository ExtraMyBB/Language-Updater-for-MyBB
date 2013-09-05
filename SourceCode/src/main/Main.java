package main;

import controller.MainController;
import gui.MainFrame;
import javax.swing.JOptionPane;
import javax.swing.UnsupportedLookAndFeelException;

public class Main {

    /**
     * Everything starts from here.
     * 
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        try {
            // Tries to make some inits before using application
            MainController.getInstance().loadApp();
        } catch (Exception ex) {
            // Announce user using a properly error message
            JOptionPane.showMessageDialog(null, ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            
            // Exit immediately
            System.exit(0);
        }
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if (MainController.getInstance().getConfigProperty("LookAndFeel", "Nimbus").equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }
}
