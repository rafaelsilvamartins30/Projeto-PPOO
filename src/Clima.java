import java.util.Random;

/**
 * Classe que gerencia o sistema climático da simulação.
 * <p>
 * O clima afeta diretamente o crescimento da vegetação, criando
 * variabilidade ambiental que influencia toda a cadeia alimentar.
 * Mudanças climáticas ocorrem periodicamente com probabilidade configurada.
 * </p>
 * 
 * <p>
 * <strong>Estados Climáticos:</strong>
 * </p>
 * <ul>
 * <li><strong>NORMAL:</strong> Crescimento padrão de vegetação (1x por
 * turno)</li>
 * <li><strong>CHUVOSO:</strong> Crescimento acelerado de vegetação (2x por
 * turno)</li>
 * </ul>
 * 
 * <p>
 * <strong>Mecânica de Mudança:</strong>
 * </p>
 * <ol>
 * <li>Contador incrementa a cada turno de simulação</li>
 * <li>Ao atingir número configurável de ciclos, verifica mudança</li>
 * <li>50% de chance de alternar entre NORMAL ↔ CHUVOSO</li>
 * <li>Contador é resetado após mudança</li>
 * </ol>
 * 
 * <p>
 * <strong>Impacto Ecológico:</strong>
 * </p>
 * <p>
 * Chuva acelera crescimento vegetal, aumentando disponibilidade de alimento
 * para herbívoros. Isto pode desencadear:
 * <ul>
 * <li>Explosão populacional de herbívoros</li>
 * <li>Subsequente crescimento de predadores</li>
 * <li>Ciclos de abundância e escassez</li>
 * </ul>
 * </p>
 * 
 * @author Grupo 10
 * @version 1.0
 * @see Vegetacao
 * @see Campo
 * @see Simulador
 */
public class Clima {

    /**
     * Enumeração que define os possíveis estados climáticos da simulação.
     * <p>
     * Cada estado tem efeitos diferentes sobre o ecossistema,
     * especialmente no crescimento de vegetação.
     * </p>
     */
    public enum EstadoClima {
        /**
         * Estado climático normal com crescimento padrão de vegetação.
         */
        NORMAL,

        /**
         * Estado chuvoso com crescimento acelerado de vegetação (dobro).
         */
        CHUVOSO
    }

    // ========== ATRIBUTOS ==========

    /**
     * Estado climático atual da simulação.
     */
    private EstadoClima estadoAtual;

    /**
     * Gerador de números aleatórios para determinar mudanças climáticas.
     */
    private Random aleatorio;

    /**
     * Contador de ciclos desde a última mudança de clima.
     */
    private int ciclosDesdeUltimaMudanca;

    /**
     * Número de ciclos necessários antes de verificar possível mudança.
     */
    private int ciclosParaMudanca;

    /**
     * Probabilidade de mudança climática quando o contador atinge o limite.
     * <p>
     * Valor fixo de 50% cria equilíbrio entre estabilidade e variação.
     * </p>
     */
    private static final double PROBABILIDADE_MUDANCA = 0.5;

    // ========== CONSTRUTORES ==========

    /**
     * Cria um sistema de clima com período de verificação customizado.
     * <p>
     * O clima inicia sempre em estado NORMAL. Mudanças ocorrem apenas
     * após o período especificado, com 50% de probabilidade.
     * </p>
     * 
     * @param ciclosParaMudanca Número de turnos entre verificações de mudança
     *                          climática
     */
    public Clima(int ciclosParaMudanca) {
        this.aleatorio = new Random();
        this.estadoAtual = EstadoClima.NORMAL;
        this.ciclosParaMudanca = ciclosParaMudanca;
        this.ciclosDesdeUltimaMudanca = 0;
    }

    /**
     * Cria um sistema de clima com período padrão de 50 ciclos.
     * <p>
     * Construtor de conveniência que usa valor padrão balanceado
     * para frequência de mudanças climáticas.
     * </p>
     */
    public Clima() {
        this(50);
    }

