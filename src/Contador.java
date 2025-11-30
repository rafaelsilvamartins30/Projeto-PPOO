/**
 * Classe auxiliar que mantém um contador nomeado para rastreamento de
 * quantidades.
 * <p>
 * Esta classe é utilizada pelo sistema de estatísticas para contar ocorrências
 * de diferentes tipos de entidades na simulação, associando um nome legível
 * a um valor numérico.
 * </p>
 * 
 * <p>
 * <strong>Características:</strong>
 * </p>
 * <ul>
 * <li>Armazena um identificador textual (nome da entidade)</li>
 * <li>Mantém um contador inteiro incrementável</li>
 * <li>Suporta reset para reutilização em múltiplos ciclos</li>
 * <li>Imutável quanto ao nome após criação</li>
 * </ul>
 * 
 * <p>
 * <strong>Uso Típico:</strong>
 * </p>
 * 
 * <pre>
 * Contador raposas = new Contador("Raposa");
 * raposas.incrementar(); // contagem = 1
 * raposas.incrementar(); // contagem = 2
 * System.out.println(raposas.getNome() + ": " + raposas.getContagem());
 * // Output: "Raposa: 2"
 * raposas.reiniciar(); // contagem = 0
 * </pre>
 * 
 * <p>
 * <strong>Contexto de Uso:</strong>
 * </p>
 * <p>
 * Utilizado principalmente por {@link EstatisticasCampo} para manter
 * contadores individuais para cada tipo de entidade encontrada no campo.
 * </p>
 * 
 * @author David J. Barnes e Michael Kolling
 * @author Grupo 10
 * @version 2002-04-23 (traduzido e expandido)
 * @see EstatisticasCampo
 */
public class Contador {
    // ========== ATRIBUTOS ==========

    /**
     * Identificador textual do que está sendo contado.
     * <p>
     * Geralmente o nome simples da classe (ex: "Raposa", "Coelho").
     * Imutável após criação.
     * </p>
     */
    private String nome;

    /**
     * Valor atual do contador.
     * <p>
     * Incrementado via {@link #incrementar()} e resetado via {@link #reiniciar()}.
     * </p>
     */
    private int contagem;

    // ========== CONSTRUTOR ==========

    /**
     * Cria um novo contador com o nome especificado.
     * <p>
     * O contador é inicializado com valor zero e pode ser incrementado
     * posteriormente conforme necessário.
     * </p>
     * 
     * @param nome Identificador textual do que será contado (ex: "Raposa")
     */
    public Contador(String nome) {
        this.nome = nome;
        contagem = 0;
    }

    // ========== GETTERS ==========

    /**
     * Retorna o identificador textual associado a este contador.
     * <p>
     * Útil para gerar relatórios e legendas na interface gráfica.
     * </p>
     * 
     * @return Nome do tipo sendo contado
     */
    public String getNome() {
        return nome;
    }

    /**
     * Retorna o valor atual do contador.
     * <p>
     * Representa quantas vezes {@link #incrementar()} foi chamado
     * desde a criação ou desde o último {@link #reiniciar()}.
     * </p>
     * 
     * @return Contagem atual (número não-negativo)
     */
    public int getContagem() {
        return contagem;
    }

    // ========== MÉTODOS DE MODIFICAÇÃO ==========

    /**
     * Incrementa o contador em uma unidade.
     * <p>
     * Chamado tipicamente ao encontrar uma nova instância da entidade
     * sendo rastreada durante a varredura do campo.
     * </p>
     */
    public void incrementar() {
        contagem++;
    }

    /**
     * Reseta o contador para zero.
     * <p>
     * Utilizado no início de cada ciclo de contagem para limpar
     * valores do passo anterior, permitindo reutilização do mesmo
     * objeto Contador em múltiplos passos da simulação.
     * </p>
     */
    public void reiniciar() {
        contagem = 0;
    }
}