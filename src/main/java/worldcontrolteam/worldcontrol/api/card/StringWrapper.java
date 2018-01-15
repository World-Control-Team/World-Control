package worldcontrolteam.worldcontrol.api.card;

public class StringWrapper {

    /**
     * Text of the left aligned part of the line.
     */
    public String textLeft;
    /**
     * Text of the centered part of the line.
     */
    public String textCenter;
    /**
     * text of the right aligned part of the line.
     */
    public String textRight;
    /**
     * Color of the left aligned part of the line.
     */
    public int colorLeft = 0;
    /**
     * Color of the centered part of the line.
     */
    public int colorCenter = 0;
    /**
     * Color of the right aligned part of the line.
     */
    public int colorRight = 0;

    public StringWrapper(String text) {
        textLeft = text;
    }

    public StringWrapper() {
    }
}
