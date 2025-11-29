/**
 * Modelo de Obstáculos no campo.
 * Alguns podem conter peixes.
 */
public enum Obstaculo {
    RIO(true),   // Tem peixe
    PEDRA(false); // Não tem peixe

    private boolean contemPeixe;

    /**
     * Construtor do Obstáculo.
     * 
     * @param contemPeixe Se o obstáculo contém peixes.
     */
    Obstaculo(boolean contemPeixe) {
        this.contemPeixe = contemPeixe;
    }

    /**
     * Verifica se é possível pescar neste obstáculo.
     * 
     * @return verdadeiro se for possível pescar, falso caso contrário.
     */
    public boolean podePescar() {
        return contemPeixe;
    }
}