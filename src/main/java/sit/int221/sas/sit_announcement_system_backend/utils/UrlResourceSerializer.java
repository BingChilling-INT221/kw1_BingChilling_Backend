package sit.int221.sas.sit_announcement_system_backend.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.springframework.core.io.UrlResource;
import java.io.IOException;

public class UrlResourceSerializer extends StdSerializer<UrlResource>{

    public UrlResourceSerializer() {
        this(null);
    }

    protected UrlResourceSerializer(Class<UrlResource> t) {
        super(t);
    }

    @Override
    public void serialize(UrlResource value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeString(value.getURL().toString()); // Serialize the URL as a string
    }
}
