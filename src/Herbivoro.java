import java.util.List;

/**
 * Classe abstrata que representa herbívoros no ecossistema de simulação.
 * <p>
 * Esta classe implementa o comportamento base de todos os herbívoros,
 * incluindo alimentação por vegetação e movimento exploratório. Cada herbívoro
 * específico (Coelho, Rato) herda desta classe e define seus próprios
 * parâmetros de vida e reprodução.
 * </p>
 * 
 * <p>
 * <strong>Características Comuns de Herbívoros:</strong>
 * </p>
 * <ul>
 * <li>Alimentam-se exclusivamente de vegetação (grama madura)</li>
 * <li>Movem-se aleatoriamente pelo campo em busca de alimento</li>
 * <li>Morrem de fome se não conseguirem se alimentar</li>
 * <li>São presas naturais de predadores</li>
 * <li>Reproduzem-se mais rapidamente que predadores</li>
 * </ul>
 * 
 * <p>
 * <strong>Mecânica de Alimentação:</strong>
 * </p>
 * <ol>
 * <li>Tenta comer grama na sua localização atual</li>
 * <li>Se a grama estiver madura, consome e ganha nutrição</li>
 * <li>A grama consumida volta ao estado inicial de crescimento</li>
 * <li>O nível de alimento é limitado a um máximo configurável</li>
 * </ol>
 * 
 * <p>
 * <strong>Ciclo de Vida:</strong>
 * </p>
 * <p>
 * A cada turno, o herbívoro:
 * <ol>
 * <li>Envelhece (pode morrer de velhice)</li>
 * <li>Sente fome (perde energia, pode morrer de inanição)</li>
 * <li>Tenta se alimentar na posição atual</li>
 * <li>Tenta se reproduzir se houver parceiro próximo</li>
 * <li>Move-se para uma célula adjacente livre</li>
 * </ol>
 * </p>
 * 
 * <p>
 * <strong>Papel Ecológico:</strong>
 * </p>
 * <p>
 * Herbívoros são consumidores primários na cadeia alimentar, convertendo
 * energia vegetal em biomassa animal. Servem como base da pirâmide
 * alimentar, sustentando populações de predadores.
 * </p>
 * 
 * @author Grupo 10
 * @version 1.0
 * @see Animal
 * @see Vegetacao
 * @see Coelho
 * @see Rato
 */
public abstract class Herbivoro extends Animal {

    // ========== CONSTRUTOR ==========

    /**
     * Cria uma nova instância de herbívoro.
     * <p>
     * Inicializa as características básicas herdadas de {@link Animal},
     * incluindo idade, localização e nível de alimento inicial.
     * </p>
     * 
     * @param idadeAleatoria Se true, a idade será aleatória entre 0 e idade máxima;
     *                       se false, a idade será zero (recém-nascido)
     */
    public Herbivoro(boolean idadeAleatoria) {
        super(idadeAleatoria);
    }

    // ========== MÉTODOS DE AÇÃO ==========

    /**
     * Executa o ciclo completo de ação do herbívoro em um turno.
     * <p>
     * <strong>Sequência de operações:</strong>
     * <ol>
     * <li>Incrementa idade (envelhecimento natural)</li>
     * <li>Incrementa fome (gasto de energia)</li>
     * <li>Se ainda vivo: tenta se alimentar de grama</li>
     * <li>Se ainda vivo: tenta se reproduzir</li>
     * <li>Se ainda vivo: move-se para célula adjacente</li>
     * </ol>
     * </p>
     * <p>
     * O herbívoro pode morrer de fome ou velhice durante este processo,
     * caso em que as etapas seguintes não são executadas.
     * </p>
     * 
     * @param campoAtual      Campo no estado atual (leitura)
     * @param campoAtualizado Campo sendo construído para o próximo passo
     * @param novosHerbivoros Lista onde filhotes nascidos serão adicionados
     */
    @Override
    public void agir(CampoInterativo campoAtual, CampoInterativo campoAtualizado, List<Ator> novosHerbivoros) {
        incrementarIdade();
        incrementarFome();

        if (estaVivo()) {
            tentarComerGrama(campoAtual);
            processarReproducao(campoAtualizado, novosHerbivoros);
            tentarMoverLivremente(campoAtualizado);
        }
    }

    // ========== MÉTODOS DE ALIMENTAÇÃO ==========

    /**
     * Tenta consumir vegetação na localização atual do herbívoro.
     * <p>
     * <strong>Processo de alimentação:</strong>
     * <ol>
     * <li>Consulta o campo para tentar comer grama na posição atual</li>
     * <li>Se houver grama madura: recebe valor nutricional</li>
     * <li>Incrementa nível de alimento atual</li>
     * <li>Limita o alimento ao máximo configurado (evita desperdício)</li>
     * </ol>
     * </p>
     * <p>
     * <strong>Limite de Saciedade:</strong><br>
     * O nível de alimento é limitado a
     * {@link Configuracao#VALOR_ALIMENTAR_MAX_HERBIVORO}
     * para evitar que herbívoros acumulem energia infinita, mantendo o
     * equilíbrio ecológico da simulação.
     * </p>
     * 
     * @param campo Campo atual onde o herbívoro está localizado
     */
    private void tentarComerGrama(CampoInterativo campo) {
        int comida = campo.comerGrama(getLocalizacao());
        if (comida > 0) {
            setNivelAlimento(getNivelAlimento() + comida);
            if (getNivelAlimento() > Configuracao.VALOR_ALIMENTAR_MAX_HERBIVORO)
                setNivelAlimento(Configuracao.VALOR_ALIMENTAR_MAX_HERBIVORO);
        }
    }

    // ========== MÉTODOS ABSTRATOS ==========

    /**
     * Cria um novo filhote do tipo específico do herbívoro.
     * <p>
     * Cada subclasse deve implementar este método retornando
     * uma nova instância de si mesma com idade zero.
     * </p>
     * <p>
     * <strong>Exemplo de implementação:</strong>
     * 
     * <pre>
     * {@code
     * @Override
     * protected Herbivoro criarFilho() {
     *     return new Coelho(false);
     * }
     * }
     * </pre>
     * </p>
     * 
     * @return Nova instância de herbívoro representando um filhote
     */
    protected abstract Herbivoro criarFilho();
}