/**
 * Classe que representa gaviões no ecossistema de simulação.
 * <p>
 * Gaviões são predadores aéreos especializados na caça de pequenos roedores,
 * representando aves de rapina no ecossistema. Sua presença ajuda a controlar
 * populações de ratos, complementando a pressão predatória terrestre.
 * </p>
 * 
 * <p>
 * <strong>Características do Gavião:</strong>
 * </p>
 * <ul>
 * <li><strong>Dieta:</strong> Ratos (presa exclusiva)</li>
 * <li><strong>Predadores Naturais:</strong> Nenhum (topo da cadeia em seu
 * nicho)</li>
 * <li><strong>Comportamento:</strong> Caça especializada, reprodução
 * moderada</li>
 * <li><strong>Posição na Cadeia:</strong> Predador especialista</li>
 * </ul>
 * 
 * <p>
 * <strong>Estratégia Alimentar:</strong>
 * </p>
 * <p>
 * Como predador especialista, o gavião depende exclusivamente de ratos
 * para sobrevivência. Esta especialização torna sua população sensível
 * às flutuações na população de presas, criando dinâmica predador-presa
 * clássica.
 * </p>
 * 
 * <p>
 * <strong>Papel Ecológico:</strong>
 * </p>
 * <p>
 * Gaviões funcionam como controle biológico de roedores, prevenindo
 * superpopulação de ratos. Sua especialização cria um balanço delicado:
 * poucos ratos = gaviões morrem de fome; muitos gaviões = ratos diminuem
 * rapidamente.
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
 * <li>Valor nutricional de ratos</li>
 * </ul>
 * 
 * @author Grupo 10
 * @version 1.0
 * @see Predador
 * @see Rato
 * @see Configuracao
 */
public class Gaviao extends Predador {

    // ========== CONSTRUTOR ==========

    /**
     * Cria uma nova instância de gavião com dieta especializada.
     * <p>
     * O gavião é configurado como predador especialista, caçando
     * exclusivamente ratos. Esta especialização reflete o comportamento
     * de aves de rapina que focam em tipos específicos de presas.
     * </p>
     * <p>
     * A idade inicial pode ser:
     * <ul>
     * <li>Zero: representa um filhote recém-nascido</li>
     * <li>Aleatória: representa um gavião já existente no início da simulação</li>
     * </ul>
     * </p>
     * 
     * @param idadeAleatoria Se true, a idade será aleatória entre 0 e idade máxima;
     *                       se false, a idade será zero (recém-nascido)
     */
    public Gaviao(boolean idadeAleatoria) {
        super(idadeAleatoria);
        dieta.put(Rato.class, Configuracao.VALOR_NUTRICIONAL_RATO);
    }

    // ========== MÉTODOS DE REPRODUÇÃO ==========

    /**
     * Cria um novo filhote de gavião.
     * <p>
     * O filhote nasce com:
     * <ul>
     * <li>Idade zero (recém-nascido)</li>
     * <li>Dieta especializada já configurada</li>
     * <li>Nível de alimento inicial padrão</li>
     * </ul>
     * </p>
     * 
     * @return Nova instância de Gavião representando um filhote
     */
    @Override
    public Predador criarFilho() {
        return new Gaviao(false);
    }

    // ========== MÉTODOS DE CONFIGURAÇÃO (da superclasse) ==========

    /**
     * Retorna a idade máxima que um gavião pode atingir.
     * <p>
     * Gaviões têm expectativa de vida relativamente longa para
     * predadores, refletindo a longevidade de aves de rapina.
     * </p>
     * 
     * @return Idade máxima em passos de simulação
     * @see Configuracao#IDADE_MAX_GAVIAO
     */
    @Override
    protected int idadeMaxima() {
        return Configuracao.IDADE_MAX_GAVIAO;
    }

    /**
     * Retorna a idade em que o gavião atinge maturidade sexual.
     * <p>
     * Aves de rapina geralmente levam mais tempo para amadurecer,
     * representado por uma idade reprodutiva mais elevada.
     * </p>
     * 
     * @return Idade reprodutiva em passos de simulação
     * @see Configuracao#IDADE_REPROD_GAVIAO
     */
    @Override
    protected int getIdadeReprodutiva() {
        return Configuracao.IDADE_REPROD_GAVIAO;
    }

    /**
     * Retorna a probabilidade de reprodução por passo.
     * <p>
     * Gaviões têm taxa reprodutiva moderada, típica de predadores
     * especialistas que investem mais em cada filhote (estratégia K).
     * </p>
     * 
     * @return Probabilidade entre 0.0 e 1.0
     * @see Configuracao#PROB_REPROD_GAVIAO
     */
    @Override
    protected double probabilidadeReproducao() {
        return Configuracao.PROB_REPROD_GAVIAO;
    }

    /**
     * Retorna o número máximo de filhotes em um único nascimento.
     * <p>
     * Ninhadas pequenas são típicas de aves de rapina, que investem
     * mais energia no cuidado parental de poucos filhotes.
     * </p>
     * 
     * @return Tamanho máximo da ninhada
     * @see Configuracao#MAX_NINHADA_GAVIAO
     */
    @Override
    protected int tamanhoMaximoNinhada() {
        return Configuracao.MAX_NINHADA_GAVIAO;
    }
}