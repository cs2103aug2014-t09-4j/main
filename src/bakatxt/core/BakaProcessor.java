package bakatxt.core;

import java.util.LinkedList;

public class BakaProcessor implements BakaProcessorInterface {

    private static Database _database;
    private static BakaParser _parser;

    public BakaProcessor() {
        _database = Database.getInstance();
        _parser = BakaParser.getInstance();
    }

    @Override
    public String addTask(String input) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void deleteTask(String input) {
        // TODO Auto-generated method stub

    }

    @Override
    public LinkedList<Task> displayTask() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void editTask(String input) {
        // TODO Auto-generated method stub

    }

}
