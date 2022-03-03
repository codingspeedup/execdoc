package io.github.codingspeedup.execdoc.spring.gui;

import io.github.codingspeedup.execdoc.spring.generators.SpringBootGenCfg;
import org.apache.commons.lang3.SerializationUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SpringGeneratorConfigGUI extends JDialog {

    private final SpringBootGenCfg genCfg;
    private int dialogResult = JOptionPane.DEFAULT_OPTION;

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JCheckBox restMethodsBox;
    private JCheckBox dryRunBox;


    public SpringGeneratorConfigGUI(SpringBootGenCfg genCfg) {
        this.genCfg = genCfg;

        setTitle("Generator Options");
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        restMethodsBox.setSelected(genCfg.isRestMethods());
        dryRunBox.setSelected(genCfg.isDryRun());

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

    public static SpringBootGenCfg showDialog(SpringBootGenCfg genCfg) {
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
        SpringBootGenCfg config = showDialog(new SpringBootGenCfg());
        System.out.println(config == null ? null : config.isDryRun());
        System.exit(0);
    }

    private void onOK() {
        dialogResult = JOptionPane.OK_OPTION;
        genCfg.setRestMethods(restMethodsBox.isSelected());
        genCfg.setDryRun(dryRunBox.isSelected());
        dispose();
    }

    private void onCancel() {
        dialogResult = JOptionPane.CANCEL_OPTION;
        dispose();
    }

}
