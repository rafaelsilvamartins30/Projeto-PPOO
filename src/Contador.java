import java.awt.Color;

/**
 * Fornece um contador para um participante da simulação.
 * Inclui uma string de identificação e uma contagem de quantos
 * participantes desse tipo existem atualmente na simulação.
 * 
 * @author David J. Barnes e Michael Kolling
 * @version 2002-04-23 (traduzido)
 */
public class Contador
{
    private String nome;
    private int contagem;

    /**
     * Cria um contador para um dos tipos da simulação.
     * @param nome Um nome, por exemplo: "Raposa".
     */
    public Contador(String nome)
    {
        this.nome = nome;
        contagem = 0;
    }
    
    /**
     * @return A descrição curta deste tipo.
     */
    public String getNome()
    {
        return nome;
    }

    /**
     * @return A contagem atual deste tipo.
     */
    public int getContagem()
    {
        return contagem;
    }

    /**
     * Incrementa a contagem atual em um.
     */
    public void incrementar()
    {
        contagem++;
    }
    
    /**
     * Reinicia a contagem atual para zero.
     */
    public void reiniciar()
    {
        contagem = 0;
    }
}