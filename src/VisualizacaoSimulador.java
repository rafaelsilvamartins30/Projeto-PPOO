import java.awt.*;
import java.awt.event.ActionListener;
import java.util.HashMap;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class VisualizacaoSimulador extends JFrame implements Desenhavel
{
    private static final Color COR_VAZIA = Color.white;
    private static final Color COR_DESCONHECIDA = Color.gray;

    private final String PREFIXO_PASSO = "Passo: ";
    private final String PREFIXO_POPULACAO = "População: ";
    
    private JLabel rotuloPasso;
    private JLabel rotuloPopulacao;
    private JLabel rotuloClima;
    private JButton botaoPausar;
    private JButton botaoReiniciar;
    
    private VisaoCampo visaoCampo;
    private HashMap cores;
    private EstatisticasCampo estatisticas;

    public VisualizacaoSimulador(int altura, int largura)
    {
        estatisticas = new EstatisticasCampo();
        cores = new HashMap();

        setTitle("Simulador Ecossistema - Grupo 10");
        setLocation(100, 50);
        
        // 1. Painel Superior
        JPanel painelSuperior = new JPanel(new GridLayout(1, 2));
        rotuloPasso = new JLabel(PREFIXO_PASSO, JLabel.CENTER);
        rotuloClima = new JLabel("Clima: NORMAL", JLabel.CENTER);
        rotuloClima.setOpaque(true);
        rotuloClima.setBackground(new Color(135, 206, 235));
        
        painelSuperior.add(rotuloPasso);
        painelSuperior.add(rotuloClima);
        
        // 2. Painel Central
        visaoCampo = new VisaoCampo(altura, largura);
        
        // 3. Painel Inferior
        JPanel painelInferior = new JPanel(new BorderLayout());
        rotuloPopulacao = new JLabel(PREFIXO_POPULACAO, JLabel.CENTER);
        rotuloPopulacao.setBorder(new EmptyBorder(10, 0, 10, 0));
        
        JPanel painelBotoes = new JPanel(new FlowLayout());
        botaoPausar = new JButton("Pausar");
        botaoReiniciar = new JButton("Resetar Simulação");
        
        botaoPausar.setPreferredSize(new Dimension(100, 30));
        botaoReiniciar.setPreferredSize(new Dimension(150, 30));
        
        painelBotoes.add(botaoPausar);
        painelBotoes.add(botaoReiniciar);
        
        painelInferior.add(rotuloPopulacao, BorderLayout.NORTH);
        painelInferior.add(painelBotoes, BorderLayout.SOUTH);

        Container conteudo = getContentPane();
        conteudo.add(painelSuperior, BorderLayout.NORTH);
        conteudo.add(visaoCampo, BorderLayout.CENTER);
        conteudo.add(painelInferior, BorderLayout.SOUTH);
        
        pack();
        setVisible(true);
    }
    
    // --- IMPLEMENTAÇÃO SEM LAMBDAS (APENAS REPASSA O LISTENER) ---

    @Override
    public void setInfoClima(String texto, boolean chuvoso) {
        rotuloClima.setText(texto);
        if(chuvoso) {
            rotuloClima.setBackground(new Color(70, 130, 180)); // Azul escuro
            rotuloClima.setForeground(Color.WHITE);
            // Ícones são opcionais, se der erro pode remover as linhas .setIcon
            rotuloClima.setIcon(UIManager.getIcon("OptionPane.informationIcon"));
        } else {
            rotuloClima.setBackground(new Color(255, 255, 224)); // Amarelo claro
            rotuloClima.setForeground(Color.BLACK);
            rotuloClima.setIcon(UIManager.getIcon("OptionPane.warningIcon"));
        }
    }

    @Override
    public void setAcaoPausar(ActionListener listener) {
        // Loop clássico em vez de stream
        ActionListener[] listeners = botaoPausar.getActionListeners();
        for(int i=0; i<listeners.length; i++) {
             botaoPausar.removeActionListener(listeners[i]);
        }
        botaoPausar.addActionListener(listener);
    }

    @Override
    public void setAcaoReiniciar(ActionListener listener) {
        ActionListener[] listeners = botaoReiniciar.getActionListeners();
        for(int i=0; i<listeners.length; i++) {
             botaoReiniciar.removeActionListener(listeners[i]);
        }
        botaoReiniciar.addActionListener(listener);
    }
    
    @Override
    public void setTextoBotaoPausa(String texto) {
        botaoPausar.setText(texto);
    }

    // --- MÉTODOS EXISTENTES ---

    @Override
    public void definirCor(Class<?> classeAnimal, Color cor) {
        cores.put(classeAnimal, cor);
    }

    private Color getCor(Class classeAnimal) {
        Color c = (Color) cores.get(classeAnimal);
        if(c == null) return COR_DESCONHECIDA;
        else return c;
    }

    @Override
    public void mostrarStatus(int passo, Campo campo) {
        if(!isVisible()) setVisible(true);

        rotuloPasso.setText(PREFIXO_PASSO + passo);
        estatisticas.reiniciar();
        visaoCampo.prepararPintura();
            
        for(int linha = 0; linha < campo.getProfundidade(); linha++) {
            for(int coluna = 0; coluna < campo.getLargura(); coluna++) {
                Object animal = campo.getObjetoEm(linha, coluna);
                if(animal != null) {
                    if(!(animal instanceof Obstaculo)) {
                        estatisticas.incrementarContagem(animal.getClass());
                    }
                    
                    Color cor;
                    if (animal instanceof Obstaculo) {
                        if (animal == Obstaculo.RIO) cor = Color.CYAN;
                        else cor = Color.DARK_GRAY;
                    } else {
                        cor = getCor(animal.getClass());
                    }
                    visaoCampo.desenharMarca(coluna, linha, cor);
                }
                else {
                    // Desenha grama ou terra
                    if(campo.temGramaMadura(linha, coluna)) {
                        visaoCampo.desenharMarca(coluna, linha, new Color(200, 255, 200)); 
                    } else {
                        visaoCampo.desenharMarca(coluna, linha, new Color(240, 230, 140)); 
                    }
                }
            }
        }
        estatisticas.contagemConcluida();
        rotuloPopulacao.setText(PREFIXO_POPULACAO + estatisticas.getDetalhesPopulacao(campo));
        visaoCampo.repaint();
    }

    @Override
    public boolean ehViavel(Campo campo) {
        return estatisticas.ehViavel(campo);
    }
    
    @Override
    public void reiniciar() {
        estatisticas.reiniciar();
        visaoCampo.prepararPintura();
        visaoCampo.repaint();
    }

    @Override
    public void fechar() {
        dispose();
    }
    
    private class VisaoCampo extends JPanel {
        private final int FATOR_ESCALA_GRADE = 6;
        private int larguraGrade, alturaGrade;
        private int escalaX, escalaY;
        Dimension tamanho;
        private Graphics g;
        private Image imagemCampo;

        public VisaoCampo(int altura, int largura) {
            alturaGrade = altura;
            larguraGrade = largura;
            tamanho = new Dimension(0, 0);
        }

        public Dimension getPreferredSize() {
            return new Dimension(larguraGrade * FATOR_ESCALA_GRADE, alturaGrade * FATOR_ESCALA_GRADE);
        }
        
        public void prepararPintura() {
            if(!tamanho.equals(getSize())) {
                tamanho = getSize();
                imagemCampo = visaoCampo.createImage(tamanho.width, tamanho.height);
                g = imagemCampo.getGraphics();

                escalaX = tamanho.width / larguraGrade;
                if(escalaX < 1) escalaX = FATOR_ESCALA_GRADE;
                escalaY = tamanho.height / alturaGrade;
                if(escalaY < 1) escalaY = FATOR_ESCALA_GRADE;
            }
        }
        
        public void desenharMarca(int x, int y, Color cor) {
            g.setColor(cor);
            g.fillRect(x * escalaX, y * escalaY, escalaX - 1, escalaY - 1);
        }

        public void paintComponent(Graphics g) {
            if(imagemCampo != null) {
                g.drawImage(imagemCampo, 0, 0, null);
            }
        }
    }
}