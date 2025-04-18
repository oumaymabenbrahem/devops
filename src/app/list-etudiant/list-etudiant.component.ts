import { Component, OnInit } from '@angular/core';
import { EtudiantService } from '../etudiant.service';

@Component({
  selector: 'app-list-etudiant',
  templateUrl: './list-etudiant.component.html',
  styleUrls: ['./list-etudiant.component.scss']
})
export class ListEtudiantComponent implements OnInit {
  etudiants: any[] = [];

  constructor(private etudiantService: EtudiantService) {}

  ngOnInit() {
    this.fetchEtudiants();
  }

  fetchEtudiants() {
    this.etudiantService.getAllEtudiants().subscribe({
      next: (data) => {
        this.etudiants = data;
        console.log('Data received:', this.etudiants);
      },
      error: (error) => {
        console.error('Error fetching students:', error);
        // You can add more detailed error handling here
        if (error.status) {
          console.error(Status code: ${error.status});
        }
        if (error.message) {
          console.error(Error message: ${error.message});
        }
      }
    });
  }

  addEtudiant() {
    const newEtudiant = { nomEt: 'Jean', prenomEt: 'Dupont', cin: 12345678, ecole: 'ESPRIT', dateNaissance: new Date() };
    this.etudiantService.addEtudiant(newEtudiant).subscribe({
      next: () => {
        console.log('Student added successfully');
        this.fetchEtudiants(); // Refresh list after adding
      },
      error: (error) => {
        console.error('Error adding student:', error);
        if (error.status) {
          console.error(Status code: ${error.status});
        }
        if (error.message) {
          console.error(Error message: ${error.message});
        }
      }
    });
  }

}
