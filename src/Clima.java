import java.util.Random;

/**
 * Representa o sistema de clima da simulação.
 * O clima pode estar "Normal" ou "Chuvoso".
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
    
    private EstadoClima estadoAtual;
    
    private Random aleatorio;
    
    private int ciclosDesdeUltimaMudanca;
    
    private int ciclosParaMudanca;
    
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
        this(50);
    }
    
    /**
     * Atualiza o clima, possivelmente mudando de estado
     * Deve ser chamado a cada ciclo da simulação
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
     * Muda o estado do clima para o oposto do atual
     */
    private void mudarClima() {
        if (estadoAtual == EstadoClima.NORMAL) {
            estadoAtual = EstadoClima.CHUVOSO;
        } else {
            estadoAtual = EstadoClima.NORMAL;
        }
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
}
