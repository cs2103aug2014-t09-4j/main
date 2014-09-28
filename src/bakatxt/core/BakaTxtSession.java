package bakatxt.core;

public class BakaTxtSession implements BakaTxtSessionInterface {

    // TODO Ensure filename input is correct
    // TODO Ensure database is closed

    Database database;

    public BakaTxtSession(String fileName) {
        database = new Database(fileName);
    }

    @Override
    public String add(String input) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String delete(String input) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String display(String input) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String display() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void sort() {
        // TODO Auto-generated method stub

    }

    @Override
    public void exit() {
        // TODO Auto-generated method stub

    }

    @Override
    public String getFileName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getPrevious() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getTotal() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getUndone() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getDone() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String done(String input) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getTask(String title) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String deleteDone() {
        // TODO Auto-generated method stub
        return null;
    }

}
