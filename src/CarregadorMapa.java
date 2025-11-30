import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Responsável por carregar e gerenciar mapas de obstáculos a partir de arquivos
 * de texto.
 * Centraliza toda a lógica de leitura, validação e aplicação de mapas.
 * 
 * Formato do arquivo:
 * - 'R' = Rio
 * - 'P' = Pedra
 * - ' ' (espaço ou outro caractere) = Espaço vazio
 * 
 * @author Grupo 10
 * @version 2025-11-30
 */
public class CarregadorMapa {

    private Obstaculo[][] mapaAtual;
    private String caminhoAtual;

    /**
     * Carrega um mapa de obstáculos de um arquivo de texto.
     * 
     * @param caminhoArquivo Caminho para o arquivo de mapa
     * @return true se o mapa foi carregado com sucesso, false caso contrário
     */
    public boolean carregar(String caminhoArquivo) {
        try {
            this.mapaAtual = lerArquivo(caminhoArquivo);
            this.caminhoAtual = caminhoArquivo;
            System.out.println("✅ Mapa carregado: " + caminhoArquivo);
            return true;
        } catch (IOException e) {
            System.err.println("❌ Erro ao carregar mapa: " + e.getMessage());
            this.mapaAtual = null;
            this.caminhoAtual = null;
            return false;
        }
    }

    /**
     * Lê um arquivo e converte em matriz de obstáculos.
     */
    private Obstaculo[][] lerArquivo(String caminhoArquivo) throws IOException {
        List<String> linhas = Files.readAllLines(Paths.get(caminhoArquivo));

        if (linhas.isEmpty()) {
            return null;
        }

        int altura = linhas.size();
        int largura = calcularLarguraMaxima(linhas);

        System.out.println("📏 Dimensões detectadas: " + altura + " linhas × " + largura + " colunas");

        Obstaculo[][] mapa = new Obstaculo[altura][largura];

        for (int i = 0; i < altura; i++) {
            String linha = linhas.get(i);

            for (int j = 0; j < linha.length(); j++) {
                char c = linha.charAt(j);

                if (c == 'R') {
                    mapa[i][j] = Obstaculo.RIO;
                } else if (c == 'P') {
                    mapa[i][j] = Obstaculo.PEDRA;
                }
            }
        }

        return mapa;
    }

    /**
     * Encontra a linha mais larga do arquivo.
     */
    private int calcularLarguraMaxima(List<String> linhas) {
        int largura = 0;
        for (String linha : linhas) {
            if (linha.length() > largura) {
                largura = linha.length();
            }
        }
        return largura;
    }

    /**
     * Aplica o mapa carregado em um campo.
     * 
     * @param campo Campo onde os obstáculos serão aplicados
     * @return true se o mapa foi aplicado, false se não há mapa carregado
     */
    public boolean aplicarAoCampo(Campo campo) {
        if (!temMapaCarregado()) {
            System.out.println("⚠️ Nenhum mapa carregado para aplicar");
            return false;
        }

        int obstaculosAplicados = 0;

        for (int i = 0; i < campo.getProfundidade() && i < mapaAtual.length; i++) {
            for (int j = 0; j < campo.getLargura() && j < mapaAtual[i].length; j++) {
                if (mapaAtual[i][j] != null) {
                    campo.colocar(mapaAtual[i][j], i, j);
                    obstaculosAplicados++;
                }
            }
        }

        System.out.println("🗺️ " + obstaculosAplicados + " obstáculos aplicados ao campo");
        return true;
    }

    /**
     * Verifica se há um mapa carregado.
     */
    public boolean temMapaCarregado() {
        return mapaAtual != null;
    }

    /**
     * Retorna a altura do mapa carregado.
     */
    public int getAltura() {
        return temMapaCarregado() ? mapaAtual.length : 0;
    }

    /**
     * Retorna a largura do mapa carregado.
     */
    public int getLargura() {
        return temMapaCarregado() && mapaAtual.length > 0 ? mapaAtual[0].length : 0;
    }

    /**
     * Retorna o caminho do mapa atualmente carregado.
     */
    public String getCaminhoAtual() {
        return caminhoAtual;
    }

    /**
     * Limpa o mapa carregado.
     */
    public void limpar() {
        this.mapaAtual = null;
        this.caminhoAtual = null;
        System.out.println("🧹 Mapa limpo");
    }

}