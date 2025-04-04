package tn.esprit.tpfoyer.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tn.esprit.tpfoyer.entities.Etudiant;
import tn.esprit.tpfoyer.entities.Tache;
import tn.esprit.tpfoyer.repositories.EtudiantRepository;
import tn.esprit.tpfoyer.repositories.TacheRepository;

import java.util.List;
@Service
@Slf4j
@AllArgsConstructor


public class TacheServiceImpl implements  ITacheService{

    TacheRepository tacheRepository;
    EtudiantRepository etudiantRepository;
    @Override
    public List<Tache> retrieAllTaches() {
        return tacheRepository.findAll();
    }

    @Override
    public Tache addTache(Tache f) {
        return tacheRepository.save(f);
    }

    @Override
    public Tache updateTache(Tache f) {
        return tacheRepository.save(f);
    }

    @Override
    public Tache retrieTache(Long idTache) {
        return tacheRepository.findById(idTache).get();
    }


    @Override
    public void removeTache(Long idTache) {
        tacheRepository.deleteById(idTache);

    }
@Override
    public List<Tache> addTasksAndAffectToEtudiant(List<Tache> tasks, String nomEt, String prenomEt) {
        List<Etudiant> etudiants = etudiantRepository.findByNomAndPrenom(nomEt, prenomEt);
        Etudiant etudiant = etudiants.get(0); // Prendre le premier étudiant trouvé (si plusieurs sont retournés)

        for (Tache tache : tasks) {
            tache.setEtudiant(etudiant);
            tacheRepository.save(tache);
        }

        return tasks;
    }
}

