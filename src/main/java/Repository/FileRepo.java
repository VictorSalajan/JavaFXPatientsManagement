package Repository;

import Domain.Entity;
import Domain.IEntityFactory;
import Utils.Utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Scanner;

public class FileRepo<T extends Entity> extends Repo<T> {
    private String filename;
    private IEntityFactory<T> entityFactory;

    public FileRepo(String filename, IEntityFactory<T> entityFactory) throws IOException, RepoException {
        this.filename = filename;
        this.entityFactory = entityFactory;

        readFromFile();
    }

    private void readFromFile() throws IOException, RepoException {
        File file = new File(filename);
        Scanner scanner = new Scanner(file);
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            T entity = entityFactory.createEntity(line);
            add(entity);
        }
        scanner.close();
    }

    public void add(T entity) throws IOException, RepoException {
        super.add(entity);

        try {
            writeFile(filename);
        } catch (IOException e) {
            throw new RepoException("Error writing file " + e.getMessage());
        }
    }

    private void writeFile(String filename) throws IOException {
        FileWriter writer = new FileWriter(filename);
        Iterator<T> iter = super.iterator();
        while (iter.hasNext()) {
            T obj = iter.next();
            Utils utils = new Utils();
            String strObj = utils.getEntityFileReadyString(obj);
            writer.write(strObj + "\n");
        }
        writer.close();
    }

    public void update(T entity) throws RepoException {
        super.update(entity);

        try {
            writeFile(filename);
        } catch (IOException e) {
            throw new RepoException("Error updating " + entity.getClass() + ". " + e.getMessage());
        }
    }

    public void deleteById(int ID) throws RepoException {
        super.deleteById(ID);

        try {
            writeFile(filename);
        } catch (IOException e) {
            throw new RepoException("Error deleting entity with ID = " + ID + ". " + e.getMessage());
        }
    }
}
