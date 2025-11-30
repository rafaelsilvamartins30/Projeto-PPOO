/**
 * Classe central de configuração e balanceamento do ecossistema.
 * <p>
 * Centraliza todas as constantes que controlam o comportamento da simulação,
 * permitindo ajustes finos sem modificar código lógico. Esta abordagem
 * facilita experimentação com diferentes parâmetros para encontrar
 * equilíbrios ecológicos interessantes.
 * </p>
 * 
 * <p>
 * <strong>Categorias de Configuração:</strong>
 * </p>
 * <ul>
 * <li><strong>Dimensões do Campo:</strong> Tamanho da grade de simulação</li>
 * <li><strong>População Inicial:</strong> Probabilidades de spawn para cada
 * espécie</li>
 * <li><strong>Vegetação:</strong> Crescimento e valor nutricional</li>
 * <li><strong>Espécies:</strong> Parâmetros de vida, reprodução e
 * alimentação</li>
 * </ul>
 * 
 * <p>
 * <strong>Filosofia de Design:</strong>
 * </p>
 * <p>
 * Usar constantes públicas estáticas permite:
 * <ul>
 * <li>Acesso global sem instanciação</li>
 * <li>Modificação centralizada de parâmetros</li>
 * <li>Experimentação rápida com balanceamento</li>
 * <li>Documentação clara de valores padrão</li>
 * </ul>
 * </p>
 * 
 * <p>
 * <strong>Guia de Balanceamento:</strong>
 * </p>
 * <ul>
 * <li>Herbívoros devem ter reprodução mais rápida que predadores</li>
 * <li>Predadores de topo devem ter menor probabilidade de spawn</li>
 * <li>Valores nutricionais devem refletir tamanho/esforço de caça</li>
 * <li>Idades máximas devem criar turnover populacional adequado</li>
 * </ul>
 * 
 * @author Grupo 10
 * @version 1.0
 */
public abstract class Configuracao {

    // ========== VALORES ALIMENTARES GLOBAIS ==========

    /**
     * Valor padrão de alimento usado para inicialização de animais.
     * <p>
     * Animais recém-criados começam com este nível de energia.
     * </p>
     */
    public static final int VALOR_ALIMENTAR = 10;

    /**
     * Limite máximo de alimento que herbívoros podem armazenar.
     * <p>
     * Previne acúmulo infinito de energia, mantendo herbívoros
     * dependentes de alimentação contínua.
     * </p>
     */
    public static final int VALOR_ALIMENTAR_MAX_HERBIVORO = 20;

    /**
     * Energia fornecida por um peixe capturado.
     * <p>
     * Usado pela mecânica de pesca do urso.
     * </p>
     */
    public static final int VALOR_ALIMENTAR_PEIXE = 5;

    // ========== DIMENSÕES DO CAMPO ==========

    /**
     * Largura padrão do campo de simulação em células.
     * <p>
     * Dimensão horizontal (eixo X). Valores maiores aumentam
     * o espaço disponível mas reduzem performance.
     * </p>
     */
    public static final int LARGURA_PADRAO = 120;

    /**
     * Profundidade padrão do campo de simulação em células.
     * <p>
     * Dimensão vertical (eixo Y). Junto com largura, determina
     * a capacidade total do ecossistema.
     * </p>
     */
    public static final int PROFUNDIDADE_PADRAO = 75;

    // ========== PROBABILIDADES DE POPULAÇÃO INICIAL ==========

    /**
     * Probabilidade de criar um rato em cada célula vazia na inicialização.
     * <p>
     * Valor: 8% - Alta, pois ratos são base da cadeia alimentar.
     * </p>
     */
    public static final double PROBABILIDADE_CRIACAO_RATO = 0.08;

    /**
     * Probabilidade de criar uma raposa em cada célula vazia na inicialização.
     * <p>
     * Valor: 2% - Moderada, predador de nível médio.
     * </p>
     */
    public static final double PROBABILIDADE_CRIACAO_RAPOSA = 0.02;

    /**
     * Probabilidade de criar um coelho em cada célula vazia na inicialização.
     * <p>
     * Valor: 8% - Alta, herbívoro essencial para predadores.
     * </p>
     */
    public static final double PROBABILIDADE_CRIACAO_COELHO = 0.08;

    /**
     * Probabilidade de criar uma cobra em cada célula vazia na inicialização.
     * <p>
     * Valor: 3% - Moderada-baixa, predador especializado.
     * </p>
     */
    public static final double PROBABILIDADE_CRIACAO_COBRA = 0.03;

    /**
     * Probabilidade de criar um gavião em cada célula vazia na inicialização.
     * <p>
     * Valor: 2% - Moderada-baixa, predador aéreo especialista.
     * </p>
     */
    public static final double PROBABILIDADE_CRIACAO_GAVIAO = 0.02;

    /**
     * Probabilidade de criar um urso em cada célula vazia na inicialização.
     * <p>
     * Valor: 1% - Baixa, predador de topo da cadeia alimentar.
     * </p>
     */
    public static final double PROBABILIDADE_CRIACAO_URSO = 0.01;

    // ========== VEGETAÇÃO ==========

    /**
     * Nível máximo de crescimento da vegetação.
     * <p>
     * Vegetação deve atingir este valor para ser comestível.
     * Valores maiores tornam grama mais escassa.
     * </p>
     */
    public static final int MAX_CRESCIMENTO = 10;

