package main.java.Tasks;
import main.java.inout.InOut;
import java.util.HashMap;
import java.util.Map;

public class VocabularyTask implements Task {
    private static final Map<String, String> FOOD_MAP = createFoodVocab();
    private static final Map<String, String> TRAVEL_MAP = createTravelVocab();

    @Override
    public void start(InOut io,long chatId){
        io.print("\n=== ЗАДАНИЕ: ПЕРЕВЕДИ СЛОВО ===");
        printTopicMenu(io);
        String choice = io.readline();
        Map<String, String> vocabulary;

        String mapName;
        switch (choice.toLowerCase()) {
            case "1","food":
                vocabulary=FOOD_MAP;
                mapName="food";
                break;
        case "2","travel":
            vocabulary=TRAVEL_MAP;
            mapName="travel";
            break;
        default:
            io.print("Неверный ввод. Выходим из режима словаря.");
            return;
        }
        if (vocabulary.isEmpty()) return;
        CoreUtils.processTasks(io,chatId,vocabulary,"vocabulary",mapName);
        io.sendBuffer();
    }
    private void printTopicMenu(InOut io) {
        io.print("Выберите тему:");
        io.print("[1] Food (Еда)");
        io.print("[2] Travel (Путешествия)");
        io.print("Ваш выбор (номер или название): ");
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


