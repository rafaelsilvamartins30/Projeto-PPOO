import java.util.Random;

/**
 * Representa o sistema de clima da simulação.
 * O clima pode estar "Normal" ou "Chuvoso" e afeta o comportamento dos animais.
 * 
 * O clima muda aleatoriamente após um número configurável de ciclos.
 */
public class Clima {
    
    /**
     * Enumeração dos possíveis estados climáticos
     */
    public enum EstadoClima {
        NORMAL,
        CHUVOSO
    }
    
    // Estado atual do clima
    private EstadoClima estadoAtual;
    
    // Gerador de números aleatórios
    private Random aleatorio;
    
    // Contador de ciclos desde a última mudança de clima
    private int ciclosDesdeUltimaMudanca;
    
    // Número de ciclos necessários para possível mudança de clima
    private int ciclosParaMudanca;
    
    // Probabilidade de mudança quando atinge o número de ciclos (0.0 a 1.0)
    private static final double PROBABILIDADE_MUDANCA = 0.5;
    
    /**
     * Construtor padrão que inicia com clima Normal
     * @param ciclosParaMudanca Número de ciclos entre possíveis mudanças de clima
     */
    public Clima(int ciclosParaMudanca) {
        this.aleatorio = new Random();
        this.estadoAtual = EstadoClima.NORMAL;
        this.ciclosParaMudanca = ciclosParaMudanca;
        this.ciclosDesdeUltimaMudanca = 0;
    }
    
    /**
     * Construtor alternativo com valor padrão de 50 ciclos
     */
    public Clima() {
        this(50); // Por padrão, verifica mudança a cada 50 ciclos
    }
    
    /**
     * Atualiza o clima, possivelmente mudando de estado
     * Deve ser chamado a cada ciclo da simulação
     */
    public void atualizar() {
        ciclosDesdeUltimaMudanca++;
        
        // Verifica se chegou o momento de tentar mudar o clima
        if (ciclosDesdeUltimaMudanca >= ciclosParaMudanca) {
            // Tenta mudar o clima com base na probabilidade
            if (aleatorio.nextDouble() <= PROBABILIDADE_MUDANCA) {
                mudarClima();
                ciclosDesdeUltimaMudanca = 0; // Reinicia o contador
            }
        }
    }
    
    /**
     * Muda o estado do clima para o oposto do atual
     */
    private void mudarClima() {
        if (estadoAtual == EstadoClima.NORMAL) {
            estadoAtual = EstadoClima.CHUVOSO;
            System.out.println("☔ O clima mudou para CHUVOSO!");
        } else {
            estadoAtual = EstadoClima.NORMAL;
            System.out.println("☀️ O clima mudou para NORMAL!");
        }
    }
    
    /**
     * Retorna o estado atual do clima
     * @return Estado atual (NORMAL ou CHUVOSO)
     */
    public EstadoClima getEstadoAtual() {
        return estadoAtual;
    }
    
    /**
     * Verifica se o clima está chuvoso
     * @return true se estiver chuvoso, false caso contrário
     */
    public boolean estaChuvoso() {
        return estadoAtual == EstadoClima.CHUVOSO;
    }
    
    /**
     * Verifica se o clima está normal
     * @return true se estiver normal, false caso contrário
     */
    public boolean estaNormal() {
        return estadoAtual == EstadoClima.NORMAL;
    }
    
    /**
     * Reinicia o clima para o estado inicial (Normal)
     */
    public void reiniciar() {
        this.estadoAtual = EstadoClima.NORMAL;
        this.ciclosDesdeUltimaMudanca = 0;
        System.out.println("🌤️ Clima reiniciado para NORMAL");
    }
    
    /**
     * Define manualmente o número de ciclos para mudança
     * @param ciclos Número de ciclos
     */
    public void setCiclosParaMudanca(int ciclos) {
        if (ciclos > 0) {
            this.ciclosParaMudanca = ciclos;
        }
    }
    
    /**
     * Retorna quantos ciclos faltam para a próxima verificação de mudança
     * @return Número de ciclos restantes
     */
    public int getCiclosRestantes() {
        return ciclosParaMudanca - ciclosDesdeUltimaMudanca;
    }
    
    /**
     * Retorna uma descrição textual do clima atual
     * @return String com o estado do clima
     */
    @Override
    public String toString() {
        return "Clima: " + estadoAtual + 
               " (próxima verificação em " + getCiclosRestantes() + " ciclos)";
    }
    
    /**
     * Retorna o fator multiplicador de reprodução baseado no clima
     * Pode ser usado para aumentar a taxa de reprodução em clima chuvoso
     * @return 1.0 para normal, valor maior para chuvoso
     */
    public double getFatorReproducao() {
        return estaChuvoso() ? 1.3 : 1.0; // 30% mais reprodução na chuva
    }
    
    /**
     * Retorna o fator de crescimento de plantas (afeta disponibilidade de comida)
     * @return 1.0 para normal, valor maior para chuvoso
     */
    public double getFatorCrescimentoPlantas() {
        return estaChuvoso() ? 1.5 : 1.0; // 50% mais plantas na chuva
    }
}
