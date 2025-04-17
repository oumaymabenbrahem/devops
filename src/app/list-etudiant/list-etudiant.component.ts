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
    this.etudiantService.getAllEtudiants().subscribe(data => {
      this.etudiants = data;
      console.log('Data requested ...',this.etudiants);
    });
  }

  addEtudiant() {
    const newEtudiant = { nomEt: 'Jean', prenomEt: 'Dupont', cin: 12345678, ecole: 'ESPRIT', dateNaissance: new Date() };
    this.etudiantService.addEtudiant(newEtudiant).subscribe(() => {
      this.fetchEtudiants(); // Refresh list after adding
    });
  }

}
