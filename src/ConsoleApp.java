import main.java.bot.*;
public class ConsoleApp {
    public static void main(String[] args){
<<<<<<< HEAD
        Bot bot = new ConsoleBot();
        bot.run();
=======
        Scanner scanner = new Scanner(System.in);
        try {
            CoreUtils.initDatabase();
        } catch (SQLException e) {
            System.err.println("[ERROR] Не удалось создать базу данных.");
            return;
        }
        final boolean[] InTask = {false};
        MessageService messageService = new MessageService();
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (!InTask[0]) {
                    System.out.println("\n");
                    messageService.printMessage();
                }
            }
        }, 60000, 60000);

        while (true) {
            System.out.println("[1] Заполнение пропущенных слов в предложении");
            System.out.println("[2] Проверка знания слов по темам");
            System.out.println("[3] Уроки по темам (правило и отработка)");
            System.out.println("[0] узнать статистику за всё время");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    InTask[0] = true;
                    new FillBlankTask().start(scanner);
                    InTask[0] = false;
                    break;

                case "2":
                    InTask[0] = true;
                    new VocabularyTask().start(scanner);
                    InTask[0] = false;
                    break;

                case "3":
                    InTask[0] = true;
                    new RuleTask().start(scanner);
                    InTask[0] = false;
                    break;
                case "0":
                    InTask[0] = true;
                    CoreUtils.printAllTimeStats("test_user");
                    InTask[0] = false;
                    break;

                default:
                    System.out.println("Ошибка: введен неверный номер.");
                    continue;
            }
            System.out.print("> ");
            String line = scanner.nextLine();
            if ("exit".equalsIgnoreCase(line)) {
                break;
            }
        }
        scanner.close();
>>>>>>> ee9924b44d165bc88eaaa5849275880354edf0f8
    }
}
