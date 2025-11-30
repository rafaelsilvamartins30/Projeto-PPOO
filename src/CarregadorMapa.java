import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Classe responsável por carregar mapas de obstáculos a partir de arquivos de
 * texto.
 * <p>
 * Esta classe implementa o padrão <strong>Single Responsibility Principle
 * (SRP)</strong>,
 * separando a lógica de leitura de arquivos da lógica de simulação. Permite
 * criação
 * de cenários customizados através de arquivos de texto simples.
 * </p>
 * 
 * <p>
 * <strong>Formato do Arquivo de Mapa:</strong>
 * </p>
 * 
 * <pre>
 * Cada caractere representa uma célula do campo:
 * 
 * 'R' = Rio (obstáculo pescável)
 * 'P' = Pedra (obstáculo sólido)
 * ' ' = Espaço vazio (terreno livre)
 * 
 * Exemplo de mapa 5x10:
 * ..........
 * .RRR......
 * .RRR...PP.
 * .RRR...PP.
 * ..........
 * </pre>
 * 
 * <p>
 * <strong>Processo de Carregamento:</strong>
 * </p>
 * <ol>
 * <li>Lê todas as linhas do arquivo de texto</li>
 * <li>Determina dimensões (altura = nº linhas, largura = comprimento primeira
 * linha)</li>
 * <li>Cria matriz bidimensional de obstáculos</li>
 * <li>Converte cada caractere para o tipo de obstáculo correspondente</li>
 * <li>Retorna matriz preenchida ou null em caso de erro</li>
 * </ol>
 * 
 * <p>
 * <strong>Tratamento de Erros:</strong>
 * </p>
 * <ul>
 * <li>Arquivo não encontrado: imprime erro e retorna null</li>
 * <li>Linhas irregulares: preenche com null (terreno livre)</li>
 * <li>Caracteres desconhecidos: interpretados como terreno livre</li>
 * </ul>
 * 
 * <p>
 * <strong>Uso Típico:</strong>
 * </p>
 * 
 * <pre>
 * CarregadorMapa carregador = new CarregadorMapa();
 * Obstaculo[][] mapa = carregador.carregarObstaculos("mapa.txt");
 * if (mapa != null) {
 *     // aplicar mapa ao campo
 * }
 * </pre>
 * 
 * @author Grupo 10
 * @version 1.0
 * @see Obstaculo
 * @see Campo
 * @see Simulador
 */
public class CarregadorMapa {

    // ========== MÉTODOS PÚBLICOS ==========

    /**
     * Carrega um mapa de obstáculos a partir de um arquivo de texto.
     * <p>
     * <strong>Especificações do arquivo:</strong>
     * <ul>
     * <li>Formato: Texto plano (ASCII)</li>
     * <li>Cada linha representa uma linha do mapa</li>
     * <li>Cada caractere representa uma célula</li>
     * <li>Linhas podem ter comprimentos diferentes (preenchido com null)</li>
     * </ul>
     * </p>
     * <p>
     * <strong>Caracteres válidos:</strong>
     * <ul>
     * <li>{@code 'R'} → {@link Obstaculo#RIO}</li>
     * <li>{@code 'P'} → {@link Obstaculo#PEDRA}</li>
     * <li>Outros → {@code null} (terreno livre)</li>
     * </ul>
     * </p>
     * 
     * @param caminhoArquivo Caminho relativo ou absoluto do arquivo de mapa
     * @return Matriz bidimensional de obstáculos [linha][coluna], onde null
     *         representa células vazias, ou null se houver erro na leitura
     */
    public Obstaculo[][] carregarObstaculos(String caminhoArquivo) {
        try {
            // Lê todas as linhas do arquivo
            List<String> linhas = Files.readAllLines(Paths.get(caminhoArquivo));
            if (linhas.isEmpty())
                return null;

            // Determina dimensões do mapa
            int profundidade = linhas.size();
            int largura = linhas.get(0).length();

            Obstaculo[][] mapa = new Obstaculo[profundidade][largura];

            // Converte cada caractere para obstáculo
            for (int i = 0; i < profundidade; i++) {
                String linha = linhas.get(i);
                // Protege contra linhas irregulares (diferentes comprimentos)
                for (int j = 0; j < largura && j < linha.length(); j++) {
                    char c = linha.charAt(j);
                    mapa[i][j] = converterCaractere(c);
                }
            }
            return mapa;

        } catch (IOException e) {
            System.err.println("Erro ao ler mapa: " + e.getMessage());
            return null;
        }
    }

    // ========== MÉTODOS PRIVADOS ==========

    /**
     * Converte um caractere do arquivo para o tipo de obstáculo correspondente.
     * <p>
     * <strong>Mapeamento de caracteres:</strong>
     * <ul>
     * <li>{@code 'R'} → Rio (obstáculo pescável)</li>
     * <li>{@code 'P'} → Pedra (obstáculo sólido)</li>
     * <li>Qualquer outro → null (terreno livre para animais e vegetação)</li>
     * </ul>
     * </p>
     * <p>
     * Este método é case-sensitive. Caracteres minúsculos serão interpretados
     * como terreno livre.
     * </p>
     * 
     * @param c Caractere lido do arquivo de mapa
     * @return Tipo de obstáculo correspondente, ou null para terreno livre
     */
    private Obstaculo converterCaractere(char c) {
        switch (c) {
            case 'R':
                return Obstaculo.RIO;
            case 'P':
                return Obstaculo.PEDRA;
            default:
                return null; // Espaço vazio ou caractere desconhecido = terreno livre
        }
    }
}