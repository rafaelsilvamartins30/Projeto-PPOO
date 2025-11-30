import java.util.Iterator;

/**
 * Interface que define operações de interação com o campo de simulação.
 * <p>
 * Esta interface complementa {@link GradeVisualizavel} ao fornecer operações
 * de modificação do campo, permitindo que atores (animais) interajam com
 * o ambiente. Implementa o padrão <strong>Interface Segregation Principle
 * (ISP)</strong>,
 * separando operações de leitura (GradeVisualizavel) das de escrita
 * (CampoInterativo).
 * </p>
 * 
 * <p>
 * <strong>Responsabilidades da Interface:</strong>
 * </p>
 * <ul>
 * <li>Busca de localizações livres para movimento</li>
 * <li>Navegação por células adjacentes</li>
 * <li>Consulta de objetos em posições específicas</li>
 * <li>Posicionamento de atores no campo</li>
 * <li>Remoção de atores (limpeza)</li>
 * <li>Interação com vegetação (alimentação de herbívoros)</li>
 * </ul>
 * 
 * <p>
 * <strong>Padrões de Design Aplicados:</strong>
 * </p>
 * <ul>
 * <li><strong>ISP:</strong> Interface focada em operações específicas</li>
 * <li><strong>Command:</strong> Operações encapsuladas em métodos</li>
 * <li><strong>Iterator:</strong> Navegação por células adjacentes</li>
 * </ul>
 * 
 * <p>
 * <strong>Uso Típico por Atores:</strong>
 * </p>
 * 
 * <pre>
 * // Herbívoro se alimentando
 * int alimento = campo.comerGrama(posicao);
 * 
 * // Predador buscando presa
 * Iterator&lt;Localizacao&gt; adj = campo.localizacoesAdjacentes(posicao);
 * 
 * // Animal se movendo
 * Localizacao nova = campo.localizacaoAdjacenteLivre(posicao);
 * if (nova != null) {
 *     campo.colocar(this, nova);
 * }
 * </pre>
 * 
 * <p>
 * <strong>Implementações:</strong>
 * </p>
 * <ul>
 * <li>{@link Campo} - Implementação principal do campo de simulação</li>
 * </ul>
 * 
 * @author Grupo 10
 * @version 1.0
 * @see GradeVisualizavel
 * @see Campo
 * @see Ator
 * @see Animal
 */
public interface CampoInterativo {

    // ========== MÉTODOS DE NAVEGAÇÃO ==========

    /**
     * Busca uma localização adjacente livre (sem obstáculos ou outros atores).
     * <p>
     * <strong>Algoritmo típico:</strong>
     * <ol>
     * <li>Obtém todas as localizações adjacentes</li>
     * <li>Embaralha para aleatoriedade</li>
     * <li>Retorna a primeira célula vazia encontrada</li>
     * </ol>
     * </p>
     * <p>
     * Usado principalmente para movimento de animais quando não há
     * objetivo específico (movimento exploratório).
     * </p>
     * 
     * @param localizacao Posição central a partir da qual buscar adjacentes
     * @return Localização livre adjacente, ou null se todas estiverem ocupadas
     */
    Localizacao localizacaoAdjacenteLivre(Localizacao localizacao);

    /**
     * Retorna um iterador sobre todas as localizações adjacentes.
     * <p>
     * <strong>Células adjacentes incluem:</strong>
     * <ul>
     * <li>8 células ao redor (vizinhança de Moore)</li>
     * <li>Apenas células dentro dos limites do campo</li>
     * <li>Tanto vazias quanto ocupadas</li>
     * </ul>
     * </p>
     * <p>
     * Usado para:
     * <ul>
     * <li>Busca de presas (predadores)</li>
     * <li>Busca de parceiros (reprodução)</li>
     * <li>Detecção de rios (pesca)</li>
     * </ul>
     * </p>
     * 
     * @param localizacao Posição central
     * @return Iterator sobre localizações adjacentes válidas
     */
    Iterator<Localizacao> localizacoesAdjacentes(Localizacao localizacao);

    // ========== MÉTODOS DE CONSULTA ==========

    /**
     * Retorna o objeto (ator ou obstáculo) em uma célula específica.
     * <p>
     * Sobrecarga que aceita coordenadas diretas ao invés de objeto Localização.
     * </p>
     * 
     * @param linha  Coordenada Y (índice da linha, 0-based)
     * @param coluna Coordenada X (índice da coluna, 0-based)
     * @return Objeto na posição especificada, ou null se vazia
     */
    Object getObjetoEm(int linha, int coluna);

    /**
     * Retorna o objeto (ator ou obstáculo) em uma localização específica.
     * <p>
     * <strong>Tipos de objetos retornados:</strong>
     * <ul>
     * <li>{@link Animal} - Predadores ou herbívoros</li>
     * <li>{@link Obstaculo} - Rios ou pedras</li>
     * <li>{@code null} - Célula vazia (pode conter vegetação)</li>
     * </ul>
     * </p>
     * 
     * @param localizacao Posição a ser consultada
     * @return Objeto na localização, ou null se vazia
     */
    Object getObjetoEm(Localizacao localizacao);

    // ========== MÉTODOS DE MODIFICAÇÃO ==========

    /**
     * Posiciona um ator ou obstáculo em uma localização específica do campo.
     * <p>
     * <strong>Importante:</strong> Este método não verifica se a célula
     * já está ocupada. É responsabilidade do chamador garantir que a
     * localização esteja livre ou que a sobrescrição seja intencional.
     * </p>
     * <p>
     * Usado para:
     * <ul>
     * <li>Movimento de animais</li>
     * <li>Nascimento de filhotes</li>
     * <li>Aplicação de obstáculos do mapa</li>
     * </ul>
     * </p>
     * 
     * @param ator        Objeto a ser posicionado (Animal, Obstaculo, etc.)
     * @param localizacao Posição onde o objeto será colocado
     */
    void colocar(Object ator, Localizacao localizacao);

    /**
     * Limpa todo o campo, removendo todos os atores.
     * <p>
     * <strong>Operações realizadas:</strong>
     * <ul>
     * <li>Remove todos os atores (animais) do campo</li>
     * <li>Mantém a estrutura do campo intacta</li>
     * <li>Vegetação pode ou não ser resetada (depende da implementação)</li>
     * </ul>
     * </p>
     * <p>
     * Usado principalmente ao:
     * <ul>
     * <li>Reiniciar a simulação</li>
     * <li>Preparar campo para novo passo (double buffering)</li>
     * </ul>
     * </p>
     */
    void limpar();

    // ========== MÉTODOS DE INTERAÇÃO COM AMBIENTE ==========

    /**
     * Tenta consumir vegetação em uma localização específica.
     * <p>
     * <strong>Comportamento esperado:</strong>
     * <ol>
     * <li>Verifica se há vegetação madura na localização</li>
     * <li>Se sim: consome a vegetação e retorna valor nutricional</li>
     * <li>Se não: retorna 0 (sem efeito)</li>
     * <li>Vegetação consumida volta ao estado inicial de crescimento</li>
     * </ol>
     * </p>
     * <p>
     * Usado exclusivamente por herbívoros para se alimentar.
     * </p>
     * 
     * @param localizacao Posição onde tentar se alimentar
     * @return Valor nutricional obtido (0 se não havia vegetação madura)
     */
    int comerGrama(Localizacao localizacao);
}
