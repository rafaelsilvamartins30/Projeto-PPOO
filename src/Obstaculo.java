/**
 * Modelo de Obstáculos no campo.
 * Alguns podem conter peixes.
 */
public enum Obstaculo {
    RIO(true),   // Tem peixe
    PEDRA(false); // Não tem peixe

    private boolean contemPeixe;

    Obstaculo(boolean contemPeixe) {
        this.contemPeixe = contemPeixe;
    }

    public boolean podePescar() {
        return contemPeixe;
    }
}