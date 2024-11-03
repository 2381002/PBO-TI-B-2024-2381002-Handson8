import repositories.TodoListRepository;
import repositories.TodoListRepositoryImpl;
import services.TodoListService;
import services.TodoListServiceImpl;
import views.TodoListTerminalViewImpl;
import views.TodoListView;

public class Main {
    public static void main(String[] args) {
        TodoListRepository todoListRepository = new TodoListRepositoryImpl();
        TodoListService todoListService = new TodoListServiceImpl(todoListRepository);
        TodoListView todoListView = new TodoListTerminalViewImpl(todoListService);
        todoListView.run();
    }
}