/**
 * Classe que representa uma posição específica em uma grade retangular
 * bidimensional.
 * <p>
 * Esta classe é um componente fundamental do sistema de coordenadas da
 * simulação,
 * encapsulando as coordenadas (linha, coluna) de forma imutável e fornecendo
 * métodos para comparação e representação.
 * </p>
 * 
 * <p>
 * <strong>Características:</strong>
 * </p>
 * <ul>
 * <li>Imutável após criação (linha e coluna não podem ser alteradas)</li>
 * <li>Implementa {@code equals()} e {@code hashCode()} corretamente</li>
 * <li>Pode ser usada como chave em HashMap e HashSet</li>
 * <li>Representação textual legível via {@code toString()}</li>
 * </ul>
 * 
 * <p>
 * <strong>Sistema de Coordenadas:</strong>
 * </p>
 * 
 * <pre>
 *     0   1   2   3  ... (colunas)
 *   +---+---+---+---+
 * 0 |   |   |   |   |
 *   +---+---+---+---+
 * 1 |   | X |   |   |  <- Localizacao(1, 1)
 *   +---+---+---+---+
 * 2 |   |   |   |   |
 *   +---+---+---+---+
 * ...
 * (linhas)
 * </pre>
 * 
 * <p>
 * <strong>Uso Típico:</strong>
 * </p>
 * <ul>
 * <li>Posição de animais no campo</li>
 * <li>Identificação de células adjacentes</li>
 * <li>Navegação e movimento de atores</li>
 * <li>Mapeamento de obstáculos</li>
 * </ul>
 * 
 * @author David J. Barnes e Michael Kolling
 * @author Grupo 10
 * @version 2002-04-09 (traduzido e expandido)
 * @see Campo
 * @see Animal
 */
public class Localizacao {

    // ========== ATRIBUTOS ==========

    /**
     * Coordenada vertical (índice da linha na grade).
     * <p>
     * Convenção: linha 0 é o topo, valores aumentam para baixo.
     * </p>
     */
    private int linha;

    /**
     * Coordenada horizontal (índice da coluna na grade).
     * <p>
     * Convenção: coluna 0 é a esquerda, valores aumentam para direita.
     * </p>
     */
    private int coluna;

    // ========== CONSTRUTOR ==========

    /**
     * Cria uma nova localização com as coordenadas especificadas.
     * <p>
     * As coordenadas são armazenadas como valores imutáveis.
     * Não há validação de limites - responsabilidade do chamador.
     * </p>
     * 
     * @param linha  Coordenada Y (índice da linha, 0-based)
     * @param coluna Coordenada X (índice da coluna, 0-based)
     */
    public Localizacao(int linha, int coluna) {
        this.linha = linha;
        this.coluna = coluna;
    }

    // ========== MÉTODOS DE COMPARAÇÃO ==========

    /**
     * Verifica igualdade baseada no conteúdo (linha e coluna).
     * <p>
     * Duas localizações são consideradas iguais se, e somente se,
     * suas linhas e colunas forem idênticas.
     * </p>
     * <p>
     * <strong>Exemplo:</strong>
     * 
     * <pre>
     * Localizacao loc1 = new Localizacao(5, 10);
     * Localizacao loc2 = new Localizacao(5, 10);
     * loc1.equals(loc2); // retorna true
     * </pre>
     * </p>
     * 
     * @param obj Objeto a ser comparado
     * @return true se o objeto for uma Localizacao com mesmas coordenadas,
     *         false caso contrário
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Localizacao) {
            Localizacao outra = (Localizacao) obj;
            return linha == outra.getLinha() && coluna == outra.getColuna();
        } else {
            return false;
        }
    }

    /**
     * Gera um código hash único baseado nas coordenadas.
     * <p>
     * <strong>Algoritmo:</strong> Usa os 16 bits superiores para a linha
     * e os 16 bits inferiores para a coluna, criando um inteiro único.
     * </p>
     * <p>
     * <strong>Limitação:</strong> Em grades maiores que 65536x65536,
     * pode haver colisões de hash. Para a maioria das simulações,
     * este algoritmo é mais que suficiente.
     * </p>
     * <p>
     * Este método é essencial para uso em {@link java.util.HashMap}
     * e {@link java.util.HashSet}.
     * </p>
     * 
     * @return Código hash único para estas coordenadas
     */
    @Override
    public int hashCode() {
        return (linha << 16) + coluna;
    }

    // ========== MÉTODOS DE REPRESENTAÇÃO ==========

    /**
     * Retorna uma representação textual da localização.
     * <p>
     * Formato: {@code "linha,coluna"}
     * </p>
     * <p>
     * <strong>Exemplo:</strong> {@code "5,10"} para linha 5, coluna 10.
     * </p>
     * <p>
     * Útil para debug, logging e exibição de informações ao usuário.
     * </p>
     * 
     * @return String no formato "linha,coluna"
     */
    @Override
    public String toString() {
        return linha + "," + coluna;
    }

    // ========== GETTERS ==========

    /**
     * Retorna a coordenada da linha.
     * 
     * @return Índice da linha (coordenada Y)
     */
    public int getLinha() {
        return linha;
    }

    /**
     * Retorna a coordenada da coluna.
     * 
     * @return Índice da coluna (coordenada X)
     */
    public int getColuna() {
        return coluna;
    }
}
