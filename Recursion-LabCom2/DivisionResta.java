public class DivisionResta {
    public static void main(String[] args) throws Exception {
        int dividendo = 25;
        int divisor = 0;

        System.out.print("\n");

        // Enfoque recursivo
        int resultadoRecursivo = division(dividendo, divisor);
        System.out.println(">>> Enfoque recursivo: " + resultadoRecursivo + "\n");

        // Enfoque iterativo
        int resultadoIterativo = division(dividendo, divisor);
        System.out.println(">>> Enfoque iterativo: " + resultadoIterativo + "\n");
    }

    // Enfoque recursivo
    public static int division(int dividendo, int divisor) {
        return divisionRecursiva(dividendo, divisor);
    }

    private static int divisionRecursiva(int dividendo, int divisor) {
        if (divisor == 0) {
            System.out.println("xxx El divisor no puede ser cero.");
            return 0;
        }

        if (dividendo < divisor) {
            return 0;
        } else {
            return 1 + divisionRecursiva(dividendo - divisor, divisor);
        }
    }

    // Enfoque iterativo con restas sucesivas
    public static int division(int dividendo, int divisor, boolean iteracion) {
        if (divisor == 0) {
            System.out.println("xxx El divisor no puede ser cero.");
            return 0;
        }

        if (iteracion) {
            int cociente = 0;
            while (dividendo >= divisor) {
                dividendo -= divisor;
                cociente++;
            }
            return cociente;
        } else {
            return divisionRecursiva(dividendo, divisor);
        }
    }
}
