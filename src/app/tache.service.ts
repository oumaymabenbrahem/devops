import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class TacheService {

  // URL de l'API avec l'adresse IP spécifique
  private apiUrl = 'http://172.27.185.6:9002/';

  // Options HTTP avec en-têtes pour éviter les problèmes CORS
  private httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json',
      'Access-Control-Allow-Origin': '*'
    })
  };

  constructor(private http: HttpClient) { }

  // 🔹 Récupérer toutes les tâches
  getAllTaches(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl + 'foyer/api/taches/retrieve-all-taches', this.httpOptions);
  }

  // 🔹 Ajouter une tâche
  addTache(tache: any): Observable<any> {
    return this.http.post<any>(this.apiUrl + 'foyer/api/taches/add-tache', tache, this.httpOptions);
  }

  // 🔹 Mettre à jour une tâche
  updateTache(tache: any): Observable<any> {
    return this.http.put<any>(this.apiUrl + 'foyer/api/taches/update-tache', tache, this.httpOptions);
  }

  // 🔹 Supprimer une tâche
  deleteTache(id: number): Observable<any> {
    return this.http.delete<any>(this.apiUrl + 'foyer/api/taches/delete-tache/' + id, this.httpOptions);
  }

  // 🔹 Ajouter des tâches et les affecter à un étudiant
  addTasksAndAssignToEtudiant(tasks: any[], nomEt: string, prenomEt: string): Observable<any[]> {
    return this.http.post<any[]>(
      `${this.apiUrl}foyer/api/taches/addTasksAndAssignToEtudiant?nomEt=${nomEt}&prenomEt=${prenomEt}`,
      tasks,
      this.httpOptions
    );
  }
}
