/**
 * Interface que define o contrato para acesso somente leitura a uma grade de
 * simulação.
 * <p>
 * Esta interface é utilizada pela camada de visualização para consultar o
 * estado
 * do campo sem permitir modificações. Implementa o princípio de segregação de
 * interfaces (ISP), expondo apenas operações de leitura necessárias para
 * renderização.
 * </p>
 * 
 * <p>
 * <strong>Propósito:</strong>
 * </p>
 * <ul>
 * <li>Desacoplar a visualização da implementação concreta do campo</li>
 * <li>Prevenir modificações acidentais do estado durante renderização</li>
 * <li>Facilitar testes com implementações mock</li>
 * <li>Permitir múltiplas implementações de campo</li>
 * </ul>
 * 
 * <p>
 * <strong>Padrão de Design:</strong>
 * </p>
 * <p>
 * Implementa o padrão <strong>Interface Segregation Principle (ISP)</strong>
 * e <strong>Dependency Inversion Principle (DIP)</strong>, onde classes de alto
 * nível
 * (visualização) dependem de abstrações, não de implementações concretas.
 * </p>
 * 
 * <p>
 * <strong>Implementações:</strong>
 * </p>
 * <ul>
 * <li>{@link Campo} - Implementação principal do campo de simulação</li>
 * </ul>
 * 
 * <p>
 * <strong>Uso Típico:</strong>
 * </p>
 * 
 * <pre>
 * GradeVisualizavel grade = campo;
 * for (int i = 0; i &lt; grade.getProfundidade(); i++) {
 *     for (int j = 0; j &lt; grade.getLargura(); j++) {
 *         Object obj = grade.getObjetoEm(i, j);
 *         boolean grama = grade.temGramaMadura(i, j);
 *         // renderizar célula...
 *     }
 * }
 * </pre>
 * 
 * @author Grupo 10
 * @version 1.0
 * @see Campo
 * @see Desenhavel
 * @see VisualizacaoSimulador
 */
public interface GradeVisualizavel {

    // ========== MÉTODOS DE DIMENSÕES ==========

    /**
     * Retorna a profundidade (altura) da grade em células.
     * <p>
     * A profundidade representa o número de linhas da grade,
     * correspondendo à dimensão vertical (eixo Y).
     * </p>
     * 
     * @return Número de linhas da grade (profundidade)
     */
    int getProfundidade();

    /**
     * Retorna a largura da grade em células.
     * <p>
     * A largura representa o número de colunas da grade,
     * correspondendo à dimensão horizontal (eixo X).
     * </p>
     * 
     * @return Número de colunas da grade (largura)
     */
    int getLargura();

    // ========== MÉTODOS DE CONSULTA ==========

    /**
     * Retorna o objeto (ator ou obstáculo) presente em uma célula específica.
     * <p>
     * <strong>Tipos de objetos retornados:</strong>
     * <ul>
     * <li>{@link Animal} - Predadores ou herbívoros</li>
     * <li>{@link Obstaculo} - Rios ou pedras</li>
     * <li>{@code null} - Célula vazia (pode conter apenas grama)</li>
     * </ul>
     * </p>
     * <p>
     * <strong>Nota:</strong> Vegetação (grama) não é retornada por este método.
     * Use {@link #temGramaMadura(int, int)} para verificar o estado da grama.
     * </p>
     * 
     * @param linha  Coordenada Y (índice da linha, 0-based)
     * @param coluna Coordenada X (índice da coluna, 0-based)
     * @return Objeto na localização especificada, ou null se vazia
     */
    Object getObjetoEm(int linha, int coluna);

    /**
     * Verifica se há grama madura (totalmente crescida) na célula especificada.
     * <p>
     * Grama madura é importante para:
     * <ul>
     * <li>Renderização visual (cor diferente de grama nova)</li>
     * <li>Determinar se herbívoros podem se alimentar</li>
     * <li>Cálculo de recursos disponíveis no ecossistema</li>
     * </ul>
     * </p>
     * <p>
     * <strong>Estados da grama:</strong>
     * <ul>
     * <li><strong>Madura:</strong> Nível de crescimento = MAX_CRESCIMENTO</li>
     * <li><strong>Em crescimento:</strong> 0 &lt; nível &lt; MAX_CRESCIMENTO</li>
     * <li><strong>Consumida:</strong> Nível = 0 (recém comida)</li>
     * </ul>
     * </p>
     * 
     * @param linha  Coordenada Y (índice da linha, 0-based)
     * @param coluna Coordenada X (índice da coluna, 0-based)
     * @return true se há grama madura na localização, false caso contrário
     */
    boolean temGramaMadura(int linha, int coluna);
}