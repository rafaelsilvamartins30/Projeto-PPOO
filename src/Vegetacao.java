/**
 * Classe que representa a vegetação (grama) no ecossistema.
 * <p>
 * A vegetação funciona como fonte de alimento para animais herbívoros,
 * gerenciando seu próprio ciclo de crescimento e regeneração após ser
 * consumida.
 * </p>
 * <p>
 * <strong>Ciclo de vida da vegetação:</strong>
 * <ol>
 * <li>Inicia em estado maduro (totalmente crescida)</li>
 * <li>Quando consumida, retorna ao nível 0 de crescimento</li>
 * <li>Cresce gradualmente até atingir maturidade novamente</li>
 * <li>Apenas vegetação madura pode ser consumida</li>
 * </ol>
 * </p>
 * <p>
 * O valor nutricional e o nível máximo de crescimento são definidos
 * na classe {@link Configuracao}, permitindo ajuste fácil do comportamento.
 * </p>
 * 
 * @author Grupo 10
 * @version 1.0
 * @see Configuracao
 * @see Campo
 */
public class Vegetacao {

    // ========== ATRIBUTOS ==========

    /**
     * Nível atual de crescimento da vegetação.
     * <p>
     * Valores possíveis:
     * <ul>
     * <li>0 = Recém consumida, não visível</li>
     * <li>1 até MAX_CRESCIMENTO-1 = Em crescimento</li>
     * <li>MAX_CRESCIMENTO = Madura e comestível</li>
     * </ul>
     * </p>
     * 
     * @see Configuracao#MAX_CRESCIMENTO
     */
    private int nivelCrescimento;

    // ========== CONSTRUTOR ==========

    /**
     * Cria uma nova instância de vegetação em estado maduro.
     * <p>
     * A vegetação inicia completamente crescida (nível máximo),
     * representando um campo já estabelecido no início da simulação.
     * </p>
     */
    public Vegetacao() {
        this.nivelCrescimento = Configuracao.MAX_CRESCIMENTO;
    }

    // ========== MÉTODOS DE CRESCIMENTO ==========

    /**
     * Avança o crescimento da vegetação em um estágio.
     * <p>
     * Este método deve ser chamado periodicamente pela simulação
     * para permitir que a vegetação regenere após ser consumida.
     * Se a vegetação já estiver madura, não há efeito adicional.
     * </p>
     * <p>
     * <strong>Exemplo de uso:</strong>
     * 
     * <pre>
     * Vegetacao grama = new Vegetacao();
     * grama.vegetacaoEhComida(); // Consumida, nível = 0
     * grama.crescer(); // nível = 1
     * grama.crescer(); // nível = 2
     * // ... até atingir MAX_CRESCIMENTO
     * </pre>
     * </p>
     */
    public void crescer() {
        if (nivelCrescimento < Configuracao.MAX_CRESCIMENTO) {
            nivelCrescimento++;
        }
    }

    // ========== MÉTODOS DE VERIFICAÇÃO ==========

    /**
     * Verifica se a vegetação está madura e pronta para consumo.
     * <p>
     * Vegetação madura significa que:
     * <ul>
     * <li>Está visível no campo de simulação</li>
     * <li>Pode ser consumida por herbívoros</li>
     * <li>Fornece valor nutricional completo</li>
     * </ul>
     * </p>
     * 
     * @return true se o nível de crescimento atingiu o máximo, false caso contrário
     */
    public boolean estaMadura() {
        return nivelCrescimento >= Configuracao.MAX_CRESCIMENTO;
    }

    // ========== MÉTODOS DE INTERAÇÃO ==========

    /**
     * Simula o consumo da vegetação por um animal herbívoro.
     * <p>
     * <strong>Comportamento:</strong>
     * <ul>
     * <li>Se madura: reseta o crescimento para 0 e retorna valor nutricional</li>
     * <li>Se não madura: não há efeito e retorna 0</li>
     * </ul>
     * </p>
     * <p>
     * Após ser consumida, a vegetação precisará crescer novamente
     * através de chamadas ao método {@link #crescer()}.
     * </p>
     * 
     * @return O valor nutricional obtido ({@link Configuracao#VALOR_NUTRICIONAL})
     *         se a vegetação estava madura, ou 0 caso contrário
     */
    public int vegetacaoEhComida() {
        if (estaMadura()) {
            nivelCrescimento = 0;
            return Configuracao.VALOR_NUTRICIONAL;
        }
        return 0;
    }

    // ========== GETTERS E SETTERS ==========

    /**
     * Retorna o nível atual de crescimento da vegetação.
     * <p>
     * Este valor é útil para:
     * <ul>
     * <li>Determinar visualmente o estado da vegetação</li>
     * <li>Renderizar cores diferentes (ex: verde claro vs. verde escuro)</li>
     * <li>Estatísticas e debug da simulação</li>
     * </ul>
     * </p>
     * 
     * @return Nível de crescimento atual (0 até
     *         {@link Configuracao#MAX_CRESCIMENTO})
     */
    public int getNivelCrescimento() {
        return nivelCrescimento;
    }

    /**
     * Define manualmente o nível de crescimento da vegetação.
     * <p>
     * <strong>Uso típico:</strong>
     * <ul>
     * <li>Reinicialização da simulação</li>
     * <li>Testes unitários</li>
     * <li>Criação de cenários específicos</li>
     * </ul>
     * </p>
     * <p>
     * <strong>Atenção:</strong> Não há validação de limites. Certifique-se
     * de passar valores entre 0 e {@link Configuracao#MAX_CRESCIMENTO}.
     * </p>
     * 
     * @param nivel Novo nível de crescimento a ser definido
     */
    public void setNivelCrescimento(int nivel) {
        this.nivelCrescimento = nivel;
    }
}