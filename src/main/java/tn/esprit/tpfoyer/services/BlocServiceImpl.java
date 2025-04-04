package tn.esprit.tpfoyer.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tn.esprit.tpfoyer.entities.Bloc;
import tn.esprit.tpfoyer.entities.Chambre;
import tn.esprit.tpfoyer.entities.Foyer;
import tn.esprit.tpfoyer.entities.Universite;
import tn.esprit.tpfoyer.repositories.BlocRepository;
import tn.esprit.tpfoyer.repositories.ChambreRepository;

import java.util.List;
import java.util.Set;

@Service
@Slf4j
@AllArgsConstructor
public class BlocServiceImpl implements  IBlocService {
    private final ChambreRepository chambreRepository;
    BlocRepository blocRepository;


    @Override
    public List<Bloc> retrieAllBlocs() {
        return blocRepository.findAll();
    }

    @Override
    public Bloc addBloc(Bloc b) {
        return blocRepository.save(b);
    }

    @Override
    public Bloc updateBloc(Bloc b) {
        return blocRepository.save(b);
    }

    @Override
    public Bloc retrieBloc(Long idBloc) {
        return blocRepository.findById(idBloc).get();
    }

    @Override
    public void removeBloc(Long idBloc) {
        blocRepository.deleteById(idBloc);

    }

    @Override
    public List<Bloc> findByFoyerUniversiteNomUniversite(String nomUniversite) {
        return blocRepository.findByFoyerUniversiteNomUniversite(nomUniversite);
    }
    @Override
    public Bloc affecterChambresABloc(List<Long> numChambres, String nomBloc) {
        // Fetch the Bloc by its name
        Bloc bloc = blocRepository.findByNomBloc(nomBloc);
        if (bloc == null) {
            throw new IllegalArgumentException("Bloc with name " + nomBloc + " not found");
        }

        // Fetch Chambres by their IDs
        List<Chambre> chambres = chambreRepository.findByNumChambreIn(numChambres);
        if (chambres.isEmpty()) {
            throw new IllegalArgumentException("No Chambres found for the provided numbers");
        }

        // Associate Chambres with the Bloc
        for (Chambre chambre : chambres) {
            chambre.setBloc(bloc); // Assign Bloc to each Chambre
        }
        bloc.getChambres().addAll(chambres); // Add Chambres to Bloc's collection

        // Save Bloc with updated associations
        return blocRepository.save(bloc);
    }



}
