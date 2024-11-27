package tn.esprit.tpfoyer.services;


import tn.esprit.tpfoyer.entities.Etudiant;

import java.util.List;

public interface IEtudiantService {
    List<Etudiant> retrieAllEtudiants();
    Etudiant addCEtudiant(Etudiant e);
    Etudiant updateEtudiant(Etudiant e);
    Etudiant retrieEtudiant(Long idEtudiant);
    void removeEtudiant(Long idEtudiant);
}
