import java.util.List;

public interface Ator {

    /**
     * Define o que o ator faz durante um passo de tempo na simulação.
     * Cada subclasse deve implementar este método.
     * 
     * @param campoAtual O campo atual onde o ator está localizado.
     * @param campoAtualizado O campo onde o ator deve se mover.
     * @param lista A lista para adicionar novos atores nascidos.
     */
    void agir(Campo campoAtual, Campo campoAtualizado, List<Ator> lista);

    /**
     * Verifica se o ator está vivo.
     * 
     * @return verdadeiro se o ator estiver vivo, falso caso contrário.
     */
    boolean estaVivo();
}
