import java.awt.*;
import javax.swing.*;
import java.util.HashMap;

/**
 * Fornece uma visualização gráfica de um simulador de predador e presa.
 * A visualização exibe o estado atual do campo, incluindo as populações.
 * Permite definir cores para diferentes classes de animais.
 * Também verifica se a simulação ainda é viável (ou seja, se há mais de uma espécie viva).
 * Pode reiniciar a visualização para um novo início de simulação. 
 * 
 * @author David J. Barnes e Michael Kolling
 * @version 2002-04-23 (traduzido)
 */
public class VisualizacaoSimulador extends JFrame implements Desenhavel
{
    // Cor usada para locais vazios.
    private static final Color COR_VAZIA = Color.white;

    // Cor usada para objetos que não possuem uma cor definida.
    private static final Color COR_DESCONHECIDA = Color.gray;

    private final String PREFIXO_PASSO = "Passo: ";
    private final String PREFIXO_POPULACAO = "População: ";
    private JLabel rotuloPasso, rotuloPopulacao;
    private VisaoCampo visaoCampo;
    
    // Mapa de cores para as classes dos animais.
    private HashMap cores;
    // Objeto de estatísticas que calcula e armazena informações da simulação.
    private EstatisticasCampo estatisticas;

    /**
     * Cria uma visualização com a altura e largura fornecidas.
     * @param altura Altura do campo.
     * @param largura Largura do campo.
     */
    public VisualizacaoSimulador(int altura, int largura)
    {
        estatisticas = new EstatisticasCampo();
        cores = new HashMap();

        setTitle("Simulação de Raposas e Coelhos");
        rotuloPasso = new JLabel(PREFIXO_PASSO, JLabel.CENTER);
        rotuloPopulacao = new JLabel(PREFIXO_POPULACAO, JLabel.CENTER);
        
        setLocation(100, 50);
        
        visaoCampo = new VisaoCampo(altura, largura);

        Container conteudo = getContentPane();
        conteudo.add(rotuloPasso, BorderLayout.NORTH);
        conteudo.add(visaoCampo, BorderLayout.CENTER);
        conteudo.add(rotuloPopulacao, BorderLayout.SOUTH);
        pack();
        setVisible(true);
    }
    
    /**
     * Define uma cor a ser usada para uma determinada classe de animal.
     * @param classeAnimal A classe do animal.
     * @param cor A cor correspondente.
     */
    @Override
    public void definirCor(Class<?> classeAnimal, Color cor)
    {
        cores.put(classeAnimal, cor);
    }

    /**
     * Retorna a cor associada a uma determinada classe de animal.
     * @param classeAnimal A classe do animal.
     * @return A cor correspondente, ou uma cor padrão se não houver.
     */
    private Color getCor(Class classeAnimal)
    {
        Color c = (Color) cores.get(classeAnimal);
        if(c == null) {
            // nenhuma cor definida para esta classe
            return COR_DESCONHECIDA;
        }
        else {
            return c;
        }
    }

    /**
     * Mostra o estado atual do campo.
     * @param passo Qual iteração (passo) está sendo exibida.
     * @param campo O estado atual do campo.
     */
    @Override
    public void mostrarStatus(int passo, Campo campo)
    {
        if(!isVisible())
            setVisible(true);

        rotuloPasso.setText(PREFIXO_PASSO + passo);

        estatisticas.reiniciar();
        visaoCampo.prepararPintura();
            
        for(int linha = 0; linha < campo.getProfundidade(); linha++) {
            for(int coluna = 0; coluna < campo.getLargura(); coluna++) {
                Object animal = campo.getObjetoEm(linha, coluna);
                if(animal != null) {
                    estatisticas.incrementarContagem(animal.getClass());
                    visaoCampo.desenharMarca(coluna, linha, getCor(animal.getClass()));
                }
                else {
                    visaoCampo.desenharMarca(coluna, linha, COR_VAZIA);
                }
            }
        }
        estatisticas.contagemConcluida();

        rotuloPopulacao.setText(PREFIXO_POPULACAO + estatisticas.getDetalhesPopulacao(campo));
        visaoCampo.repaint();
    }

    /**
     * Determina se a simulação ainda deve continuar sendo executada.
     * @param campo O campo atual.
     * @return Verdadeiro se houver mais de uma espécie viva.
     */
    @Override
    public boolean ehViavel(Campo campo)
    {
        return estatisticas.ehViavel(campo);
    }
    
    /**
     * Reinicia a visualização.
     */
    @Override
    public void reiniciar()
    {
        estatisticas.reiniciar();
        visaoCampo.prepararPintura();
        visaoCampo.repaint();
    }

    /**
     * Fornece uma visualização gráfica de um campo retangular.
     * Esta é uma classe interna que define um componente personalizado
     * da interface gráfica responsável por exibir o campo.
     * 
     * Isso é um recurso de GUI mais avançado — pode ser ignorado
     * se desejar apenas estudar a lógica da simulação.
     */
    private class VisaoCampo extends JPanel
    {
        private final int FATOR_ESCALA_GRADE = 6;

        private int larguraGrade, alturaGrade;
        private int escalaX, escalaY;
        Dimension tamanho;
        private Graphics g;
        private Image imagemCampo;

        /**
         * Cria um novo componente de visualização do campo.
         * @param altura Altura da grade.
         * @param largura Largura da grade.
         */
        public VisaoCampo(int altura, int largura)
        {
            alturaGrade = altura;
            larguraGrade = largura;
            tamanho = new Dimension(0, 0);
        }

        /**
         * Informa ao gerenciador de interface gráfica o tamanho preferido.
         */
        public Dimension getPreferredSize()
        {
            return new Dimension(larguraGrade * FATOR_ESCALA_GRADE,
                                 alturaGrade * FATOR_ESCALA_GRADE);
        }
        
        /**
         * Prepara para uma nova rodada de pintura. Como o componente
         * pode ser redimensionado, o fator de escala é recalculado.
         */
        public void prepararPintura()
        {
            if(!tamanho.equals(getSize())) {  // se o tamanho mudou
                tamanho = getSize();
                imagemCampo = visaoCampo.createImage(tamanho.width, tamanho.height);
                g = imagemCampo.getGraphics();

                escalaX = tamanho.width / larguraGrade;
                if(escalaX < 1) {
                    escalaX = FATOR_ESCALA_GRADE;
                }
                escalaY = tamanho.height / alturaGrade;
                if(escalaY < 1) {
                    escalaY = FATOR_ESCALA_GRADE;
                }
            }
        }
        
        /**
         * Pinta uma posição da grade com a cor fornecida.
         * @param x Coluna.
         * @param y Linha.
         * @param cor Cor a ser usada.
         */
        public void desenharMarca(int x, int y, Color cor)
        {
            g.setColor(cor);
            g.fillRect(x * escalaX, y * escalaY, escalaX - 1, escalaY - 1);
        }

        /**
         * Reexibe o componente visualizando a imagem interna na tela.
         */
        public void paintComponent(Graphics g)
        {
            if(imagemCampo != null) {
                g.drawImage(imagemCampo, 0, 0, null);
            }
        }
    }
}
