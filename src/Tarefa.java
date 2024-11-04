public class Tarefa {
    private int id;
    private String nome;
    private String descricao;
    private String prioridade;
    private String prazo;
    private String status; // Mudando para String para aceitar status como "A Fazer", "Em Andamento", "Concluída"

    public Tarefa(int id, String nome, String descricao, String prioridade, String prazo, String status) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.prioridade = prioridade;
        this.prazo = prazo;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getPrioridade() {
        return prioridade;
    }

    public String getPrazo() {
        return prazo;
    }

    public String getStatus() {
        return status;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setPrioridade(String prioridade) {
        this.prioridade = prioridade;
    }

    public void setPrazo(String prazo) {
        this.prazo = prazo;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
