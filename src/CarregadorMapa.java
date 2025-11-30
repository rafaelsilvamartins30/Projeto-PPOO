import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Responsável por ler arquivos de texto e convertê-los em uma matriz de Obstaculos.
 */
public class CarregadorMapa {
    
    /**
     * Lê o arquivo e retorna uma matriz de Obstaculos correspondente.
     * @param caminhoArquivo O caminho do arquivo mapa.txt
     * @return Uma matriz bidimensional de Obstaculos (pode conter null onde é grama).
     * Retorna null se houver erro na leitura.
     */
    public Obstaculo[][] carregarObstaculos(String caminhoArquivo) {
        try {
            List<String> linhas = Files.readAllLines(Paths.get(caminhoArquivo));
            if (linhas.isEmpty()) return null;

            int profundidade = linhas.size();
            int largura = linhas.get(0).length();

            Obstaculo[][] mapa = new Obstaculo[profundidade][largura];

            for (int i = 0; i < profundidade; i++) {
                String linha = linhas.get(i);
                // Garante que não vamos ler além do tamanho da linha (caso o arquivo seja irregular)
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

    /**
     * Traduz um caractere do arquivo para um Enum Obstaculo.
     */
    private Obstaculo converterCaractere(char c) {
        switch (c) {
            case 'R': return Obstaculo.RIO;
            case 'P': return Obstaculo.PEDRA;
            default:  return null; // Espaço vazio ou caractere desconhecido é terreno livre
        }
    }
}