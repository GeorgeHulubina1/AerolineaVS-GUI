package com.aerolineavs.tarifas;

import javax.swing.SwingUtilities;
import java.util.Scanner;

/**
 * Punto de entrada de la aplicación.
 */
public final class App {

    private App() {
    }

    /**
     * Punto de entrada principal.
     *
     * @param args usa --cli para ejecutar la versión de consola
     */
    public static void main(String[] args) {
        if (args.length > 0 && "--cli".equalsIgnoreCase(args[0])) {
            ejecutarCli();
            return;
        }

        SwingUtilities.invokeLater(() -> {
            TarifaFrame frame = new TarifaFrame();
            frame.setVisible(true);
        });
    }

    private static void ejecutarCli() {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("=== Evaluador de Tarifas Aerolínea ===");

            System.out.print("Edad: ");
            int edad = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("Vuelos al año: ");
            int vuelos = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("Tipo de viajero (MENOR, ESTUDIANTE_UNIVERSITARIO_DESPLAZADO, TRABAJADOR_JOVEN, GENERAL): ");
            TipoViajero tipo = TipoViajero.valueOf(scanner.nextLine().trim().toUpperCase());

            System.out.print("Clase preferida (TURISTA, BUSINESS): ");
            ClaseVuelo clase = ClaseVuelo.valueOf(scanner.nextLine().trim().toUpperCase());

            System.out.print("Destino preferido (EUROPA, ASIA, AMERICA, OTRA): ");
            RegionDestino destino = RegionDestino.valueOf(scanner.nextLine().trim().toUpperCase());

            System.out.print("Ingresos anuales (€): ");
            double ingresos = Double.parseDouble(scanner.nextLine().trim());

            System.out.print("¿Viaja con niños menores de 12? (s/n): ");
            boolean conNinos = scanner.nextLine().trim().equalsIgnoreCase("s");

            System.out.print("¿Vive con sus padres? (s/n): ");
            boolean viveConPadres = scanner.nextLine().trim().equalsIgnoreCase("s");

            ClientePotencial cliente = new ClientePotencial(
                    edad, vuelos, tipo, clase, destino, ingresos, conNinos, viveConPadres
            );

            ResultadoTarifa resultado = EvaluadorTarifas.evaluar(cliente);

            System.out.println("Tarifa recomendada: " + resultado.tarifa().getNombre());
            System.out.println("Descuento: " + resultado.tarifa().getDescuentoPorcentaje() + "%");
            System.out.println(resultado.suposiciones());
        } catch (NumberFormatException ex) {
            System.err.println("Entrada numérica no válida: " + ex.getMessage());
            System.exit(1);
        } catch (IllegalArgumentException ex) {
            System.err.println("Entrada no válida. Revise formatos y valores permitidos:");
            System.err.println("- Tipo de viajero: MENOR, ESTUDIANTE_UNIVERSITARIO_DESPLAZADO, TRABAJADOR_JOVEN, GENERAL");
            System.err.println("- Clase preferida: TURISTA, BUSINESS");
            System.err.println("- Destino preferido: EUROPA, ASIA, AMERICA, OTRA");
            System.err.println("- Preguntas sí/no: responda 's' o 'n'");
            System.err.println("Detalle: " + ex.getMessage());
            System.exit(1);
        }
    }
}
