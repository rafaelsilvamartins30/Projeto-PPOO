public interface GradeVisualizavel {
    /**
     * Retorna a profundidade (número de linhas) da grade.
     * @return A profundidade da grade.
     */
    int getProfundidade();
    /**
     * Retorna a largura (número de colunas) da grade.
     * @return A largura da grade.
     */   
    int getLargura();
    /**
     * Retorna o objeto presente em uma localização específica da grade.
     * @param linha A coordenada vertical da localização.
     * @param coluna A coordenada horizontal da localização.
     * @return O objeto na localização especificada, ou null se estiver vazia.
     */
    Object getObjetoEm(int linha, int coluna);
    /**
     * Indica se há grama madura na localização especificada.
     * @param linha A coordenada vertical da localização.
     * @param coluna A coordenada horizontal da localização.
     * @return true se houver grama madura, false caso contrário.
     */
    boolean temGramaMadura(int linha, int coluna);
}