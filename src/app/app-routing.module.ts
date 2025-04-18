import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ListEtudiantComponent } from './list-etudiant/list-etudiant.component';
import { ListTacheComponent } from './list-tache/list-tache.component';

const routes: Routes = [
  { path: '', redirectTo: '/taches', pathMatch: 'full' },
  { path: 'etudiants', component: ListEtudiantComponent },
  { path: 'taches', component: ListTacheComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
