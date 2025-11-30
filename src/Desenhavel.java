import java.awt.Color;
import java.awt.event.ActionListener;

/**
 * Interface que define o contrato para visualizações gráficas da simulação.
 * <p>
 * Esta interface desacopla a lógica de simulação da apresentação visual,
 * implementando o padrão <strong>Model-View-Controller (MVC)</strong>.
 * Qualquer implementação desta interface pode ser usada para visualizar
 * a simulação sem modificar a lógica do simulador.
 * </p>
 * 
 * <p>
 * <strong>Responsabilidades da Interface:</strong>
 * </p>
 * <ul>
 * <li>Renderização visual do estado do campo</li>
 * <li>Exibição de estatísticas populacionais</li>
 * <li>Configuração de cores para entidades</li>
 * <li>Controles de simulação (pausar, continuar, reiniciar)</li>
 * <li>Exibição de informações climáticas</li>
 * <li>Verificação de viabilidade do ecossistema</li>
 * </ul>
 * 
 * <p>
 * <strong>Padrões de Design Aplicados:</strong>
 * </p>
 * <ul>
 * <li><strong>MVC:</strong> Separa apresentação (View) de lógica
 * (Model/Controller)</li>
 * <li><strong>Observer:</strong> View observa mudanças no Model via
 * callbacks</li>
 * <li><strong>Strategy:</strong> Permite diferentes estratégias de
 * visualização</li>
 * </ul>
 * 
 * <p>
 * <strong>Implementações Existentes:</strong>
 * </p>
 * <ul>
 * <li>{@link VisualizacaoSimulador} - Interface gráfica Swing completa</li>
 * </ul>
 * 
 * <p>
 * <strong>Fluxo de Interação Típico:</strong>
 * </p>
 * <ol>
 * <li>Simulador cria a visualização e configura cores</li>
 * <li>A cada passo: {@link #mostrarStatus(int, GradeVisualizavel)}</li>
 * <li>Usuário interage via botões (callbacks configurados)</li>
 * <li>Simulador consulta {@link #ehViavel(GradeVisualizavel)}</li>
 * <li>Ao fim: {@link #fechar()} libera recursos</li>
 * </ol>
 * 
 * @author Grupo 10
 * @version 1.0
 * @see VisualizacaoSimulador
 * @see Simulador
 * @see GradeVisualizavel
 */
public interface Desenhavel {

    // ========== MÉTODOS DE CONFIGURAÇÃO VISUAL ==========

    /**
     * Define a cor de renderização para uma classe ou objeto específico.
     * <p>
     * Este método permite mapear cores para:
     * <ul>
     * <li><strong>Classes:</strong>
     * {@code definirCor(Raposa.class, Color.BLUE)}</li>
     * <li><strong>Objetos individuais:</strong>
     * {@code definirCor(Obstaculo.RIO, Color.CYAN)}</li>
     * </ul>
     * </p>
     * <p>
     * <strong>Ordem de precedência:</strong><br>
     * Cores de objetos específicos têm prioridade sobre cores de classes.
     * </p>
     * 
     * @param chave Classe (ex: Animal.class) ou objeto (ex: enum) a ser colorido
     * @param cor   Cor a ser usada na renderização visual
     */
    void definirCor(Object chave, Color cor);

    // ========== MÉTODOS DE ATUALIZAÇÃO DE ESTADO ==========

    /**
     * Atualiza a visualização com o estado atual da simulação.
     * <p>
     * Este método é chamado a cada passo da simulação e deve:
     * <ol>
     * <li>Atualizar o número do passo exibido</li>
     * <li>Coletar estatísticas populacionais</li>
     * <li>Renderizar o campo visual</li>
     * <li>Atualizar legendas e contadores</li>
     * </ol>
     * </p>
     * <p>
     * <strong>Performance:</strong> Este método é chamado frequentemente,
     * então deve ser otimizado (ex: usar double buffering).
     * </p>
     * 
     * @param passo Número do passo atual da simulação
     * @param campo Interface de acesso ao estado do campo
     */
    void mostrarStatus(int passo, GradeVisualizavel campo);

    /**
     * Atualiza as informações climáticas na interface.
     * <p>
     * Ajusta visual do indicador de clima baseado no estado:
     * <ul>
     * <li><strong>Chuvoso:</strong> Ícone/cor indicando chuva</li>
     * <li><strong>Normal:</strong> Ícone/cor indicando tempo bom</li>
     * </ul>
     * </p>
     * 
     * @param texto   Texto descritivo do clima (ex: "Clima: CHUVOSO")
     * @param chuvoso true se está chovendo, false caso contrário
     */
    void setInfoClima(String texto, boolean chuvoso);

    /**
     * Define o texto exibido no botão de pausa/continuar.
     * <p>
     * Permite alternar dinamicamente entre "Pausar" e "Continuar"
     * baseado no estado atual da simulação.
     * </p>
     * 
     * @param texto Novo texto do botão (ex: "Pausar" ou "Continuar")
     */
    void setTextoBotaoPausa(String texto);

    // ========== MÉTODOS DE CONTROLE DE SIMULAÇÃO ==========

    /**
     * Configura o listener para o botão de pausar/continuar.
     * <p>
     * O listener será notificado quando o usuário clicar no botão,
     * permitindo que o simulador pause ou continue a execução.
     * </p>
     * 
     * @param listener ActionListener a ser chamado ao clicar no botão
     */
    void setAcaoPausar(ActionListener listener);

    /**
     * Configura o listener para o botão de reiniciar.
     * <p>
     * O listener será notificado quando o usuário clicar no botão,
     * permitindo que o simulador reinicie do zero.
     * </p>
     * 
     * @param listener ActionListener a ser chamado ao clicar no botão
     */
    void setAcaoReiniciar(ActionListener listener);

    // ========== MÉTODOS DE VERIFICAÇÃO ==========

    /**
     * Verifica se a simulação ainda é viável para continuar.
     * <p>
     * Uma simulação é considerada viável quando há dinâmica ecológica
     * suficiente para justificar continuação (ex: múltiplas espécies vivas).
     * </p>
     * <p>
     * <strong>Critérios típicos de inviabilidade:</strong>
     * <ul>
     * <li>Apenas uma ou nenhuma espécie sobreviveu</li>
     * <li>População total abaixo de limiar mínimo</li>
     * <li>Ecossistema colapsou (sem predadores ou presas)</li>
     * </ul>
     * </p>
     * 
     * @param campo Interface de acesso ao estado atual do campo
     * @return true se a simulação deve continuar, false caso contrário
     */
    boolean ehViavel(GradeVisualizavel campo);

    // ========== MÉTODOS DE CICLO DE VIDA ==========

    /**
     * Reinicia o estado visual da simulação.
     * <p>
     * Limpa estatísticas, reseta contadores e prepara a interface
     * para uma nova execução sem fechar a janela.
     * </p>
     */
    void reiniciar();

    /**
     * Fecha a visualização e libera todos os recursos do sistema.
     * <p>
     * Deve ser chamado ao encerrar a aplicação para:
     * <ul>
     * <li>Fechar janelas abertas</li>
     * <li>Liberar memória de buffers de imagem</li>
     * <li>Desconectar listeners</li>
     * </ul>
     * </p>
     */
    void fechar();
}