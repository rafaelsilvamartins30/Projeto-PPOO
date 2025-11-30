import java.util.List;
import java.util.Random;
/**
 * Representa um animal genérico na simulação de predador e presa.
 * Cada espécie de animal (predador ou presa) estende esta classe abstrata.
 * Cada animal tem uma idade, um nível de alimento (fome) e pode se mover,
 * reproduzir e morrer.
 * 
 * @author David J. Barnes e Michael Kolling
 * @version 2025-11-30
 */
public abstract class Animal implements Ator{
    private static final Random aleatorio = new Random();
    private boolean vivo;
    private Localizacao localizacao;
    private int idade;
    private int nivelAlimento;

    /**
     * Cria um novo animal. 
     * A idade pode ser aleatória ou zero (novo nascimento).
     * Em ambos os casos, o nível de alimento é inicializado.
     * @param idadeAleatoria Se verdadeiro, o animal terá uma idade aleatória.
     */
    public Animal(boolean idadeAleatoria) 
    {
        vivo = true;
        idade = 0;
        if(idadeAleatoria) {
            idade = aleatorio.nextInt(idadeMaxima());
        }
        localizacao = null;

        setNivelAlimento(Configuracao.VALOR_ALIMENTAR);
        if(idadeAleatoria) setNivelAlimento(getAleatorio().nextInt(Configuracao.VALOR_ALIMENTAR));
    }

    /**
     * Retorna o gerador de números aleatórios compartilhado.
     * @return o gerador de números aleatórios
     */
    protected Random getAleatorio() { return aleatorio; }

    /**
     * Se o animal está vivo
     * @return verdadeiro se estiver vivo, falso está morto
     */
    @Override public boolean estaVivo() { return vivo; }

    /**
     * Indica que o animal morreu.
     * Define seu estado como morto e zera seu nível de alimento.
     */
    public void morrer() {
        vivo = false;
        nivelAlimento = 0;
    }

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
    protected void definirLocalizacao(int linha, int coluna) {this.localizacao = new Localizacao(linha, coluna); }

    /**
     * Define a localização do animal.
     * @param localizacao A localização do animal.
     */
    protected void definirLocalizacao(Localizacao localizacao) { this.localizacao = localizacao; }

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
    public boolean podeReproduzir() { return idade >= getIdadeReprodutiva(); }

    /**
     * Calcula o número de nascimentos para este animal.
     * 
     * @return O número de nascimentos (pode ser zero).
     */
    protected int reproduzir() {
        int nascimentos = 0;
        if(podeReproduzir() && aleatorio.nextDouble() <= probabilidadeReproducao()) {
            nascimentos = aleatorio.nextInt(tamanhoMaximoNinhada()) + 1;
        }
        return nascimentos;
    }

    /**
     * Lógica comum de reprodução para QUALQUER animal.
     * @param campoAtualizado O campo onde os filhos serão colocados.
     * @param novosAnimais A lista para registrar os recém-nascidos.
     */
    protected void processarReproducao(CampoInterativo campoAtualizado, List<Ator> novosAnimais) {
        int nascimentos = reproduzir();
        
        for (int i = 0; i < nascimentos; i++) {
            Localizacao loc = campoAtualizado.localizacaoAdjacenteLivre(getLocalizacao());
            
            if (loc != null) {
                Animal filhote = criarFilho();
                novosAnimais.add(filhote);
                filhote.definirLocalizacao(loc);
                campoAtualizado.colocar(filhote, loc);
            }
        }
    }

    /**
     * Tenta mover o animal para uma localização adjacente livre.
     * Se não conseguir (estiver bloqueado), o animal morre (superpopulação).
     */
    protected void tentarMoverLivremente(CampoInterativo campoAtualizado) {
        Localizacao novaLocalizacao = campoAtualizado.localizacaoAdjacenteLivre(getLocalizacao());
        
        if (novaLocalizacao != null) {
            moverPara(novaLocalizacao, campoAtualizado);
        } else {
            morrer();
        }
    }

    /**
     * Auxiliar para efetivar o movimento para um local específico.
     */
    protected void moverPara(Localizacao localizacao, CampoInterativo campoAtualizado) {
        definirLocalizacao(localizacao);
        campoAtualizado.colocar(this, localizacao);
    }

    /**
     * Método abstrato que obriga cada espécie a definir como criar seu filhote.
     */
    protected abstract Animal criarFilho();

    /**
     * A probabilidade de reprodução do animal.
     */
    protected abstract double probabilidadeReproducao();

    /**
     * O tamanho máximo da ninhada do animal.
     */
    protected abstract int tamanhoMaximoNinhada();

    /**
     * Incrementa a idade.
     * Isso pode resultar na morte do animal.
     */
    protected void incrementarIdade() {
        idade++;
        if(idade > idadeMaxima()) {
            morrer();
        }
    }

    /**
     * A idade máxima do animal.
     */
    protected abstract int idadeMaxima();

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
        if(nivelAlimento <= 0) morrer();
    }
}