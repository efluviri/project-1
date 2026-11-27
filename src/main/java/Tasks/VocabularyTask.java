package main.java.Tasks;
import main.java.inout.InOut;
import java.util.HashMap;
import java.util.Map;

public class VocabularyTask implements Task {
    private static final Map<String, String> FOOD_MAP = createFoodVocab();
    private static final Map<String, String> TRAVEL_MAP = createTravelVocab();

    @Override
    public void start(InOut io,long chatId) {
        io.print("\n ЗАДАНИЕ: ПЕРЕВЕДИ СЛОВО ");
        String[] topics = {"Еда", "Путешествия"};
        String[] callbacks = {"vocab_food", "vocab_travel"};
        io.printWithInlineButtons("Выберите тему:", topics, callbacks);
    }
    public void startTopic(InOut io, long chatId, Map<String, String> vocab, String mapName) {
        CoreUtils.processTasks(io, chatId, vocab, "vocabulary", mapName);
        io.sendBuffer();
    }
    public static Map<String, String> getFoodVocab() {
        return new HashMap<>(FOOD_MAP);
    }
    public static Map<String, String> getTravelVocab() {
         return new HashMap<>(TRAVEL_MAP);
    }
    private static Map<String, String> createFoodVocab() {
        Map<String, String> vocab = new HashMap<>();
        vocab.put("яблоко", "apple");
        vocab.put("хлеб", "bread");
        vocab.put("молоко", "milk");
        vocab.put("яйцо", "egg");
        return vocab;
    }
    private static Map<String, String> createTravelVocab() {
        Map<String, String> vocab = new HashMap<>();
        vocab.put("аэропорт", "airport");
        vocab.put("билет", "ticket");
        vocab.put("отель", "hotel");
        vocab.put("поезд", "train");
        return vocab;
    }
}


