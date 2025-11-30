import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

/**
 * Classe que representa o campo de simulação onde o ecossistema se desenvolve.
 * <p>
 * O campo é implementado como uma grade retangular bidimensional que gerencia
 * tanto atores (animais e obstáculos) quanto vegetação. Implementa as
 * interfaces
 * {@link GradeVisualizavel} e {@link CampoInterativo} para separar
 * responsabilidades
 * de leitura e modificação.
 * </p>
 * 
 * <p>
 * <strong>Estrutura de Dados:</strong>
 * </p>
 * <ul>
 * <li><strong>Matriz de Atores:</strong> Armazena animais e obstáculos (um por
 * célula)</li>
 * <li><strong>Matriz de Vegetação:</strong> Armazena estado de crescimento da
 * grama (independente)</li>
 * </ul>
 * 
 * <p>
 * <strong>Características Principais:</strong>
 * </p>
 * <ul>
 * <li>Cada célula pode conter no máximo um ator (animal ou obstáculo)</li>
 * <li>Vegetação existe em todas as células, independente de atores</li>
 * <li>Navegação por vizinhança de Moore (8 células adjacentes)</li>
 * <li>Busca aleatória de células livres para movimento</li>
 * </ul>
 * 
 * <p>
 * <strong>Sistema de Vegetação:</strong>
 * </p>
 * <p>
 * Cada célula possui uma instância de {@link Vegetacao} que:
 * <ul>
 * <li>Cresce gradualmente a cada turno (exceto sob obstáculos)</li>
 * <li>Pode ser consumida por herbívoros quando madura</li>
 * <li>Regenera após ser consumida</li>
 * </ul>
 * </p>
 * 
 * <p>
 * <strong>Double Buffering:</strong>
 * </p>
 * <p>
 * O simulador usa dois campos alternados:
 * <ol>
 * <li>Campo atual: leitura do estado presente</li>
 * <li>Campo atualizado: escrita do próximo estado</li>
 * <li>Vegetação é copiada entre campos via {@link #copiarGramaDe(Campo)}</li>
 * </ol>
 * </p>
 * 
 * @author David J. Barnes e Michael Kolling
 * @author Grupo 10 - PPOO
 * @version 2025-11
 * @see GradeVisualizavel
 * @see CampoInterativo
 * @see Vegetacao
 * @see Localizacao
 */
public class Campo implements GradeVisualizavel, CampoInterativo {
    // ========== ATRIBUTOS ESTÁTICOS ==========

    /**
     * Gerador de números aleatórios compartilhado para embaralhar adjacências.
     */
    private static final Random aleatorio = new Random();

    // ========== ATRIBUTOS DE DIMENSÃO ==========

    /**
     * Número de linhas do campo (altura).
     */
    private int profundidade;

    /**
     * Número de colunas do campo (largura).
     */
    private int largura;

    // ========== MATRIZES DE DADOS ==========

    /**
     * Matriz que armazena atores (animais e obstáculos) no campo.
     * <p>
     * Cada célula pode conter:
     * <ul>
     * <li>{@link Animal} - Predadores ou herbívoros</li>
     * <li>{@link Obstaculo} - Rios ou pedras</li>
     * <li>{@code null} - Célula vazia</li>
     * </ul>
     * </p>
     */
    private Object[][] campo;

    /**
     * Matriz que armazena o estado de vegetação em cada célula.
     * <p>
     * Cada posição contém uma instância de {@link Vegetacao} com
     * seu próprio nível de crescimento, independente de atores.
     * </p>
     */
    private Vegetacao[][] vegetacao;

    // ========== CONSTRUTOR ==========

    /**
     * Cria um novo campo com as dimensões especificadas.
     * <p>
     * <strong>Inicialização:</strong>
     * <ol>
     * <li>Cria matriz de atores (todas vazias)</li>
     * <li>Cria matriz de vegetação (todas maduras)</li>
     * <li>Inicializa cada célula com vegetação no nível máximo</li>
     * </ol>
     * </p>
     * 
     * @param profundidade Número de linhas do campo (altura)
     * @param largura      Número de colunas do campo (largura)
     */
    public Campo(int profundidade, int largura) {
        this.profundidade = profundidade;
        this.largura = largura;
        campo = new Object[profundidade][largura];
        vegetacao = new Vegetacao[profundidade][largura];

        for (int i = 0; i < profundidade; i++) {
            for (int j = 0; j < largura; j++) {
                vegetacao[i][j] = new Vegetacao();
            }
        }
    }

