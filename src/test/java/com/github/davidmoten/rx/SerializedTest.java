package com.github.davidmoten.rx;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.util.List;

import org.junit.Test;

import rx.Observable;

public class SerializedTest {

    @Test
    public void testSerializeUnregisteredClass() {
        File file = new File("target/temp_unregistered");
        file.delete();
        // Create an instance of an unregistered class
        UnregisteredClass obj = new UnregisteredClass("test");
        try {
            Serialized.kryo().write(Observable.just(obj), file).subscribe();
        } catch (Exception e) {
            // If an exception occurs, fail the test
            fail("Serialization failed: " + e.getMessage());
        }
        assertTrue(file.exists());
        assertTrue(file.length() > 0);
        // Try to deserialize
        List<UnregisteredClass> list = Serialized.kryo()
                .read(UnregisteredClass.class, file)
                .toList()
                .toBlocking()
                .single();
        assertEquals(1, list.size());
        assertEquals("test", list.get(0).name);
    }

    static class UnregisteredClass {
        final String name;

        UnregisteredClass() {
            this("");
        }

        UnregisteredClass(String name) {
            this.name = name;
        }
    }
}
