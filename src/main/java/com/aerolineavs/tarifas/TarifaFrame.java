package com.aerolineavs.tarifas;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

/**
 * Ventana gráfica para evaluar tarifas de vuelo.
 */
public class TarifaFrame extends JFrame {

    private final JSpinner edadSpinner = new JSpinner(new SpinnerNumberModel(30, 0, 120, 1));
    private final JSpinner vuelosSpinner = new JSpinner(new SpinnerNumberModel(6, 0, 1000, 1));
    private final JComboBox<TipoViajero> tipoCombo = new JComboBox<>(TipoViajero.values());
    private final JComboBox<ClaseVuelo> claseCombo = new JComboBox<>(ClaseVuelo.values());
    private final JComboBox<RegionDestino> destinoCombo = new JComboBox<>(RegionDestino.values());
    private final JTextField ingresosField = new JTextField("25000");
    private final JCheckBox conNinosCheck = new JCheckBox("Viaja con niños menores de 12");
    private final JCheckBox viveConPadresCheck = new JCheckBox("Vive con sus padres");

    private final JLabel tarifaLabel = new JLabel("Sin tarifa aplicada");
    private final JLabel descuentoLabel = new JLabel("0%");
    private final JTextArea suposicionesArea = new JTextArea();

    public TarifaFrame() {
        super("Evaluador de Tarifas Aerolínea");
        configureFrame();
        add(createContentPanel());
        pack();
        setLocationRelativeTo(null);
    }

    private void configureFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(560, 620));
        setResizable(false);
    }

    private JPanel createContentPanel() {
        JPanel panel = new JPanel(new BorderLayout(12, 12));
        panel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JLabel title = new JLabel("Datos del cliente", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 22f));
        panel.add(title, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        addField(formPanel, gbc, "Edad:", edadSpinner, 0);
        addField(formPanel, gbc, "Vuelos al año:", vuelosSpinner, 1);
        addField(formPanel, gbc, "Tipo de viajero:", tipoCombo, 2);
        addField(formPanel, gbc, "Clase preferida:", claseCombo, 3);
        addField(formPanel, gbc, "Destino:", destinoCombo, 4);
        addField(formPanel, gbc, "Ingresos anuales (€):", ingresosField, 5);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        formPanel.add(conNinosCheck, gbc);

        gbc.gridy = 7;
        formPanel.add(viveConPadresCheck, gbc);

        JButton calcularButton = new JButton("Calcular tarifa");
        calcularButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        calcularButton.addActionListener(event -> calcularTarifa());

        gbc.gridy = 8;
        gbc.fill = GridBagConstraints.CENTER;
        formPanel.add(calcularButton, gbc);

        panel.add(formPanel, BorderLayout.CENTER);

        JPanel resultadoPanel = new JPanel();
        resultadoPanel.setLayout(new BoxLayout(resultadoPanel, BoxLayout.Y_AXIS));
        resultadoPanel.setBorder(BorderFactory.createTitledBorder("Resultado"));

        JPanel tarifaRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        tarifaRow.add(new JLabel("Tarifa: "));
        tarifaLabel.setForeground(new Color(0, 102, 204));
        tarifaLabel.setFont(tarifaLabel.getFont().deriveFont(Font.BOLD, 16f));
        tarifaRow.add(tarifaLabel);

        JPanel descuentoRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        descuentoRow.add(new JLabel("Descuento: "));
        descuentoLabel.setFont(descuentoLabel.getFont().deriveFont(Font.BOLD, 14f));
        descuentoRow.add(descuentoLabel);

        suposicionesArea.setRows(6);
        suposicionesArea.setLineWrap(true);
        suposicionesArea.setWrapStyleWord(true);
        suposicionesArea.setEditable(false);
        suposicionesArea.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        resultadoPanel.add(tarifaRow);
        resultadoPanel.add(descuentoRow);
        resultadoPanel.add(new JScrollPane(suposicionesArea));

        panel.add(resultadoPanel, BorderLayout.SOUTH);
        return panel;
    }

    private void addField(JPanel panel, GridBagConstraints gbc, String label, Component component, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        panel.add(new JLabel(label), gbc);

        gbc.gridx = 1;
        panel.add(component, gbc);
    }

    private void calcularTarifa() {
        try {
            int edad = (Integer) edadSpinner.getValue();
            int vuelos = (Integer) vuelosSpinner.getValue();
            TipoViajero tipo = (TipoViajero) tipoCombo.getSelectedItem();
            ClaseVuelo clase = (ClaseVuelo) claseCombo.getSelectedItem();
            RegionDestino destino = (RegionDestino) destinoCombo.getSelectedItem();
            double ingresos = Double.parseDouble(ingresosField.getText().trim());
            boolean conNinos = conNinosCheck.isSelected();
            boolean viveConPadres = viveConPadresCheck.isSelected();

            ClientePotencial cliente = new ClientePotencial(
                    edad, vuelos, tipo, clase, destino, ingresos, conNinos, viveConPadres
            );

            ResultadoTarifa resultado = EvaluadorTarifas.evaluar(cliente);
            tarifaLabel.setText(resultado.tarifa().getNombre());
            descuentoLabel.setText(resultado.tarifa().getDescuentoPorcentaje() + "%");
            suposicionesArea.setText(resultado.suposiciones());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Introduce un valor numérico válido para los ingresos anuales.",
                    "Entrada no válida",
                    JOptionPane.ERROR_MESSAGE
            );
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Hay un problema con los datos introducidos. Revise la información del formulario.",
                    "Datos incorrectos",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
