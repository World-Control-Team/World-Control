package worldcontrolteam.worldcontrol.api.card.property;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Properties {
    public Map<String, Optional<Object>> values;

    public Properties(IPropertyProvider propertyProvider) {
        values = new HashMap<>();
        for (Property i : propertyProvider.getProperties()) {
            values.put(i.getName(), Optional.empty());
        }
    }

    public <T> Properties withValue(Property<T> property, T value) {
        values.put(property.getName(), Optional.of(value));
        return this;
    }

    public <T> T getValue(Property<T> property) {
        Optional<Object> optional = values.get(property.getName());
        return (T) optional.orElse(null);
    }

    public <T> T getValue(String propertyName) {
        Optional<Object> optional = values.get(propertyName);
        return (T) optional.orElse(null);
    }

}
