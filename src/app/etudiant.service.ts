import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class EtudiantService {

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

  // 🔹 Ajouter un étudiant
  addEtudiant(etudiant: any): Observable<any> {
    return this.http.post<any>(this.apiUrl+'foyer/etudiant/add-etudiant', etudiant, this.httpOptions);
  }

  // 🔹 Récupérer tous les étudiants
  getAllEtudiants(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl+'foyer/etudiant/retrieve-all-etudiants', this.httpOptions);
  }
}
