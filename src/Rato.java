/**
 * Classe que representa ratos no ecossistema de simulação.
 * <p>
 * Ratos são pequenos herbívoros que desempenham papel fundamental
 * na cadeia alimentar, servindo como presa para múltiplos predadores.
 * </p>
 * 
 * <p>
 * <strong>Características do Rato:</strong>
 * </p>
 * <ul>
 * <li><strong>Dieta:</strong> Vegetação (grama madura)</li>
 * <li><strong>Predadores Naturais:</strong> Raposas, Cobras, Gaviões e
 * Ursos</li>
 * <li><strong>Comportamento:</strong> Alimentação, reprodução rápida e
 * movimento</li>
 * <li><strong>Estratégia de Sobrevivência:</strong> Alta taxa de
 * reprodução</li>
 * </ul>
 * 
 * <p>
 * <strong>Posição na Cadeia Alimentar:</strong>
 * </p>
 * <p>
 * Como herbívoro de base, o rato é essencial para manter o equilíbrio
 * populacional
 * dos predadores. Sua alta taxa reprodutiva compensa as perdas por predação.
 * </p>
 * 
 * <p>
 * <strong>Parâmetros Configuráveis:</strong>
 * </p>
 * <ul>
 * <li>Idade máxima de vida</li>
 * <li>Idade de início da reprodução</li>
 * <li>Probabilidade de reprodução por turno</li>
 * <li>Tamanho máximo de ninhada</li>
 * <li>Valor nutricional como presa</li>
 * </ul>
 * 
 * @author Grupo 10
 * @version 1.0
 * @see Herbivoro
 * @see Configuracao
 */
public class Rato extends Herbivoro {

    // ========== CONSTRUTOR ==========

    /**
     * Cria uma nova instância de rato.
     * <p>
     * O rato herda automaticamente o comportamento de alimentação
     * por vegetação da classe {@link Herbivoro}.
     * </p>
     * <p>
     * A idade inicial pode ser:
     * <ul>
     * <li>Zero: representa um filhote recém-nascido</li>
     * <li>Aleatória: representa um rato já existente no início da simulação</li>
     * </ul>
     * </p>
     * 
     * @param idadeAleatoria Se true, a idade será aleatória entre 0 e idade máxima;
     *                       se false, a idade será zero (recém-nascido)
     */
    public Rato(boolean idadeAleatoria) {
        super(idadeAleatoria);
    }

    // ========== MÉTODOS DE REPRODUÇÃO ==========

    /**
     * Cria um novo filhote de rato.
     * <p>
     * O filhote nasce com:
     * <ul>
     * <li>Idade zero (recém-nascido)</li>
     * <li>Comportamento herbívoro padrão</li>
     * <li>Nível de alimento inicial configurado</li>
     * </ul>
     * </p>
     * 
     * @return Nova instância de Rato representando um filhote
     */
    @Override
    public Herbivoro criarFilho() {
        return new Rato(false);
    }

    // ========== MÉTODOS DE CONFIGURAÇÃO (da superclasse) ==========

    /**
     * Retorna a idade máxima que um rato pode atingir.
     * <p>
     * Ratos têm expectativa de vida relativamente curta,
     * compensada por sua alta taxa reprodutiva.
     * </p>
     * 
     * @return Idade máxima em passos de simulação
     * @see Configuracao#IDADE_MAX_RATO
     */
    @Override
    protected int idadeMaxima() {
        return Configuracao.IDADE_MAX_RATO;
    }

    /**
     * Retorna a idade em que o rato atinge maturidade sexual.
     * <p>
     * Ratos atingem maturidade rapidamente, permitindo
     * rápida expansão populacional quando há recursos.
     * </p>
     * 
     * @return Idade reprodutiva em passos de simulação
     * @see Configuracao#IDADE_REPROD_RATO
     */
    @Override
    protected int getIdadeReprodutiva() {
        return Configuracao.IDADE_REPROD_RATO;
    }

    /**
     * Retorna a probabilidade de reprodução por passo.
     * <p>
     * Ratos têm alta probabilidade de reprodução, característica
     * típica de espécies r-estrategistas (reprodução rápida).
     * </p>
     * 
     * @return Probabilidade entre 0.0 e 1.0
     * @see Configuracao#PROB_REPROD_RATO
     */
    @Override
    protected double probabilidadeReproducao() {
        return Configuracao.PROB_REPROD_RATO;
    }

    /**
     * Retorna o número máximo de filhotes em um único nascimento.
     * <p>
     * Ratos podem ter ninhadas grandes, permitindo recuperação
     * rápida da população após eventos de predação intensa.
     * O número real pode ser menor dependendo do espaço disponível.
     * </p>
     * 
     * @return Tamanho máximo da ninhada
     * @see Configuracao#MAX_NINHADA_RATO
     */
    @Override
    protected int tamanhoMaximoNinhada() {
        return Configuracao.MAX_NINHADA_RATO;
    }
}