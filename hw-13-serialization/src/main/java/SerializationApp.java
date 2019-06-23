import com.google.gson.Gson;
import engine.Mson;

public class SerializationApp {

    private SampleObjectFactory objectFactory = new SampleObjectFactory();

    public static void main(String[] args) {
        new SerializationApp().app();
    }

    private void app() {

        assertEquals(null);
        assertEquals(Byte.MAX_VALUE);
        assertEquals(Short.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE);
        assertEquals(Long.MAX_VALUE);
        assertEquals(Float.MAX_VALUE);
        assertEquals(Double.MAX_VALUE);
        assertEquals("abcdef");
        assertEquals('я');
        var bookStore = objectFactory.createBookStore();
        assertEquals(bookStore);
    }

    private void assertEquals(Object value) {
        String s1 = new Mson().toJson(value);
        String s2 = new Gson().toJson(value);
        if (!s1.equals(s2)) {
            throw new RuntimeException("Сериализованные представления для объекта отличается: " + value);
        }
        if (value == null) {
            System.out.println("Проверка для значения null - OK");
        } else {
            System.out.println(String.format("Проверка для типа %s - OK", value.getClass().getName()));
        }
    }
}
