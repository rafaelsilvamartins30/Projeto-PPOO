/**
 * Classe que representa cobras no ecossistema de simulação.
 * <p>
 * Cobras são predadores terrestres de médio porte que caçam pequenos
 * mamíferos, desempenhando papel importante no controle populacional
 * de herbívoros e competindo com outros predadores por recursos.
 * </p>
 * 
 * <p>
 * <strong>Características da Cobra:</strong>
 * </p>
 * <ul>
 * <li><strong>Dieta:</strong> Ratos e Coelhos</li>
 * <li><strong>Predadores Naturais:</strong> Ursos</li>
 * <li><strong>Comportamento:</strong> Caça oportunista, reprodução
 * moderada</li>
 * <li><strong>Posição na Cadeia:</strong> Predador de nível médio</li>
 * </ul>
 * 
 * <p>
 * <strong>Estratégia de Caça:</strong>
 * </p>
 * <p>
 * Cobras são predadores generalistas que se alimentam tanto de ratos
 * quanto de coelhos, oferecendo flexibilidade alimentar. Esta adaptabilidade
 * permite sobrevivência mesmo quando uma das presas se torna escassa.
 * </p>
 * 
 * <p>
 * <strong>Dinâmica Ecológica:</strong>
 * </p>
 * <p>
 * Cobras competem com raposas pelos mesmos recursos (coelhos e ratos),
 * criando pressão predatória compartilhada sobre herbívoros. Esta competição
 * interespecífica influencia a distribuição e abundância de ambos predadores.
 * </p>
 * 
 * <p>
 * <strong>Parâmetros Configuráveis:</strong>
 * </p>
 * <ul>
 * <li>Idade máxima de vida</li>
 * <li>Idade de início da reprodução</li>
 * <li>Probabilidade de reprodução</li>
 * <li>Tamanho máximo de ninhada</li>
 * <li>Valor nutricional de cada tipo de presa</li>
 * </ul>
 * 
 * @author Grupo 10
 * @version 1.0
 * @see Predador
 * @see Rato
 * @see Coelho
 * @see Configuracao
 */
public class Cobra extends Predador {

    // ========== CONSTRUTOR ==========

    /**
     * Cria uma nova instância de cobra com dieta generalista.
     * <p>
     * A dieta é configurada automaticamente no construtor, incluindo:
     * <ul>
     * <li>Ratos - presa pequena de fácil captura</li>
     * <li>Coelhos - presa maior com mais valor nutricional</li>
     * </ul>
     * </p>
     * <p>
     * A idade inicial pode ser:
     * <ul>
     * <li>Zero: representa um filhote recém-nascido</li>
     * <li>Aleatória: representa uma cobra já existente no início da simulação</li>
     * </ul>
     * </p>
     * 
     * @param idadeAleatoria Se true, a idade será aleatória entre 0 e idade máxima;
     *                       se false, a idade será zero (recém-nascido)
     */
    public Cobra(boolean idadeAleatoria) {
        super(idadeAleatoria);
        dieta.put(Rato.class, Configuracao.VALOR_NUTRICIONAL_RATO);
        dieta.put(Coelho.class, Configuracao.VALOR_NUTRICIONAL_COELHO);
    }

    // ========== MÉTODOS DE REPRODUÇÃO ==========

    /**
     * Cria um novo filhote de cobra.
     * <p>
     * O filhote nasce com:
     * <ul>
     * <li>Idade zero (recém-nascido)</li>
     * <li>Dieta generalista já configurada</li>
     * <li>Nível de alimento inicial padrão</li>
     * </ul>
     * </p>
     * 
     * @return Nova instância de Cobra representando um filhote
     */
    @Override
    public Predador criarFilho() {
        return new Cobra(false);
    }

    // ========== MÉTODOS DE CONFIGURAÇÃO (da superclasse) ==========

    /**
     * Retorna a idade máxima que uma cobra pode atingir.
     * <p>
     * Cobras têm expectativa de vida moderada (80 passos), permitindo
     * múltiplos ciclos reprodutivos durante a vida.
     * </p>
     * 
     * @return Idade máxima em passos de simulação
     * @see Configuracao#IDADE_MAX_COBRA
     */
    @Override
    protected int idadeMaxima() {
        return Configuracao.IDADE_MAX_COBRA;
    }

    /**
     * Retorna a idade em que a cobra atinge maturidade sexual.
     * <p>
     * Cobras amadurecem relativamente rápido (8 passos), começando
     * a reproduzir antes que predadores maiores.
     * </p>
     * 
     * @return Idade reprodutiva em passos de simulação
     * @see Configuracao#IDADE_REPROD_COBRA
     */
    @Override
    protected int getIdadeReprodutiva() {
        return Configuracao.IDADE_REPROD_COBRA;
    }

    /**
     * Retorna a probabilidade de reprodução por passo.
     * <p>
     * Cobras têm probabilidade de reprodução moderada-alta (10%),
     * compensando predação por ursos e competição com raposas.
     * </p>
     * 
     * @return Probabilidade entre 0.0 e 1.0
     * @see Configuracao#PROB_REPROD_COBRA
     */
    @Override
    protected double probabilidadeReproducao() {
        return Configuracao.PROB_REPROD_COBRA;
    }

    /**
     * Retorna o número máximo de filhotes em um único nascimento.
     * <p>
     * Cobras podem ter ninhadas relativamente grandes (até 4 filhotes),
     * estratégia que favorece rápida recuperação populacional.
     * O número real pode ser menor dependendo do espaço disponível.
     * </p>
     * 
     * @return Tamanho máximo da ninhada
     * @see Configuracao#MAX_NINHADA_COBRA
     */
    @Override
    protected int tamanhoMaximoNinhada() {
        return Configuracao.MAX_NINHADA_COBRA;
    }
}