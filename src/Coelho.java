/**
 * Classe que representa coelhos no ecossistema de simulação.
 * <p>
 * Coelhos são herbívoros de médio porte que desempenham papel crucial
 * na cadeia alimentar, servindo como presa principal para diversos predadores
 * e convertendo vegetação em biomassa animal.
 * </p>
 * 
 * <p>
 * <strong>Características do Coelho:</strong>
 * </p>
 * <ul>
 * <li><strong>Dieta:</strong> Vegetação (grama madura)</li>
 * <li><strong>Predadores Naturais:</strong> Raposas, Cobras e Ursos</li>
 * <li><strong>Comportamento:</strong> Alimentação constante, reprodução
 * ativa</li>
 * <li><strong>Estratégia de Sobrevivência:</strong> Alta taxa reprodutiva</li>
 * </ul>
 * 
 * <p>
 * <strong>Posição na Cadeia Alimentar:</strong>
 * </p>
 * <p>
 * Como herbívoro intermediário, o coelho forma um elo essencial entre
 * produtores (vegetação) e predadores. Sua população influencia diretamente
 * a dinâmica de predadores e a pressão sobre recursos vegetais.
 * </p>
 * 
 * <p>
 * <strong>Dinâmica Populacional:</strong>
 * </p>
 * <p>
 * Coelhos exibem crescimento populacional rápido quando há vegetação abundante,
 * mas são controlados pela predação intensa. Este balanço cria ciclos
 * populacionais
 * característicos de sistemas predador-presa.
 * </p>
 * 
 * <p>
 * <strong>Parâmetros Configuráveis:</strong>
 * </p>
 * <ul>
 * <li>Idade máxima de vida</li>
 * <li>Idade de início da reprodução</li>
 * <li>Probabilidade de reprodução (alta)</li>
 * <li>Tamanho máximo de ninhada (grande)</li>
 * <li>Valor nutricional como presa</li>
 * </ul>
 * 
 * @author David J. Barnes e Michael Kolling
 * @author Grupo 10
 * @version 2002-04-11 (traduzido e expandido)
 * @see Herbivoro
 * @see Configuracao
 */
public class Coelho extends Herbivoro {

    // ========== CONSTRUTOR ==========

    /**
     * Cria uma nova instância de coelho.
     * <p>
     * O coelho herda automaticamente o comportamento de alimentação
     * por vegetação da classe {@link Herbivoro}, incluindo limite
     * de saciedade e busca por grama madura.
     * </p>
     * <p>
     * A idade inicial pode ser:
     * <ul>
     * <li>Zero: representa um filhote recém-nascido</li>
     * <li>Aleatória: representa um coelho já existente no início da simulação</li>
     * </ul>
     * </p>
     * 
     * @param idadeAleatoria Se true, a idade será aleatória entre 0 e idade máxima;
     *                       se false, a idade será zero (recém-nascido)
     */
    public Coelho(boolean idadeAleatoria) {
        super(idadeAleatoria);
    }

    // ========== MÉTODOS DE REPRODUÇÃO ==========

    /**
     * Cria um novo filhote de coelho.
     * <p>
     * O filhote nasce com:
     * <ul>
     * <li>Idade zero (recém-nascido)</li>
     * <li>Comportamento herbívoro padrão</li>
     * <li>Nível de alimento inicial configurado</li>
     * </ul>
     * </p>
     * 
     * @return Nova instância de Coelho representando um filhote
     */
    @Override
    public Herbivoro criarFilho() {
        return new Coelho(false);
    }

    // ========== MÉTODOS DE CONFIGURAÇÃO (da superclasse) ==========

    /**
     * Retorna a idade máxima que um coelho pode atingir.
     * <p>
     * Coelhos têm expectativa de vida moderada, equilibrando tempo
     * suficiente para reprodução com renovação populacional.
     * </p>
     * 
     * @return Idade máxima em passos de simulação
     * @see Configuracao#IDADE_MAX_COELHO
     */
    @Override
    protected int idadeMaxima() {
        return Configuracao.IDADE_MAX_COELHO;
    }

    /**
     * Retorna a idade em que o coelho atinge maturidade sexual.
     * <p>
     * Coelhos amadurecem relativamente rápido, permitindo
     * rápido crescimento populacional quando há recursos disponíveis.
     * </p>
     * 
     * @return Idade reprodutiva em passos de simulação
     * @see Configuracao#IDADE_REPROD_COELHO
     */
    @Override
    protected int getIdadeReprodutiva() {
        return Configuracao.IDADE_REPROD_COELHO;
    }

    /**
     * Retorna a probabilidade de reprodução por passo.
     * <p>
     * Coelhos têm probabilidade de reprodução alta (15%), característica
     * de espécies que compensam alta taxa de predação com reprodução rápida.
     * </p>
     * 
     * @return Probabilidade entre 0.0 e 1.0
     * @see Configuracao#PROB_REPROD_COELHO
     */
    @Override
    protected double probabilidadeReproducao() {
        return Configuracao.PROB_REPROD_COELHO;
    }

    /**
     * Retorna o número máximo de filhotes em um único nascimento.
     * <p>
     * Coelhos podem ter ninhadas grandes (até 5 filhotes), estratégia
     * típica de presas que precisam manter população apesar da predação intensa.
     * O número real pode ser menor dependendo do espaço disponível.
     * </p>
     * 
     * @return Tamanho máximo da ninhada
     * @see Configuracao#MAX_NINHADA_COELHO
     */
    @Override
    protected int tamanhoMaximoNinhada() {
        return Configuracao.MAX_NINHADA_COELHO;
    }
}