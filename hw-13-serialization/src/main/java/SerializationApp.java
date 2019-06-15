import com.google.gson.Gson;
import engine.Mson;

public class SerializationApp {

    private SampleObjectFactory objectFactory = new SampleObjectFactory();

    public static void main(String[] args) {
        new SerializationApp().app();
    }

    public void app() {

        var object = objectFactory.createBookStore();

        Gson gson = new Gson();
        String json1 = gson.toJson(object);

        Mson mson = new Mson();
        String json2 = mson.toJson(object);

        System.out.println(json1);
        System.out.println(json2);
        System.out.println(json1.equals(json2));
    }
}
