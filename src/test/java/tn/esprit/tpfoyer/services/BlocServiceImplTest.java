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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
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
        sampleBloc.setChambres(Set.of(new Chambre()));
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
    }

    @Test
    void findByFoyerUniversiteNomUniversite() {
    }

    @Test
    void affecterChambresABloc() {
    }
}