public class Sport {
    private int id;
    private String name;
    private int required_participants;

    Sport(int id, String name, int required_participants)
    {
        this.id = id;
        this.name = name;
        this.required_participants = required_participants;
    }
    public int getId() {return this.id;};
    public String getName() {return this.name;};
    public int getParticipants(){return required_participants;};
}
