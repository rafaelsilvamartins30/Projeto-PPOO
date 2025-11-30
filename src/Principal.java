/**
 * Classe principal que serve como ponto de entrada da aplicação.
 * <p>
 * Esta classe contém o método {@code main} que inicializa e executa
 * a simulação do ecossistema com parâmetros padrão.
 * </p>
 * 
 * <p>
 * <strong>Funcionamento:</strong>
 * </p>
 * <ol>
 * <li>Cria uma instância do {@link Simulador} com configurações padrão</li>
 * <li>Inicia a simulação por um número predefinido de passos</li>
 * <li>A interface gráfica é aberta automaticamente</li>
 * <li>O usuário pode interagir via botões (pausar, continuar, reiniciar)</li>
 * </ol>
 * 
 * <p>
 * <strong>Customização:</strong>
 * </p>
 * <p>
 * Para ajustar o comportamento da simulação, modifique:
 * <ul>
 * <li>Número de passos no método {@code simular()}</li>
 * <li>Dimensões do campo no construtor do {@link Simulador}</li>
 * <li>Parâmetros de população em {@link Configuracao}</li>
 * </ul>
 * </p>
 * 
 * @author Grupo 10
 * @version 1.0
 * @see Simulador
 * @see Configuracao
 */
public class Principal {

  /**
   * Método de entrada da aplicação.
   * <p>
   * Inicializa o simulador com configurações padrão e executa
   * a simulação por 500 passos. A simulação continuará além deste
   * limite se o usuário não pausá-la, desde que o ecossistema
   * permaneça viável.
   * </p>
   * 
   * @param args Argumentos de linha de comando (não utilizados atualmente)
   */
  public static void main(String[] args) {
    // Cria o simulador com dimensões padrão (definidas em Configuracao)
    Simulador simulador = new Simulador();

    // Executa a simulação por 500 passos (pode ser pausada/continuada pelo usuário)
    simulador.simular(500);
  }
}
