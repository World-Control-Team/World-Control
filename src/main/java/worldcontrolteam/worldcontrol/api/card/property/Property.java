package worldcontrolteam.worldcontrol.api.card.property;

public abstract class Property<T> {
    private final String name;
    private String unlocalizedName;

    public Property(String name) {
        this.name = name;
        this.unlocalizedName = "worldcontrol.card.prop_" + name;
    }

    public Property withUnlocalizedName(String name) {
        unlocalizedName = name;
        return this;
    }

    public abstract String getStringValue(T value);


    public String getName() {
        return name;
    }

    public String getUnlocalizedName() {
        return unlocalizedName;
    }
}
