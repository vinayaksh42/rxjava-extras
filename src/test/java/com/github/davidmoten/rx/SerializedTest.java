package com.github.davidmoten.rx;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;
import org.junit.Test;

import java.io.ByteArrayOutputStream;

import static org.junit.Assert.fail;

public class SerializedTest {

    /**
     * An example unregistered class to serialize.
     */
    public static class UnregisteredClass {
        private String data;

        public UnregisteredClass(String data) {
            this.data = data;
        }

        // Getter and setter methods
        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }
    }

    @Test
    public void testSerializeUnregisteredClass() {
        Kryo kryo = new Kryo();
        // Note: In Kryo v3.0.3, registrationRequired is false by default.
        // In Kryo v5.0.0-RC4, registrationRequired is true by default.

        UnregisteredClass obj = new UnregisteredClass("test data");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Output output = new Output(baos);

        try {
            kryo.writeObject(output, obj);
            output.close();
            // In v3.0.3, this succeeds because registration is not required.
            // In v5.0.0-RC4, this throws an exception because registration is required.
        } catch (Exception e) {
            // Exception is expected in v5.0.0-RC4 due to the change in default behavior.
            fail("Exception was thrown during serialization: " + e.getMessage());
        }
    }
}
