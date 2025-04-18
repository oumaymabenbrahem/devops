import { Component, OnInit } from '@angular/core';
import { TacheService } from '../tache.service';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-list-tache',
  templateUrl: './list-tache.component.html',
  styleUrls: ['./list-tache.component.scss']
})
export class ListTacheComponent implements OnInit {
  taches: any[] = [];
  loading: boolean = false;
  error: string | null = null;

  // Propriété publique pour l'URL de l'API (accessible depuis le template)
  apiUrl: string = '';

  constructor(private tacheService: TacheService) {
    // Récupérer l'URL de l'API pour l'afficher dans le template
    this.apiUrl = this.tacheService['apiUrl'] + 'foyer/api/taches/retrieve-all-taches';
  }

  ngOnInit() {
    console.log('Initialisation du composant ListTacheComponent');
    console.log('Appel de l\'API à:', this.tacheService['apiUrl'] + 'foyer/api/taches/retrieve-all-taches');
    this.fetchTaches();
  }

  fetchTaches() {
    this.loading = true;
    this.error = null;

    this.tacheService.getAllTaches().subscribe({
      next: (data: any[]) => {
        this.taches = data;
        this.loading = false;
        console.log('Tâches récupérées:', this.taches);

        // Vérifier si les données sont vides ou nulles
        if (!data || data.length === 0) {
          console.log('Aucune tâche n\'a été trouvée dans la réponse API');
        }
      },
      error: (error: HttpErrorResponse) => {
        this.loading = false;
        console.error('Erreur lors de la récupération des tâches:', error);

        // Message d'erreur plus détaillé
        if (error.status === 0) {
          this.error = 'Impossible de se connecter au serveur. Vérifiez votre connexion réseau ou contactez l\'administrateur.';
          console.error('Erreur de connexion au serveur. Problème réseau ou CORS.');
        } else if (error.status === 404) {
          this.error = 'API introuvable. Vérifiez l\'URL de l\'API.';
          console.error(`API introuvable: ${this.tacheService['apiUrl']}foyer/api/taches/retrieve-all-taches`);
        } else if (error.status === 500) {
          this.error = 'Erreur serveur. Contactez l\'administrateur.';
          console.error('Erreur interne du serveur:', error.message);
        } else {
          this.error = `Erreur ${error.status}: ${error.message || 'Détails non disponibles'}`;
        }

        // Informations de débogage supplémentaires
        console.error(`URL appelée: ${this.tacheService['apiUrl']}foyer/api/taches/retrieve-all-taches`);
        console.error(`Code d'état: ${error.status}`);
        console.error(`Message: ${error.message}`);
        if (error.error) {
          console.error('Détails de l\'erreur:', error.error);
        }
      }
    });
  }

  // Méthode pour obtenir les initiales d'une personne
  getInitials(nom: string, prenom: string): string {
    if (!nom || !prenom) return '?';
    return (nom.charAt(0) + prenom.charAt(0)).toUpperCase();
  }

  // Méthode pour formater le type de tâche pour l'affichage
  getTypeTacheLabel(type: string): string {
    switch (type) {
      case 'MENAGER':
        return 'Ménager';
      case 'JARDINAGE':
        return 'Jardinage';
      case 'BRICOLAGE':
        return 'Bricolage';
      default:
        return type || 'Non spécifié';
    }
  }

  // Méthode pour calculer le coût total d'une tâche
  calculateCost(duree: number, tarifHoraire: number): number {
    return duree * tarifHoraire;
  }
}
