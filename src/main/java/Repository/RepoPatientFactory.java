package Repository;

import Domain.IEntityFactory;
import Domain.Patient;
import Domain.PatientFactory;

import java.io.IOException;

public class RepoPatientFactory {
    public static Repo<Patient> createRepo(String repoType, String filePath) throws IOException, RepoException, ClassNotFoundException {
    IEntityFactory<Patient> patientFactory = new PatientFactory();
    if ("file".equals(repoType)) {
        return new FileRepo<Patient>(filePath, patientFactory);
    } else if ("memory".equals(repoType)) {
        return new Repo<Patient>();
    } else if ("binary".equals(repoType)) {
        return new BinaryFileRepo<Patient>(filePath);
    }
    else if ("sql".equals(repoType)) {
        return new PatientDbRepo();
    }
    else {
        throw new IllegalArgumentException("Unknown repo: " + repoType);
    }
}
}
