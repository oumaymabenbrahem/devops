import { Component } from '@angular/core';
import { EtudiantService } from '../etudiant.service';

@Component({
  selector: 'app-list-etudiant',
  templateUrl: './list-etudiant.component.html',
  styleUrls: ['./list-etudiant.component.scss']
})
export class ListEtudiantComponent {
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
    const newEtudiant = { nom: 'Jean', age: 22 };
    this.etudiantService.addEtudiant(newEtudiant).subscribe(() => {
      this.fetchEtudiants(); // Refresh list after adding
    });
  }

}
