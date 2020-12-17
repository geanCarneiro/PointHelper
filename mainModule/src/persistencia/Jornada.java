package persistencia;

import java.time.LocalTime;

@Tabela(nome = "JOR")
public class Jornada extends BaseEntity {

    @Coluna(tipo = "VARCHAR(10)")
    private String data;

    @Coluna(nome = "INI_EXP", tipo = "VARCHAR(5)")
    private String inicioExpediente;

    @Coluna(nome = "INI_ALM", tipo = "VARCHAR(5)")
    private String inicioAlmoco;

    @Coluna(nome = "FIM_ALM", tipo = "VARCHAR(5)")
    private String fimAlmoco;

    @Coluna(nome = "FIM_EXP", tipo = "VARCHAR(5)")
    private String fimExpediente;

    @Coluna(nome = "DUR", tipo = "VARCHAR(5)")
    private String duracao;

    public void calcularDuracao(){
        LocalTime iniExp = (inicioExpediente == null) ? LocalTime.MIN : LocalTime.parse(inicioExpediente);
        LocalTime iniAlm = (inicioAlmoco == null) ? LocalTime.MIN : LocalTime.parse(inicioAlmoco);
        LocalTime fimAlm = (fimAlmoco == null) ? LocalTime.MIN : LocalTime.parse(fimAlmoco);
        LocalTime fimExp = (fimExpediente == null) ? LocalTime.MIN : LocalTime.parse(fimExpediente);

        LocalTime durAlm = fimAlm.minusHours(iniAlm.getHour()).minusMinutes(iniAlm.getMinute());
        LocalTime durTotal = fimExp.minusHours(iniExp.getHour()).minusMinutes(iniExp.getMinute());

        LocalTime durReal = durTotal.minusHours(durAlm.getHour()).minusMinutes(durAlm.getMinute());
        this.duracao = String.format("%02d:%02d", durReal.getHour(), durReal.getMinute());
    }

    public boolean isJornadaValida(){
        if(this.getFimExpediente() == null) return false;

        if(this.getInicioAlmoco() != null
                && this.getFimAlmoco() == null)
            return false;

        return true;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getInicioExpediente() {
        return inicioExpediente;
    }

    public void setInicioExpediente(String inicioExpediente) {
        this.inicioExpediente = inicioExpediente;
    }

    public String getInicioAlmoco() {
        return inicioAlmoco;
    }

    public void setInicioAlmoco(String inicioAlmoco) {
        this.inicioAlmoco = inicioAlmoco;
    }

    public String getFimAlmoco() {
        return fimAlmoco;
    }

    public void setFimAlmoco(String fimAlmoco) {
        this.fimAlmoco = fimAlmoco;
    }

    public String getFimExpediente() {
        return fimExpediente;
    }

    public void setFimExpediente(String fimExpediente) {
        this.fimExpediente = fimExpediente;
    }

    public String getDuracao() {
        return duracao;
    }

    public void setDuracao(String duracao) {
        this.duracao = duracao;
    }

}