    /**
     * Valor nutricional fornecido por vegetação madura quando consumida.
     * <p>
     * Energia que herbívoros ganham ao comer grama.
     * </p>
     */
    public static final int VALOR_NUTRICIONAL = 5;

    // ========== RAPOSA ==========

    /**
     * Idade máxima que uma raposa pode atingir (em passos).
     */
    public static final int IDADE_MAX_RAPOSA = 150;

    /**
     * Idade mínima para reprodução de raposas (em passos).
     */
    public static final int IDADE_REPROD_RAPOSA = 10;

    /**
     * Probabilidade de reprodução por turno para raposas.
     * <p>
     * Valor: 9% - Moderada, balanceando crescimento populacional.
     * </p>
     */
    public static final double PROB_REPROD_RAPOSA = 0.09;

    /**
     * Número máximo de filhotes por ninhada de raposa.
     */
    public static final int MAX_NINHADA_RAPOSA = 3;

    /**
     * Energia fornecida ao consumir uma raposa.
     * <p>
     * Usado por predadores maiores (ex: ursos).
     * </p>
     */
    public static final int VALOR_NUTRICIONAL_RAPOSA = 8;

    // ========== URSO ==========

    /**
     * Idade máxima que um urso pode atingir (em passos).
     */
    public static final int IDADE_MAX_URSO = 120;

    /**
     * Idade mínima para reprodução de ursos (em passos).
     */
    public static final int IDADE_REPROD_URSO = 10;

    /**
     * Probabilidade de reprodução por turno para ursos.
     * <p>
     * Valor: 5% - Baixa, predador de topo com reprodução lenta.
     * </p>
     */
    public static final double PROB_REPROD_URSO = 0.05;

    /**
     * Número máximo de filhotes por ninhada de urso.
     */
    public static final int MAX_NINHADA_URSO = 2;

    /**
     * Energia fornecida ao consumir um urso.
     * <p>
     * Valor alto - ursos são grandes e nutritivos.
     * </p>
     */
    public static final int VALOR_NUTRICIONAL_URSO = 30;

    /**
     * Probabilidade de sucesso ao tentar pescar em um rio.
     * <p>
     * Valor: 30% - Moderada, pesca não é garantida.
     * </p>
     */
    public static final double PROB_PESCA_URSO = 0.3;

    // ========== COBRA ==========

    /**
     * Idade máxima que uma cobra pode atingir (em passos).
     */
    public static final int IDADE_MAX_COBRA = 80;

    /**
     * Idade mínima para reprodução de cobras (em passos).
     */
    public static final int IDADE_REPROD_COBRA = 8;

    /**
     * Probabilidade de reprodução por turno para cobras.
     * <p>
     * Valor: 10% - Moderada-alta, compensando predação.
     * </p>
     */
    public static final double PROB_REPROD_COBRA = 0.10;

    /**
     * Número máximo de filhotes por ninhada de cobra.
     */
    public static final int MAX_NINHADA_COBRA = 4;

    /**
     * Energia fornecida ao consumir uma cobra.
     */
    public static final int VALOR_NUTRICIONAL_COBRA = 6;

    // ========== GAVIÃO ==========

    /**
     * Idade máxima que um gavião pode atingir (em passos).
     */
    public static final int IDADE_MAX_GAVIAO = 70;

    /**
     * Idade mínima para reprodução de gaviões (em passos).
     */
    public static final int IDADE_REPROD_GAVIAO = 5;

    /**
     * Probabilidade de reprodução por turno para gaviões.
     * <p>
     * Valor: 8% - Moderada, ave de rapina com reprodução controlada.
     * </p>
     */
    public static final double PROB_REPROD_GAVIAO = 0.08;

    /**
     * Número máximo de filhotes por ninhada de gavião.
     */
    public static final int MAX_NINHADA_GAVIAO = 3;

    /**
     * Energia fornecida ao consumir um gavião.
     */
    public static final int VALOR_NUTRICIONAL_GAVIAO = 8;

    // ========== COELHO ==========

    /**
     * Idade máxima que um coelho pode atingir (em passos).
     */
    public static final int IDADE_MAX_COELHO = 50;

    /**
     * Idade mínima para reprodução de coelhos (em passos).
     */
    public static final int IDADE_REPROD_COELHO = 5;

    /**
     * Probabilidade de reprodução por turno para coelhos.
     * <p>
     * Valor: 15% - Alta, herbívoro com reprodução rápida.
     * </p>
     */
    public static final double PROB_REPROD_COELHO = 0.15;

    /**
     * Número máximo de filhotes por ninhada de coelho.
     */
    public static final int MAX_NINHADA_COELHO = 5;

    /**
     * Energia fornecida ao consumir um coelho.
     */
    public static final int VALOR_NUTRICIONAL_COELHO = 7;

    // ========== RATO ==========

    /**
     * Idade máxima que um rato pode atingir (em passos).
     */
    public static final int IDADE_MAX_RATO = 30;

    /**
     * Idade mínima para reprodução de ratos (em passos).
     */
    public static final int IDADE_REPROD_RATO = 3;

    /**
     * Probabilidade de reprodução por turno para ratos.
     * <p>
     * Valor: 20% - Muito alta, estratégia r (muitos filhotes, rápido).
     * </p>
     */
    public static final double PROB_REPROD_RATO = 0.20;

    /**
     * Número máximo de filhotes por ninhada de rato.
     */
    public static final int MAX_NINHADA_RATO = 6;

    /**
     * Energia fornecida ao consumir um rato.
     */
    public static final int VALOR_NUTRICIONAL_RATO = 5;
}