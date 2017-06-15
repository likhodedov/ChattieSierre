package application.xxmp.chattiesierre;

/**
 * Created by d.lihodedov on 16.05.2017.
 */

public class StandartMessage {
    private String Body;
    private String From;

    public StandartMessage(String Body, String From){
        this.Body=Body;
        this.From=From;
    }
    public String getBody(){
        return Body;
    }
    public String getFrom(){
        return From;
    }
}
