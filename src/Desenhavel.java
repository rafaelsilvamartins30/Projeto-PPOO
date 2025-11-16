import java.awt.Color;
/**
 * Interface para visualizações gráficas da simulação.
 * Permite definir cores para diferentes classes de animais,
 * mostrar o status atual do campo, verificar a viabilidade da simulação
 * e reiniciar a visualização.
 */
public interface Desenhavel {
    void definirCor(Class<?> classeAnimal, Color cor);
    void mostrarStatus(int passo, Campo campo);
    boolean ehViavel(Campo campo);
    void reiniciar();

}
