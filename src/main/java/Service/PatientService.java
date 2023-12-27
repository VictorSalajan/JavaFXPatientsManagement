package Service;

import Domain.Appointment;
import Domain.Patient;
import Repository.DuplicateIdException;
import Repository.IdNotFoundException;
import Repository.Repo;
import Repository.RepoException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class PatientService {
    public Repo<Patient> patientRepo;
    public Repo<Appointment> appointmentRepo;   // used for cascade delete

    /**
     * Constructs a PatientService
     * @param patientRepo: Repo<Patient>
     */
    public PatientService(Repo<Patient> patientRepo, Repo<Appointment> appointmentRepo) {
        this.patientRepo = patientRepo;
        this.appointmentRepo = appointmentRepo;
    }

    /**
     * Add a patient by its attributes
     * @param ID: int
     * @param nume: String
     * @param firstName: String
     * @param age: int
     * @throws DuplicateIdException: RepoException
     */
    public void add(int ID, String nume, String firstName, int age) throws RepoException, IOException {
        Patient p = new Patient(ID, nume, firstName, age);
        patientRepo.add(p);
    }

    /**
     * Return a patient by ID
     * @param ID: int
     * @return Patient
     */
    public Patient getById(int ID) {
        return patientRepo.getById(ID);
    }

    /**
     * Returns a list of all patients
     * @return entities: ArrayList<T>
     */
    public ArrayList<Patient> getAll() {
        return patientRepo.getAll();
    }

    public Iterator<Patient> getIterator() {
        return patientRepo.iterator();
    }

    /**
     * Update a patient by ID
     * @param newEntity: Entity
     * @throws IdNotFoundException: RepoException
     */
    public void update(Patient newEntity) throws RepoException {
        patientRepo.update(newEntity);
    }

    /**
     * Deletes a patient by ID and all appointments of that patient
     * @param ID: int
     * @throws IdNotFoundException: RepoException
     */
    public void deleteById(int ID) throws RepoException {
        Iterator<Appointment> appIter = appointmentRepo.iterator();
        while (appIter.hasNext()) {
            Appointment crtApp = appIter.next();
            Patient crtPacient = crtApp.getPacient();
            if (crtPacient.getID() == ID)
                appIter.remove();                                   // avoids ConcurrentModificationException
        }
        patientRepo.deleteById(ID);
    }
}