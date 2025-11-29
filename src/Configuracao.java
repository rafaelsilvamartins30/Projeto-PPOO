/**
 * Classe central para configurações e balanceamento do ecossistema.
 * Permite ajustar probabilidades, idades e valores nutricionais em um só lugar.
 */
public abstract class Configuracao {
    // Valor padrão de alimento para animais. Usado para inicialização e alimentação.
    public static final int VALOR_ALIMENTAR = 10;
    // Valor máximo de alimento que um herbívoro pode ter.
    public static final int VALOR_ALIMENTAR_MAX_HERBIVORO = 20;
    // Valor de alimento que um peixe fornece quando comido.
    public static final int VALOR_ALIMENTAR_PEIXE = 5;

    // --- Dimensões do campo de simulação ---
    public static final int LARGURA_PADRAO = 120;
    public static final int PROFUNDIDADE_PADRAO = 75;

    // --- Probabilidades de criação ---
    public static final double PROBABILIDADE_CRIACAO_RATO = 0.08;
    public static final double PROBABILIDADE_CRIACAO_RAPOSA = 0.02;
    public static final double PROBABILIDADE_CRIACAO_COELHO = 0.08;
    public static final double PROBABILIDADE_CRIACAO_COBRA = 0.03;
    public static final double PROBABILIDADE_CRIACAO_GAVIAO = 0.02;
    public static final double PROBABILIDADE_CRIACAO_URSO = 0.01;

    // --- Vegetação ---
    public static final int MAX_CRESCIMENTO = 10;
    public static final int VALOR_NUTRICIONAL = 5;

    // --- RAPOSA ---
    public static final int IDADE_MAX_RAPOSA = 150;
    public static final int IDADE_REPROD_RAPOSA = 10;
    public static final double PROB_REPROD_RAPOSA = 0.09;
    public static final int MAX_NINHADA_RAPOSA = 3;
    public static final int VALOR_NUTRICIONAL_RAPOSA = 8;

    // --- URSO ---
    public static final int IDADE_MAX_URSO = 120;
    public static final int IDADE_REPROD_URSO = 10;
    public static final double PROB_REPROD_URSO = 0.05;
    public static final int MAX_NINHADA_URSO = 2;
    public static final int VALOR_NUTRICIONAL_URSO = 30;
    public static final double PROB_PESCA_URSO = 0.3;

    // --- COBRA ---
    public static final int IDADE_MAX_COBRA = 80;
    public static final int IDADE_REPROD_COBRA = 8;
    public static final double PROB_REPROD_COBRA = 0.10;
    public static final int MAX_NINHADA_COBRA = 4;
    public static final int VALOR_NUTRICIONAL_COBRA = 6;
    
    // --- GAVIAO ---
    public static final int IDADE_MAX_GAVIAO = 70;
    public static final int IDADE_REPROD_GAVIAO = 5;
    public static final double PROB_REPROD_GAVIAO = 0.08;
    public static final int MAX_NINHADA_GAVIAO = 3;
    public static final int VALOR_NUTRICIONAL_GAVIAO = 8;

    // --- COELHO ---
    public static final int IDADE_MAX_COELHO = 50;
    public static final int IDADE_REPROD_COELHO = 5;
    public static final double PROB_REPROD_COELHO = 0.15;
    public static final int MAX_NINHADA_COELHO = 5;
    public static final int VALOR_NUTRICIONAL_COELHO = 7;

    // --- RATO ---
    public static final int IDADE_MAX_RATO = 30;
    public static final int IDADE_REPROD_RATO = 3;
    public static final double PROB_REPROD_RATO = 0.20;
    public static final int MAX_NINHADA_RATO = 6;
    public static final int VALOR_NUTRICIONAL_RATO = 5;
}