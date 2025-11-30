import java.util.Iterator;

public interface CampoInterativo {
    /**
     * Retorna o objeto localizado em uma dada localização.
     * @param localizacao A localização a ser verificada.
     * @return O objeto na localização, ou null se estiver vazia.
     */
    Localizacao localizacaoAdjacenteLivre(Localizacao localizacao);

    /**
     * Retorna um iterador sobre as localizações adjacentes a uma dada localização.
     * @param localizacao A localização central.
     * @return Um iterador sobre as localizações adjacentes.
     */
    Iterator<Localizacao> localizacoesAdjacentes(Localizacao localizacao);

    /**
     * Retorna o objeto (ator) em uma localização específica.
     * @param linha A coordenada vertical da localização.
     * @param coluna A coordenada horizontal da localização.
     * @return O objeto na localização, ou null se estiver vazia.
     */
    Object getObjetoEm(int linha, int coluna);

    /**
     * Retorna o objeto (ator) em uma localização específica.
     * @param localizacao A localização a ser verificada.
     * @return O objeto na localização, ou null se estiver vazia.
     */
    Object getObjetoEm(Localizacao localizacao);
    
    /**
     * Coloca um ator em uma localização específica.
     * @param ator O ator a ser colocado.
     * @param localizacao A localização onde colocar o ator.
     */
    void colocar(Object ator, Localizacao localizacao);

    /**
     * Limpa a localização especificada, removendo qualquer ator presente.
     * @param localizacao A localização a ser limpa.
     */
    void limpar();

    /**
     * Tenta comer grama na localização especificada.
     * @param localizacao A localização onde tentar comer grama.
     * @return A quantidade de grama comida (pode ser zero).
     */
    int comerGrama(Localizacao localizacao);
}
