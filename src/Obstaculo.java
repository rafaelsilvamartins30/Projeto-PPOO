/**
 * Enumeração que representa obstáculos fixos no campo de simulação.
 * <p>
 * Obstáculos são elementos imóveis do terreno que afetam o comportamento
 * dos animais e a dinâmica do ecossistema. Alguns obstáculos oferecem
 * recursos especiais, como possibilidade de pesca.
 * </p>
 * 
 * <p>
 * <strong>Tipos de Obstáculos:</strong>
 * </p>
 * <ul>
 * <li><strong>RIO:</strong> Corpo d'água onde ursos podem pescar</li>
 * <li><strong>PEDRA:</strong> Obstáculo sólido intransponível</li>
 * </ul>
 * 
 * <p>
 * <strong>Características dos Obstáculos:</strong>
 * </p>
 * <ul>
 * <li>São permanentes durante toda a simulação</li>
 * <li>Não podem ser ocupados por animais</li>
 * <li>São carregados de arquivos de mapa no início</li>
 * <li>Aparecem na visualização com cores específicas</li>
 * </ul>
 * 
 * <p>
 * <strong>Interação com Animais:</strong>
 * </p>
 * <ul>
 * <li>Animais terrestres não podem se mover para células com obstáculos</li>
 * <li>Ursos podem pescar em rios adjacentes</li>
 * <li>Obstáculos criam barreiras naturais no terreno</li>
 * </ul>
 * 
 * <p>
 * <strong>Extensibilidade:</strong>
 * </p>
 * <p>
 * Novos tipos de obstáculos podem ser adicionados facilmente ao enum,
 * com propriedades customizadas (ex: LAGO, MONTANHA, ARBUSTO).
 * </p>
 * 
 * @author Grupo 10
 * @version 1.0
 * @see Campo
 * @see Urso#pescar
 * @see CarregadorMapa
 */
public enum Obstaculo {

    /**
     * Rio - corpo d'água onde é possível pescar.
     * <p>
     * Características:
     * <ul>
     * <li>Permite pesca (contém peixes)</li>
     * <li>Cor de renderização: Ciano</li>
     * <li>Intransponível para animais terrestres</li>
     * <li>Fornece alimento para ursos</li>
     * </ul>
     * </p>
     */
    RIO(true),

    /**
     * Pedra - obstáculo sólido e inerte.
     * <p>
     * Características:
     * <ul>
     * <li>Não permite pesca</li>
     * <li>Cor de renderização: Cinza escuro</li>
     * <li>Intransponível para todos os animais</li>
     * <li>Serve apenas como barreira física</li>
     * </ul>
     * </p>
     */
    PEDRA(false);

    // ========== ATRIBUTOS ==========

    /**
     * Indica se este obstáculo contém recursos pesqueiros.
     * <p>
     * Se true, ursos adjacentes podem tentar pescar e obter alimento.
     * Se false, o obstáculo não oferece nenhum recurso.
     * </p>
     */
    private boolean contemPeixe;

    // ========== CONSTRUTOR ==========

    /**
     * Construtor privado do enum que define as características do obstáculo.
     * 
     * @param contemPeixe Se true, o obstáculo permite pesca;
     *                    se false, é apenas uma barreira física
     */
    Obstaculo(boolean contemPeixe) {
        this.contemPeixe = contemPeixe;
    }

    // ========== MÉTODOS DE CONSULTA ==========

    /**
     * Verifica se é possível pescar neste obstáculo.
     * <p>
     * Utilizado principalmente pela classe {@link Urso} para determinar
     * se pode tentar pescar em uma localização adjacente.
     * </p>
     * 
     * @return true se o obstáculo contém peixes e permite pesca,
     *         false caso contrário
     */
    public boolean podePescar() {
        return contemPeixe;
    }
}