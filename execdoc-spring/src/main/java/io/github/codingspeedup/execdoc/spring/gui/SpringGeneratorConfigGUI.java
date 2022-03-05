package io.github.codingspeedup.execdoc.spring.gui;

import io.github.codingspeedup.execdoc.spring.generators.SpringGenConfig;
import org.apache.commons.lang3.SerializationUtils;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class SpringGeneratorConfigGUI extends JDialog {

    private final SpringGenConfig genCfg;
    private int dialogResult = JOptionPane.DEFAULT_OPTION;

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JCheckBox restMethodsBox;
    private JCheckBox dryRunBox;
    private JCheckBox forceBox;

    public SpringGeneratorConfigGUI(SpringGenConfig genCfg) {
        this.genCfg = genCfg;

        setTitle("Generator Options");
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        restMethodsBox.setSelected(genCfg.isRestMethods());
        dryRunBox.setSelected(genCfg.isDryRun());
        forceBox.setSelected(genCfg.isForce());

        buttonOK.addActionListener(e -> onOK());

        buttonCancel.addActionListener(e -> onCancel());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    public static SpringGenConfig showDialog(SpringGenConfig genCfg) {
        SpringGeneratorConfigGUI dialog = new SpringGeneratorConfigGUI(SerializationUtils.clone(genCfg));
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
        if (dialog.dialogResult == JOptionPane.OK_OPTION) {
            return dialog.genCfg;
        }
        return null;
    }

    public static void main(String[] args) {
        SpringGenConfig config = showDialog(new SpringGenConfig());
        System.out.println(config == null ? null : config.isDryRun());
        System.exit(0);
    }

    private void onOK() {
        dialogResult = JOptionPane.OK_OPTION;
        genCfg.setRestMethods(restMethodsBox.isSelected());
        genCfg.setDryRun(dryRunBox.isSelected());
        genCfg.setForce(forceBox.isSelected());
        dispose();
    }

    private void onCancel() {
        dialogResult = JOptionPane.CANCEL_OPTION;
        dispose();
    }

}
