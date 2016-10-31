package Receptor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

public class Receptor extends JFrame {

    Vector<String> datosRecibidos = new Vector<String>();

    JTextField pantalla, pantalla2, pantalla3;

    JButton boton1;

    String operacion;

    JPanel panelNumeros, panelOperaciones;

    boolean nuevaOperacion = true;

    public Receptor() {

        setSize(600, 400);
        setTitle("SOCKET");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(true);

        JPanel panel = (JPanel) this.getContentPane();
        panel.setLayout(new BorderLayout());

        pantalla = new JTextField("SERVIDOR", 20);
        pantalla.setBorder(new EmptyBorder(4, 4, 4, 4));
        pantalla.setFont(new Font("Arial", Font.BOLD, 25));
        pantalla.setHorizontalAlignment(JTextField.LEFT);
        pantalla.setEditable(false);
        pantalla.setBackground(Color.WHITE);
        panel.add("North", pantalla);

        pantalla2 = new JTextField("ESPERANDO CONEXION....", 20);
        pantalla2.setBorder(new EmptyBorder(4, 4, 4, 4));
        pantalla2.setFont(new Font("Arial", Font.BOLD, 25));
        pantalla2.setHorizontalAlignment(JTextField.LEFT);
        pantalla2.setEditable(false);
        pantalla2.setBackground(Color.WHITE);
        panel.add("South", pantalla2);

        panelOperaciones = new JPanel();
        panelOperaciones.setLayout(new GridLayout(6, 1));
        panelOperaciones.setBorder(new EmptyBorder(4, 4, 4, 4));
        nuevoBotonOperacion("AceptarConexion");

        panel.add("East", panelOperaciones);
        validate();
    }

    private void nuevoBotonOperacion(String operacion) {
        JButton btn = new JButton(operacion);
        btn.setForeground(Color.RED);

        btn.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseReleased(MouseEvent evt) {
                JButton btn = (JButton) evt.getSource();
                operacionPulsado(btn.getText());
            }
        });

        panelOperaciones.add(btn);
    }

    private void operacionPulsado(String tecla) {
        if (tecla.equals("AceptarConexion")) {
            calcularResultado();
        }

        nuevaOperacion = true;
    }

    private void calcularResultado() {
        if (operacion.equals("AceptarConexion")) {
            try {
                ServerSocket s = new ServerSocket(1234);

                while (true) {
                    Socket cliente = s.accept();
                    BufferedReader entrada = new BufferedReader(
                            new InputStreamReader(cliente.getInputStream()));
                    PrintWriter salida = new PrintWriter(
                            new OutputStreamWriter(cliente.getOutputStream()), true);
                    String datos = entrada.readLine();
                    if (datos.equals("DATOS")) {
                        for (int n = 0; n < datosRecibidos.size(); n++) {
                            salida.println(datosRecibidos.get(n));
                        }
                    } else {
                        datosRecibidos.add(0, datos);
                        pantalla2.setText("Conexion Aceptada");
                    }
                    cliente.close();
                }
            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }

}
