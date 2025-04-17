import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class EtudiantService {

   private apiUrl = 'http://172.19.165.107:9001/';


  constructor(private http: HttpClient) { }

  // 🔹 Ajouter un étudiant
  addEtudiant(etudiant: any): Observable<any> {
    return this.http.post<any>(this.apiUrl+'foyer/etudiant/add-etudiant', etudiant);
  }

  // 🔹 Récupérer tous les étudiants
  getAllEtudiants(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl+'foyer/etudiant/retrieve-all-etudiants');
  }
}
