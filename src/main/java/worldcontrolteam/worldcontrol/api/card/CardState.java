package worldcontrolteam.worldcontrol.api.card;

public enum CardState {
    /**
     * All required data found, ready to display it
     */
    OK,

    /**
     * Target block doesn't exist or has invalid type
     */
    NO_TARGET,

    /**
     * Target is out of range
     */
    OUT_OF_RANGE,

    /**
     * Card doesn't have required fields. Basically, NOT OK.
     */
    INVALID_CARD;

}
