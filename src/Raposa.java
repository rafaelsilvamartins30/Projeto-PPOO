/**
 * Classe que representa raposas no ecossistema de simulação.
 * <p>
 * As raposas são predadores de médio porte que caçam pequenos herbívoros
 * para se alimentar, desempenhando papel importante no controle populacional
 * de presas.
 * </p>
 * 
 * <p>
 * <strong>Características da Raposa:</strong>
 * </p>
 * <ul>
 * <li><strong>Dieta:</strong> Coelhos e Ratos</li>
 * <li><strong>Predadores Naturais:</strong> Ursos</li>
 * <li><strong>Comportamento:</strong> Caça ativa, reprodução e movimento
 * estratégico</li>
 * <li><strong>Posição na Cadeia:</strong> Predador de nível médio</li>
 * </ul>
 * 
 * <p>
 * <strong>Dinâmica Populacional:</strong>
 * </p>
 * <p>
 * As raposas funcionam como reguladoras das populações de herbívoros.
 * Sua sobrevivência depende diretamente da disponibilidade de presas,
 * e sua população influencia o crescimento de coelhos e ratos.
 * </p>
 * 
 * <p>
 * <strong>Estratégia de Caça:</strong>
 * </p>
 * <p>
 * Raposas priorizam presas com maior valor nutricional e buscam
 * localizações adjacentes para caçar. Quando não encontram alimento,
 * movem-se para localizações livres.
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
 * @author David J. Barnes e Michael Kolling
 * @author Grupo 10
 * @version 2002-04-11 (traduzido e expandido)
 * @see Predador
 * @see Coelho
 * @see Rato
 * @see Configuracao
 */
public class Raposa extends Predador {

    // ========== CONSTRUTOR ==========

    /**
     * Cria uma nova instância de raposa com dieta predefinida.
     * <p>
     * A dieta é configurada automaticamente no construtor, incluindo:
     * <ul>
     * <li>Coelhos - presa principal com valor nutricional médio</li>
     * <li>Ratos - presa alternativa com menor valor nutricional</li>
     * </ul>
     * </p>
     * <p>
     * A idade inicial pode ser:
     * <ul>
     * <li>Zero: representa um filhote recém-nascido</li>
     * <li>Aleatória: representa uma raposa já existente no início da simulação</li>
     * </ul>
     * </p>
     * 
     * @param idadeAleatoria Se true, a idade será aleatória entre 0 e idade máxima;
     *                       se false, a idade será zero (recém-nascido)
     */
    public Raposa(boolean idadeAleatoria) {
        super(idadeAleatoria);
        dieta.put(Coelho.class, Configuracao.VALOR_NUTRICIONAL_COELHO);
        dieta.put(Rato.class, Configuracao.VALOR_NUTRICIONAL_RATO);
    }

    // ========== MÉTODOS DE REPRODUÇÃO ==========

    /**
     * Cria um novo filhote de raposa.
     * <p>
     * O filhote nasce com:
     * <ul>
     * <li>Idade zero (recém-nascido)</li>
     * <li>Dieta já configurada automaticamente</li>
     * <li>Nível de alimento inicial padrão</li>
     * </ul>
     * </p>
     * 
     * @return Nova instância de Raposa representando um filhote
     */
    @Override
    public Predador criarFilho() {
        return new Raposa(false);
    }

    // ========== MÉTODOS DE CONFIGURAÇÃO (da superclasse) ==========

    /**
     * Retorna a idade máxima que uma raposa pode atingir.
     * <p>
     * Quando esta idade é atingida, a raposa morre naturalmente.
     * Raposas têm expectativa de vida moderada, equilibrando
     * reprodução e longevidade.
     * </p>
     * 
     * @return Idade máxima em passos de simulação
     * @see Configuracao#IDADE_MAX_RAPOSA
     */
    @Override
    protected int idadeMaxima() {
        return Configuracao.IDADE_MAX_RAPOSA;
    }

    /**
     * Retorna a idade em que a raposa atinge maturidade sexual.
     * <p>
     * Raposas precisam atingir esta idade antes de poderem se reproduzir,
     * representando o período de desenvolvimento até a idade adulta.
     * </p>
     * 
     * @return Idade reprodutiva em passos de simulação
     * @see Configuracao#IDADE_REPROD_RAPOSA
     */
    @Override
    protected int getIdadeReprodutiva() {
        return Configuracao.IDADE_REPROD_RAPOSA;
    }

    /**
     * Retorna a probabilidade de reprodução por passo.
     * <p>
     * Em cada turno, se as condições forem atendidas (idade adequada,
     * parceiro próximo, saúde), esta probabilidade determina se ocorre reprodução.
     * Raposas têm taxa reprodutiva moderada, típica de predadores de médio porte.
     * </p>
     * 
     * @return Probabilidade entre 0.0 e 1.0
     * @see Configuracao#PROB_REPROD_RAPOSA
     */
    @Override
    protected double probabilidadeReproducao() {
        return Configuracao.PROB_REPROD_RAPOSA;
    }

    /**
     * Retorna o número máximo de filhotes em um único nascimento.
     * <p>
     * O número real de filhotes nascidos pode ser menor, dependendo
     * do espaço disponível ao redor dos pais. Raposas têm ninhadas
     * de tamanho moderado, equilibrando investimento parental e taxa reprodutiva.
     * </p>
     * 
     * @return Tamanho máximo da ninhada
     * @see Configuracao#MAX_NINHADA_RAPOSA
     */
    @Override
    protected int tamanhoMaximoNinhada() {
        return Configuracao.MAX_NINHADA_RAPOSA;
    }
}
