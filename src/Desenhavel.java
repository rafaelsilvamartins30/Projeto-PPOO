import java.awt.Color;
import java.awt.event.ActionListener;

/**
 * Interface para visualizações gráficas da simulação.
 * Permite definir cores para diferentes classes de animais,
 * mostrar o status atual do campo, verificar a viabilidade da simulação
 * e reiniciar a visualização.
 */
public interface Desenhavel {
    /**
     * Define a cor para uma determinada classe de animal.
     * @param classeAnimal 
     * @param cor
     */
    void definirCor(Class<?> classeAnimal, Color cor);

    /**
     * Mostra o status atual do campo na visualização.
     * @param passo
     * @param campo
     */
    void mostrarStatus(int passo, Campo campo);

    /**
     * Verifica se a simulação é viável com base no estado atual do campo.
     * @param campo
     * @return
     */
    boolean ehViavel(Campo campo);

    /**
     * Reinicia a visualização da simulação.
     */
    void reiniciar();

    /**
     * Fecha a visualização e libera recursos.
     */
    void fechar();
    
    /**
     * Atualiza o texto e o ícone/cor do indicador de clima.
     */
    void setInfoClima(String texto, boolean chuvoso);
    
    /**
     * Conecta a ação do botão Pausar/Continuar.
     */
    void setAcaoPausar(ActionListener listener);
    
    /**
     * Conecta a ação do botão Reiniciar.
     */
    void setAcaoReiniciar(ActionListener listener);
    
    /**
     * Atualiza o texto do botão de pausa (ex: mudar de "Pausar" para "Continuar").
     */
    void setTextoBotaoPausa(String texto);
}