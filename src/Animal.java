import java.util.Random;
/**
 * Representa um animal genérico na simulação de predador e presa.
 * 
 * Esta é uma classe abstrata que define os comportamentos e 
 * características comuns a todos os animais do modelo, como idade,
 * localização, estado de vida e reprodução. 
 * As subclasses: por exemplo, {@link Coelho} e {@link Urso},
 * implementam comportamentos específicos, como caça, movimento 
 * e morte.
 * 
 * @author David J. Barnes e Michael Kolling
 * @version 2002-04-23
 */

public abstract class Animal implements Ator{
    // Um gerador de números aleatórios compartilhado para controlar a reprodução.
    private static final Random aleatorio = new Random();

    // Indica se o animal está vivo ou não
    private boolean vivo;
    // A posição do animal
    private Localizacao localizacao;
    // A idade do animal
    private int idade;
    // Nível de alimento do animal
    private int nivelAlimento;

    /**
     * Cria um novo animal
     * @param idadeAleatoria Se verdadeiro, o animal terá uma idade aleatória.
     */
    public Animal(boolean idadeAleatoria) 
    {
        vivo = true;
        idade = 0;
        if(idadeAleatoria) {
            idade = aleatorio.nextInt(IDADE_MAXIMA());
        }
        localizacao = null;
    }

    protected Random getAleatorio() 
    {
        return aleatorio;
    }

    /**
     * Se o animal está vivo
     * @return verdadeiro se estiver vivo, falso está morto
     */
    @Override public boolean estaVivo() { return vivo; }

    /**
     * Se o animal morrer esse método deve ser chamado
     */
    protected void definirEstaVivo(boolean estaVivo) { vivo = estaVivo; }

    /**
     * Retorna a localização do animal
     * @return a localização do animal
     */
    public Localizacao getLocalizacao() { return localizacao; }

    /**
     * Define a localização do animal.
     * @param linha A coordenada vertical da localização.
     * @param coluna A coordenada horizontal da localização.
     */
    protected void definirLocalizacao(int linha, int coluna) 
    {
        this.localizacao = new Localizacao(linha, coluna);
    }

    /**
     * Define a localização do animal.
     * @param localizacao A localização do animal.
     */
    protected void definirLocalizacao(Localizacao localizacao)
    {
        this.localizacao = localizacao;
    }

    /**
     * Retorna a idade reprodutiva do animal.
     * Cada subclasse deve implementar este método.
     * 
     * @return A idade reprodutiva do animal.
     */
    protected abstract int getIdadeReprodutiva();

    /**
     * Retorna a idade atual do animal.
     * 
     * @return A idade atual do animal.
     */
    protected int getIdade() { return idade;}

    /**
     * Um animal pode procirar se atingiu a idade de procriação.
     * 
     * @return verdadeiro se o animal puder se reproduzir, falso caso contrário.
     */
    public boolean podeReproduzir() {
        return estaVivo() && getIdade() >= getIdadeReprodutiva();
    }

    /**
     * Calcula o número de nascimentos para este animal.
     * 
     * @return O número de nascimentos (pode ser zero).
     */
    protected int reproduzir() {
        int nascimentos = 0;
        if(podeReproduzir() && aleatorio.nextDouble() <= PROBABILIDADE_REPRODUCAO()) {
            nascimentos = aleatorio.nextInt(TAMANHO_MAXIMO_NINHADA()) + 1;
        }
        return nascimentos;
    }

    /**
     * A probabilidade de reprodução do animal.
     */
    protected abstract double PROBABILIDADE_REPRODUCAO();

    /**
     * O tamanho máximo da ninhada do animal.
     */
    protected abstract int TAMANHO_MAXIMO_NINHADA();

    /**
     * Incrementa a idade.
     * Isso pode resultar na morte do animal.
     */
    protected void incrementarIdade() {
        idade++;
        if(idade > IDADE_MAXIMA()) {
            definirEstaVivo(false);
        }
    }

    /**
     * A idade máxima do animal.
     */
    protected abstract int IDADE_MAXIMA();

    /**
     * Retorna o nível de alimento do animal.
     * @return o nível de alimento do animal
     */
    protected int getNivelAlimento() {
        return nivelAlimento;
    }

    /**
     * Define o nível de alimento do animal.
     * @param nivelAlimento o novo nível de alimento do animal
     */
    protected void setNivelAlimento(int nivelAlimento) {
        this.nivelAlimento = nivelAlimento;
    }

    /**
     * Incrementa o nível de fome do animal. 
     * Se o animal ficar sem comida, ele morre.
     */
    protected void incrementarFome() {
        nivelAlimento--;
        if(nivelAlimento <= 0) definirEstaVivo(false);
    }
}
