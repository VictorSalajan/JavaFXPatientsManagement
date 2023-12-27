package Repository;

import Domain.Entity;

import java.io.*;
import java.util.Iterator;

public class BinaryFileRepo<T extends Entity> extends Repo<T> {
    private String filename;

    public BinaryFileRepo(String filename) throws IOException, RepoException, ClassNotFoundException {
        this.filename = filename;
        loadFile();
    }

    private void loadFile() throws IOException, RepoException, ClassNotFoundException {
        try {
            FileInputStream fileIn = new FileInputStream(filename);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            while (true) {
                try {
                    T obj = (T) in.readObject();
                    super.add(obj);
                } catch (EOFException e) {
                    break;                  // reached end of file
                }
            }
            in.close();
            fileIn.close();
        } catch (NullPointerException e) {
            System.out.println("File does not have data yet, cannot load!");
        } catch (IOException e) {
            System.out.println("File is empty");
        } catch (ClassNotFoundException e) {
            throw new ClassNotFoundException("Class not found " + e.getMessage());
        }

    }

    @Override
    public void add(T o) throws RepoException, IOException {
        super.add(o);
        try {
            saveFile();
        } catch (IOException e) {
            throw new RepoException("Error saving file " + e.getMessage());
        }
    }

    private void saveFile() throws IOException {
        FileOutputStream fileOut = new FileOutputStream(filename);
        ObjectOutputStream out = new ObjectOutputStream(fileOut);
        Iterator<T> iter = super.iterator();
        while (iter.hasNext()) {
            T obj = iter.next();
            out.writeObject(obj);
        }
        out.close();
        fileOut.close();
    }

    public void update(T entity) throws RepoException {
        super.update(entity);

        try {
            saveFile();
        } catch (IOException e) {
            throw new RepoException("Error updating " + entity.getClass() + ". " + e.getMessage());
        }
    }

    public void deleteById(int ID) throws RepoException {
        super.deleteById(ID);

        try {
            saveFile();
        } catch (IOException e) {
            throw new RepoException("Error deleting entity with ID = " + ID + ". " + e.getMessage());
        }
    }
}