    // ========== MÉTODOS DE VEGETAÇÃO ==========

    /**
     * Faz a vegetação crescer em todo o campo.
     * <p>
     * <strong>Regras de crescimento:</strong>
     * <ul>
     * <li>Vegetação cresce em células vazias ou com animais</li>
     * <li>Vegetação NÃO cresce sob obstáculos (rios, pedras)</li>
     * <li>Cada célula avança um nível de crescimento por chamada</li>
     * </ul>
     * </p>
     * <p>
     * Este método é chamado uma vez por turno em clima normal,
     * ou duas vezes em clima chuvoso (crescimento acelerado).
     * </p>
     */
    public void crescerGrama() {
        for (int i = 0; i < profundidade; i++) {
            for (int j = 0; j < largura; j++) {
                Object obj = getObjetoEm(i, j);
                boolean ehObstaculo = (obj instanceof Obstaculo);

                if (!ehObstaculo) {
                    vegetacao[i][j].crescer();
                }
            }
        }
    }

    /**
     * Tenta consumir vegetação em uma localização específica.
     * <p>
     * <strong>Comportamento:</strong>
     * <ul>
     * <li>Se a grama está madura: consome e retorna valor nutricional</li>
     * <li>Se a grama não está madura: retorna 0 (sem efeito)</li>
     * <li>Grama consumida volta ao nível 0 de crescimento</li>
     * </ul>
     * </p>
     * <p>
     * Usado exclusivamente por herbívoros durante alimentação.
     * </p>
     * 
     * @param localizacao Posição onde tentar consumir vegetação
     * @return Valor nutricional obtido, ou 0 se não havia grama madura
     */
    public int comerGrama(Localizacao localizacao) {
        int lin = localizacao.getLinha();
        int col = localizacao.getColuna();

        return vegetacao[lin][col].vegetacaoEhComida();
    }

    /**
     * Verifica se há vegetação madura em uma célula específica.
     * <p>
     * Usado pela visualização para determinar cor da célula
     * (grama madura = verde escuro, grama nova = verde claro).
     * </p>
     * 
     * @param linha  Coordenada Y da célula
     * @param coluna Coordenada X da célula
     * @return true se a grama está madura (comestível), false caso contrário
     */
    public boolean temGramaMadura(int linha, int coluna) {
        return vegetacao[linha][coluna].estaMadura();
    }

    /**
     * Copia o estado de vegetação de outro campo para este campo.
     * <p>
     * <strong>Essencial para double buffering:</strong><br>
     * O simulador alterna entre dois campos a cada turno. A vegetação
     * precisa ser preservada copiando seu estado do campo anterior.
     * </p>
     * <p>
     * Apenas o nível de crescimento é copiado, não as instâncias.
     * </p>
     * 
     * @param outroCampo Campo fonte de onde copiar o estado da vegetação
     */
    public void copiarGramaDe(Campo outroCampo) {
        for (int i = 0; i < profundidade; i++) {
            for (int j = 0; j < largura; j++) {
                int nivelOutro = outroCampo.vegetacao[i][j].getNivelCrescimento();
                this.vegetacao[i][j].setNivelCrescimento(nivelOutro);
            }
        }
    }

    // ========== MÉTODOS DE GERENCIAMENTO DO CAMPO ==========

    /**
     * Remove todos os atores do campo, deixando apenas vegetação.
     * <p>
     * <strong>Operações:</strong>
     * <ul>
     * <li>Define todas as células da matriz de atores como null</li>
     * <li>Mantém a vegetação intacta</li>
     * <li>Mantém as dimensões do campo</li>
     * </ul>
     * </p>
     * <p>
     * Usado para preparar o campo de destino no double buffering.
     * </p>
     */
    public void limpar() {
        for (int linha = 0; linha < profundidade; linha++) {
            for (int coluna = 0; coluna < largura; coluna++) {
                campo[linha][coluna] = null;
            }
        }
    }

    // ========== MÉTODOS DE POSICIONAMENTO ==========

    /**
     * Coloca um ator em uma localização específica usando coordenadas diretas.
     * <p>
     * Sobrecarga de conveniência de {@link #colocar(Object, Localizacao)}.
     * </p>
     * 
     * @param animal Ator a ser posicionado (animal ou obstáculo)
     * @param linha  Coordenada Y (índice da linha)
     * @param coluna Coordenada X (índice da coluna)
     */
    public void colocar(Object animal, int linha, int coluna) {
        colocar(animal, new Localizacao(linha, coluna));
    }