    // ========== MÉTODOS DE ATUALIZAÇÃO ==========

    /**
     * Atualiza o estado climático, possivelmente mudando de estado.
     * <p>
     * <strong>Algoritmo:</strong>
     * <ol>
     * <li>Incrementa contador de ciclos</li>
     * <li>Se atingiu limite configurado:
     * <ul>
     * <li>Testa probabilidade de 50%</li>
     * <li>Se sucesso: alterna estado (NORMAL ↔ CHUVOSO)</li>
     * <li>Reseta contador</li>
     * </ul>
     * </li>
     * </ol>
     * </p>
     * <p>
     * <strong>Importante:</strong> Este método deve ser chamado uma vez
     * por turno de simulação para funcionamento correto.
     * </p>
     */
    public void atualizar() {
        ciclosDesdeUltimaMudanca++;

        if (ciclosDesdeUltimaMudanca >= ciclosParaMudanca) {
            if (aleatorio.nextDouble() <= PROBABILIDADE_MUDANCA) {
                mudarClima();
                ciclosDesdeUltimaMudanca = 0;
            }
        }
    }

    /**
     * Alterna o estado climático para o oposto do atual.
     * <p>
     * Transições possíveis:
     * <ul>
     * <li>NORMAL → CHUVOSO</li>
     * <li>CHUVOSO → NORMAL</li>
     * </ul>
     * </p>
     */
    private void mudarClima() {
        if (estadoAtual == EstadoClima.NORMAL) {
            estadoAtual = EstadoClima.CHUVOSO;
        } else {
            estadoAtual = EstadoClima.NORMAL;
        }
    }

    // ========== MÉTODOS DE CONSULTA ==========

    /**
     * Verifica se o clima está no estado chuvoso.
     * <p>
     * Usado pelo simulador para aplicar crescimento acelerado de vegetação.
     * </p>
     * 
     * @return true se estiver chovendo, false caso contrário
     */
    public boolean estaChuvoso() {
        return estadoAtual == EstadoClima.CHUVOSO;
    }

    /**
     * Verifica se o clima está no estado normal.
     * <p>
     * Útil para lógica condicional e validações.
     * </p>
     * 
     * @return true se estiver em estado normal, false caso contrário
     */
    public boolean estaNormal() {
        return estadoAtual == EstadoClima.NORMAL;
    }

    /**
     * Retorna quantos ciclos faltam para a próxima verificação de mudança.
     * <p>
     * Útil para interface de usuário e debug, mostrando quando
     * a próxima mudança climática pode ocorrer.
     * </p>
     * 
     * @return Número de ciclos restantes até próxima verificação
     */
    public int getCiclosRestantes() {
        return ciclosParaMudanca - ciclosDesdeUltimaMudanca;
    }

    // ========== MÉTODOS DE CONTROLE ==========

    /**
     * Reinicia o sistema climático para o estado inicial.
     * <p>
     * Restaura clima para NORMAL e reseta contador de ciclos.
     * Usado ao reiniciar a simulação.
     * </p>
     */
    public void reiniciar() {
        this.estadoAtual = EstadoClima.NORMAL;
        this.ciclosDesdeUltimaMudanca = 0;
    }

    // ========== MÉTODOS DE REPRESENTAÇÃO ==========

    /**
     * Retorna representação textual do estado climático atual.
     * <p>
     * Formato: {@code "Clima: NORMAL (próxima verificação em X ciclos)"}
     * </p>
     * <p>
     * Útil para logs, debug e exibição de informações ao usuário.
     * </p>
     * 
     * @return String descritiva do clima e tempo até próxima verificação
     */
    @Override
    public String toString() {
        return "Clima: " + estadoAtual +
                " (próxima verificação em " + getCiclosRestantes() + " ciclos)";
    }
}
