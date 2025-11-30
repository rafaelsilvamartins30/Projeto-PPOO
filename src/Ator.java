import java.util.List;

/**
 * Interface que define o contrato básico para todos os atores da simulação.
 * <p>
 * Um ator é qualquer entidade que pode realizar ações durante a simulação.
 * Esta interface implementa o padrão <strong>Strategy</strong>, permitindo que
 * diferentes tipos de atores tenham comportamentos distintos enquanto são
 * tratados de forma polimórfica pelo simulador.
 * </p>
 * 
 * <p>
 * <strong>Características de um Ator:</strong>
 * </p>
 * <ul>
 * <li>Possui ciclo de vida (pode estar vivo ou morto)</li>
 * <li>Realiza ações a cada turno da simulação</li>
 * <li>Interage com o campo e outros atores</li>
 * <li>Pode gerar novos atores (reprodução)</li>
 * </ul>
 * 
 * <p>
 * <strong>Implementações Típicas:</strong>
 * </p>
 * <ul>
 * <li>{@link Animal} - Classe abstrata para animais (predadores e
 * herbívoros)</li>
 * <li>{@link Predador} - Animais que caçam outros animais</li>
 * <li>{@link Herbivoro} - Animais que se alimentam de vegetação</li>
 * </ul>
 * 
 * <p>
 * <strong>Ciclo de Vida Típico:</strong>
 * </p>
 * <ol>
 * <li>Ator é criado e adicionado à lista de atores</li>
 * <li>A cada turno: {@link #agir} é chamado pelo simulador</li>
 * <li>Ator executa suas ações (mover, alimentar, reproduzir)</li>
 * <li>Ator pode morrer (idade, fome, predação)</li>
 * <li>Se {@link #estaVivo} retorna false, é removido da simulação</li>
 * </ol>
 * 
 * <p>
 * <strong>Padrões de Design:</strong>
 * </p>
 * <ul>
 * <li><strong>Strategy:</strong> Diferentes comportamentos de ação</li>
 * <li><strong>Template Method:</strong> Sequência de ações definida em
 * subclasses</li>
 * <li><strong>Polymorphism:</strong> Tratamento uniforme de diferentes
 * atores</li>
 * </ul>
 * 
 * @author Grupo 10
 * @version 1.0
 * @see Animal
 * @see Predador
 * @see Herbivoro
 * @see Simulador
 */
public interface Ator {

    // ========== MÉTODOS DE AÇÃO ==========

    /**
     * Define o comportamento do ator durante um turno de simulação.
     * <p>
     * Este método é chamado uma vez por turno para cada ator vivo.
     * Implementações típicas incluem:
     * <ul>
     * <li>Envelhecimento e verificação de morte por idade</li>
     * <li>Consumo de energia (fome) e verificação de morte por inanição</li>
     * <li>Busca e consumo de alimento</li>
     * <li>Tentativa de reprodução</li>
     * <li>Movimento para nova localização</li>
     * </ul>
     * </p>
     * 
     * <p>
     * <strong>Padrão Double Buffering:</strong>
     * </p>
     * <p>
     * O simulador utiliza dois campos alternados:
     * <ul>
     * <li><strong>campoAtual:</strong> Estado presente (apenas leitura)</li>
     * <li><strong>campoAtualizado:</strong> Próximo estado (escrita)</li>
     * </ul>
     * Isto evita conflitos quando múltiplos atores se movem simultaneamente.
     * </p>
     * 
     * <p>
     * <strong>Importante:</strong>
     * </p>
     * <ul>
     * <li>Ler dados do {@code campoAtual}</li>
     * <li>Escrever mudanças no {@code campoAtualizado}</li>
     * <li>Adicionar filhotes à {@code lista} fornecida</li>
     * <li>Não modificar diretamente o {@code campoAtual}</li>
     * </ul>
     * 
     * @param campoAtual      Campo no estado atual (somente leitura)
     * @param campoAtualizado Campo sendo construído para o próximo turno (escrita)
     * @param lista           Lista onde novos atores nascidos devem ser adicionados
     */
    void agir(CampoInterativo campoAtual, CampoInterativo campoAtualizado, List<Ator> lista);

    // ========== MÉTODOS DE ESTADO ==========

    /**
     * Verifica se o ator ainda está vivo na simulação.
     * <p>
     * Um ator pode morrer por diversos motivos:
     * <ul>
     * <li>Velhice (idade máxima atingida)</li>
     * <li>Inanição (nível de alimento chegou a zero)</li>
     * <li>Predação (foi consumido por outro animal)</li>
     * </ul>
     * </p>
     * <p>
     * O simulador remove automaticamente atores mortos da lista
     * de atores ativos ao final de cada turno.
     * </p>
     * 
     * @return true se o ator está vivo e deve continuar na simulação,
     *         false se está morto e deve ser removido
     */
    boolean estaVivo();
}
