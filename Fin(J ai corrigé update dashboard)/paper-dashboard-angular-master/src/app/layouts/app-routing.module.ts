import { NgModule } from "@angular/core";
import { Routes, RouterModule } from "@angular/router";
import { LoginComponent } from "app/USERALLL/USERALL/login/login.component";
import { ProfileComponent } from "app/USERALLL/USERALL/profile/profile.component";
import { RegisterComponent } from "app/USERALLL/USERALL/register/register.component";
import { UpdateUserComponent } from "app/USERALLL/USERALL/user/update-user/update-user.component";
import { UserListComponent } from "app/USERALLL/USERALL/user/user-list/user-list.component";
import { AdminLayoutRoutes } from "./admin-layout/admin-layout.routing";




const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  {
    path: '',
    redirectTo: 'login',
    pathMatch: 'full'
  },
  {
    path: 'admin',
    children: AdminLayoutRoutes  // Utilisez les routes admin comme sous-routes de 'admin'
  },
  
  //{ path: '**', redirectTo: 'dashboardComponent' }

  
//  { path: '**', redirectTo: 'login' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }