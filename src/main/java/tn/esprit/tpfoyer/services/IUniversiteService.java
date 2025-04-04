package tn.esprit.tpfoyer.services;

import tn.esprit.tpfoyer.entities.Tache;
import tn.esprit.tpfoyer.entities.Universite;

import java.util.List;

public interface IUniversiteService {
    List<Universite> retrieAllUniversites();
    Universite addUniversite(Universite u);
    Universite updateUniversite(Universite u);
    Universite retrieUniversite(Long idUniversite);
    void removeUniversite(Long idUniversite);
    public Universite affecterFoyerAUniversite(long idFoyer, String nomUniversite);
}
