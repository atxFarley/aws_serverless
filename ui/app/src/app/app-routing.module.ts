import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { MainComponent } from './main/main.component';
import { AdminComponent } from './admin/admin.component';
import { HistoryComponent } from './history/history.component';


const routes: Routes = [
  {path:  "", pathMatch:  "full",redirectTo:  "main"},
  {path: "main", component: MainComponent},
  {path: "admin", component: AdminComponent},
  {path: "history", component: HistoryComponent},

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