    /**
     * Coloca um ator em uma localização específica.
     * <p>
     * <strong>Importante:</strong> Não verifica se a célula já está ocupada.
     * Sobrescreverá qualquer ator existente na posição.
     * </p>
     * 
     * @param animal      Ator a ser posicionado (Animal, Obstaculo, etc.)
     * @param localizacao Posição onde colocar o ator
     */
    public void colocar(Object animal, Localizacao localizacao) {
        campo[localizacao.getLinha()][localizacao.getColuna()] = animal;
    }

    // ========== MÉTODOS DE CONSULTA ==========

    /**
     * Retorna o ator presente em uma localização específica.
     * <p>
     * Sobrecarga que aceita objeto Localizacao ao invés de coordenadas.
     * </p>
     * 
     * @param localizacao Posição a ser consultada
     * @return Ator na posição, ou null se vazia
     */
    public Object getObjetoEm(Localizacao localizacao) {
        return getObjetoEm(localizacao.getLinha(), localizacao.getColuna());
    }

    /**
     * Retorna o ator presente em coordenadas específicas.
     * 
     * @param linha  Coordenada Y (índice da linha)
     * @param coluna Coordenada X (índice da coluna)
     * @return Ator na posição, ou null se vazia
     */
    public Object getObjetoEm(int linha, int coluna) {
        return campo[linha][coluna];
    }

    // ========== MÉTODOS DE NAVEGAÇÃO ==========

    /**
     * Busca uma localização adjacente livre (vazia).
     * <p>
     * <strong>Estratégia de busca:</strong>
     * <ol>
     * <li>Obtém lista de adjacentes em ordem aleatória</li>
     * <li>Retorna a primeira célula vazia encontrada</li>
     * <li>Se nenhuma adjacente está livre, tenta a própria posição</li>
     * <li>Se tudo está ocupado, retorna null</li>
     * </ol>
     * </p>
     * 
     * @param localizacao Posição central a partir da qual buscar
     * @return Localização adjacente livre, ou null se todas ocupadas
     */
    public Localizacao localizacaoAdjacenteLivre(Localizacao localizacao) {
        Iterator<Localizacao> adjacentes = localizacoesAdjacentes(localizacao);
        while (adjacentes.hasNext()) {
            Localizacao proxima = adjacentes.next();
            if (campo[proxima.getLinha()][proxima.getColuna()] == null) {
                return proxima;
            }
        }
        if (campo[localizacao.getLinha()][localizacao.getColuna()] == null) {
            return localizacao;
        } else {
            return null;
        }
    }

    /**
     * Retorna iterator sobre localizações adjacentes em ordem aleatória.
     * <p>
     * <strong>Vizinhança de Moore (8 células):</strong>
     * 
     * <pre>
     * NW  N  NE
     *  W  X  E
     * SW  S  SE
     * </pre>
     * 
     * Onde X é a posição central.
     * </p>
     * <p>
     * <strong>Características:</strong>
     * <ul>
     * <li>Inclui apenas células dentro dos limites do campo</li>
     * <li>Ordem é embaralhada para aleatoriedade</li>
     * <li>Não inclui a célula central</li>
     * </ul>
     * </p>
     * 
     * @param localizacao Posição central
     * @return Iterator sobre as localizações adjacentes válidas (embaralhadas)
     */
    public Iterator<Localizacao> localizacoesAdjacentes(Localizacao localizacao) {
        int linha = localizacao.getLinha();
        int coluna = localizacao.getColuna();
        LinkedList<Localizacao> locais = new LinkedList<Localizacao>();
        for (int deslocLinha = -1; deslocLinha <= 1; deslocLinha++) {
            int proxLinha = linha + deslocLinha;
            if (proxLinha >= 0 && proxLinha < profundidade) {
                for (int deslocCol = -1; deslocCol <= 1; deslocCol++) {
                    int proxColuna = coluna + deslocCol;
                    if (proxColuna >= 0 && proxColuna < largura && (deslocLinha != 0 || deslocCol != 0)) {
                        locais.add(new Localizacao(proxLinha, proxColuna));
                    }
                }
            }
        }
        Collections.shuffle(locais, aleatorio);
        return locais.iterator();
    }

    // ========== GETTERS ==========

    /**
     * Retorna a profundidade (altura) do campo em células.
     * 
     * @return Número de linhas do campo
     */
    public int getProfundidade() {
        return profundidade;
    }

    /**
     * Retorna a largura do campo em células.
     * 
     * @return Número de colunas do campo
     */
    public int getLargura() {
        return largura;
    }
}