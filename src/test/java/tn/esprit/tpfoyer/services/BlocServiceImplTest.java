package tn.esprit.tpfoyer.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.tpfoyer.entities.Bloc;
import tn.esprit.tpfoyer.entities.Chambre;
import tn.esprit.tpfoyer.repositories.BlocRepository;
import tn.esprit.tpfoyer.repositories.ChambreRepository;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BlocServiceImplTest {

    @InjectMocks
     BlocServiceImpl blocService;

    @Mock
     BlocRepository blocRepository;

    @Mock
     ChambreRepository chambreRepository;

     Bloc sampleBloc;

    @BeforeEach
     void setUp() {
        sampleBloc = new Bloc();
        sampleBloc.setIdBloc(1L);
        sampleBloc.setNomBloc("Bloc A");
        sampleBloc.setChambres(new HashSet<>());
    }

    @Test
    void addBloc() {

        when(blocRepository.save(any(Bloc.class))).thenReturn(sampleBloc);
        Bloc added = blocService.addBloc(sampleBloc);
        assertEquals("Bloc A", added.getNomBloc());
        verify(blocRepository).save(sampleBloc);
    }

    @Test
    void updateBloc() {
        when(blocRepository.save(any(Bloc.class))).thenReturn(sampleBloc);
        Bloc updated = blocService.updateBloc(sampleBloc);
        assertEquals(sampleBloc.getIdBloc(), updated.getIdBloc());
        verify(blocRepository).save(sampleBloc);
    }

    @Test
    void retrieBloc() {
        when(blocRepository.findAll()).thenReturn(Arrays.asList(sampleBloc));
        List<Bloc> blocs = blocService.retrieAllBlocs();
        assertEquals(1, blocs.size());
        verify(blocRepository).findAll();

    }

    @Test
    void removeBloc() {
        blocService.removeBloc(1L);
        verify(blocRepository).deleteById(1L);
    }

    @Test
    void findByFoyerUniversiteNomUniversite() {
        when(blocRepository.findByFoyerUniversiteNomUniversite("Université Test")).thenReturn(Arrays.asList(sampleBloc));
        List<Bloc> blocs = blocService.findByFoyerUniversiteNomUniversite("Université Test");
        assertFalse(blocs.isEmpty());
        verify(blocRepository).findByFoyerUniversiteNomUniversite("Université Test");
    }

    @Test
    void affecterChambresABloc() {
        List<Long> chambreIds = Arrays.asList(101L, 102L);

        Chambre chambre1 = new Chambre();
        chambre1.setNumChambre(101L);

        Chambre chambre2 = new Chambre();
        chambre2.setNumChambre(102L);

        when(blocRepository.findByNomBloc("Bloc A")).thenReturn(sampleBloc);
        when(chambreRepository.findByNumChambreIn(chambreIds)).thenReturn(Arrays.asList(chambre1, chambre2));
        when(blocRepository.save(any(Bloc.class))).thenReturn(sampleBloc);

        Bloc result = blocService.affecterChambresABloc(chambreIds, "Bloc A");

        assertNotNull(result);
        assertEquals(2, result.getChambres().size());
        verify(chambreRepository).findByNumChambreIn(chambreIds);
        verify(blocRepository).save(sampleBloc);
    }
    @Test
    void testAffecterChambresABloc_ChambresNotFound() {
        when(blocRepository.findByNomBloc("Bloc A")).thenReturn(sampleBloc);
        when(chambreRepository.findByNumChambreIn(anyList())).thenReturn(Collections.emptyList());

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                blocService.affecterChambresABloc(Arrays.asList(999L), "Bloc A")
        );
        assertTrue(exception.getMessage().contains("No Chambres found"));
    }
}