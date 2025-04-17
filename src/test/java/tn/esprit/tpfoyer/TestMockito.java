package tn.esprit.tpfoyer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.tpfoyer.entities.Etudiant;
import tn.esprit.tpfoyer.repositories.EtudiantRepository;
import tn.esprit.tpfoyer.services.EtudiantServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TestMockito {

	@Mock
	private EtudiantRepository etudiantRepository;

	@InjectMocks
	private EtudiantServiceImpl etudiantService;

	@Test
	public void testRetrieveAllEtudiants() {
		// Arrange
		Etudiant etudiant = new Etudiant();
		etudiant.setIdEtudiant(1L);
		when(etudiantRepository.findAll()).thenReturn(List.of(etudiant));

		// Act
		var result = etudiantService.retrieAllEtudiants();

		// Assert
		assertNotNull(result);
		assertEquals(1, result.size());
		verify(etudiantRepository, times(1)).findAll();
	}

	@Test
	public void testRetrieveEtudiant() {
		// Arrange
		Etudiant etudiant = new Etudiant();
		etudiant.setIdEtudiant(1L);
		when(etudiantRepository.findById(etudiant.getIdEtudiant())).thenReturn(Optional.of(etudiant));

		// Act
		Etudiant foundEtudiant = etudiantService.retrieEtudiant(etudiant.getIdEtudiant());

		// Assert
		assertNotNull(foundEtudiant);
		assertEquals(etudiant.getIdEtudiant(), foundEtudiant.getIdEtudiant());
		verify(etudiantRepository, times(1)).findById(etudiant.getIdEtudiant());
	}

	@Test
	public void testAddEtudiant() {
		// Arrange
		Etudiant etudiant = new Etudiant();
		etudiant.setIdEtudiant(1L);
		when(etudiantRepository.save(any(Etudiant.class))).thenReturn(etudiant);

		// Act
		Etudiant savedEtudiant = etudiantService.addCEtudiant(etudiant);

		// Assert
		assertNotNull(savedEtudiant);
		assertEquals(etudiant.getIdEtudiant(), savedEtudiant.getIdEtudiant());
		verify(etudiantRepository, times(1)).save(etudiant);
	}

	@Test
	public void testModifyEtudiant() {
		// Arrange
		Etudiant etudiant = new Etudiant();
		etudiant.setIdEtudiant(1L);
		when(etudiantRepository.save(any(Etudiant.class))).thenReturn(etudiant);

		// Act
		Etudiant modifiedEtudiant = etudiantService.addCEtudiant(etudiant);

		// Assert
		assertNotNull(modifiedEtudiant);
		assertEquals(etudiant.getIdEtudiant(), modifiedEtudiant.getIdEtudiant());
		verify(etudiantRepository, times(1)).save(etudiant);
	}

	@Test
	public void testRemoveEtudiant() {
		// Arrange
		Long etudiantId = 1L;

		// Act
		etudiantService.removeEtudiant(etudiantId);

		// Assert
		verify(etudiantRepository, times(1)).deleteById(etudiantId);
	}


}