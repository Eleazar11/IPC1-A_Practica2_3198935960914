/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package spaceInvaders;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author 3198935960914 - Eleazar Colop
 */
public class CargarJuego extends JFrame implements ActionListener {

    private JButton seleccionarArchivoButton;
    private JButton cargarPartidaButton;
    private JButton regresarMenuButton;
    private JLabel estadoLabel;

    private File archivoSeleccionado;

    public CargarJuego() {
        initComponents();
    }

    private void initComponents() {
        // Crear panel de fondo personalizado
        ImagenFondo backgroundPanel = new ImagenFondo();
        backgroundPanel.setLayout(null);
        setContentPane(backgroundPanel);

        // Botón para seleccionar archivo
        seleccionarArchivoButton = new JButton("Seleccionar Archivo");
        seleccionarArchivoButton.setBounds(50, 50, 200, 30);
        seleccionarArchivoButton.addActionListener(this);
        backgroundPanel.add(seleccionarArchivoButton);

        // Botón para cargar partida
        cargarPartidaButton = new JButton("Cargar Partida");
        cargarPartidaButton.setBounds(50, 100, 200, 30);
        cargarPartidaButton.addActionListener(this);
        cargarPartidaButton.setEnabled(false); // Deshabilitado inicialmente
        backgroundPanel.add(cargarPartidaButton);

        // Botón para regresar al menú principal
        regresarMenuButton = new JButton("Regresar al Menú");
        regresarMenuButton.setBounds(50, 150, 200, 30);
        regresarMenuButton.addActionListener(this);
        backgroundPanel.add(regresarMenuButton);

        // Etiqueta para mostrar el estado
        estadoLabel = new JLabel();
        estadoLabel.setBounds(50, 200, 400, 30);
        backgroundPanel.add(estadoLabel);

        // Configuración de la ventana
        this.setTitle("Cargar Juego");
        this.setSize(300, 300);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Cerrar solo esta ventana
        this.setResizable(false);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == seleccionarArchivoButton) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileChooser.setFileFilter(new FileNameExtensionFilter("Archivos BIN", "bin"));
            int seleccion = fileChooser.showOpenDialog(this);
            if (seleccion == JFileChooser.APPROVE_OPTION) {
                archivoSeleccionado = fileChooser.getSelectedFile();
                estadoLabel.setText("Archivo seleccionado: " + archivoSeleccionado.getName());
                cargarPartidaButton.setEnabled(true); // Habilitar el botón de carga
            }
        } else if (ae.getSource() == cargarPartidaButton) {
            if (archivoSeleccionado != null) {
                // Aquí se implementaría la lógica para cargar la partida
                estadoLabel.setText("Partida cargada correctamente.");
            } else {
                estadoLabel.setText("Por favor, selecciona un archivo primero.");
            }
        } else if (ae.getSource() == regresarMenuButton) {
            // Cerrar esta ventana y regresar al menú principal
            this.dispose();
            new MenuUsuario();
        }
    }

    class ImagenFondo extends JPanel {
        private Image imagen;

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            // Cargar y dibujar la imagen de fondo
            imagen = new ImageIcon(getClass().getResource("backgroundCargarJuego.gif")).getImage();
            g.drawImage(imagen, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
